package com.eggoz.ecommerce.view.address

//import com.cashfree.pg.CFPaymentService
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.FragmentAddressBinding
import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.localdata.room.MyDatabase
import com.eggoz.ecommerce.localdata.room.RoomCart
import com.eggoz.ecommerce.network.repository.RetrofitClient
import com.eggoz.ecommerce.utils.Constants
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.address.adapter.AddressAdapter
import com.eggoz.ecommerce.view.address.model.CartToken
import com.eggoz.ecommerce.view.address.viewmodel.AddressRepository
import com.eggoz.ecommerce.view.address.viewmodel.AddressViewModel
import com.eggoz.ecommerce.view.address.viewmodel.AddressViewModelFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.payu.base.models.ErrorResponse
import com.payu.base.models.PayUPaymentParams
import com.payu.checkoutpro.PayUCheckoutPro
import com.payu.checkoutpro.utils.PayUCheckoutProConstants
import com.payu.ui.model.listeners.PayUCheckoutProListener
import com.payu.ui.model.listeners.PayUHashGenerationListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class AddressFragment : Fragment() {

    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!

    private lateinit var addressAdapter: AddressAdapter
    private lateinit var viewModel: AddressViewModel

    private lateinit var dialog: Loadinddialog

    private var totalamount = 0.0
    private var ordertype = ""
    private var addressid = -1
    private lateinit var job: Job
    private var cartlist: ArrayList<RoomCart> = ArrayList()

    private var item_id: Int = -1


    private var start_date = ""
    private var expiry_date = ""
    private var quantity = ""
    private var subitem = "-1"
    private var days: ArrayList<Int>? = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddressBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        val userPreferences = UserPreferences(requireContext())
        val repository =
            AddressRepository(userPreferences, MyDatabase.getInstance(context = requireContext()))
        val viewmodelFat = AddressViewModelFactory(repository)


        viewModel = ViewModelProvider(this, viewmodelFat).get(AddressViewModel::class.java)

        dialog = Loadinddialog()
        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {
//            mid = userPreferences!!.userid.first() ?: -1
            val bundle: Bundle? = arguments
            if (bundle != null) {
                ordertype = bundle.getString("ordertype", "")
                if (ordertype == "cart") {
                    totalamount = bundle.getDouble("amount", 0.0)

                } else if (ordertype == "single") {
                    totalamount = bundle.getDouble("amount", 0.0)
                    item_id = bundle.getInt("product", -1)

                } else if (ordertype == "subitem") {
                    totalamount = bundle.getDouble("amount", 0.0)
                    start_date = bundle.getString("start_date", "")
                    expiry_date = bundle.getString("expiry_date", "")
                    quantity = bundle.getString("quantity", "0")
                    subitem = bundle.getString("product", "0")
                    days = bundle.getIntegerArrayList("days") as ArrayList<Int>
                }
            }
            addressList()
        }
        binding.apply {

            addressAdapter = AddressAdapter()
            viewaddressAdapter = addressAdapter
            btnBack.setOnClickListener {
                Navigation.findNavController(root)
                    .popBackStack()
            }
            btnSubmit.setOnClickListener {
                when (ordertype) {
                    "cart" -> {
                        getTokenforcart()
                    }
                    "single" -> {
                        getTokenforsingle()
                    }
                    "subitem" -> {
                        getTokenforsubitem()
                    }
                }
            }

            txtAddAddress.setOnClickListener {
                Navigation.findNavController(root)
                    .navigate(R.id.action_nav_address_list_to_nav_manageAddress)
            }
        }
        getCartlist()

    }

    private fun getTokenforsubitem() {

        expiry_date = getArguments()?.getString("expiry_date", "") ?: ""
        quantity = getArguments()?.getString("quantity", "0") ?: "0"
        subitem = getArguments()?.getString("product", "0") ?: "0"
        days = arguments?.getIntegerArrayList("days") as ArrayList<Int>

        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {
            viewModel.getTokenforsubitem(
                totalamount,
                addressid,
                item_id,
                start_date,
                expiry_date,
                quantity.toInt(),
                subitem.toInt(),
                days!!,
                pay_by_wallet = false,
                date = "11-11-2021"
            )
            viewModel.responTokenforsingle.observe(viewLifecycleOwner) {
                // if (it.body()?.errorType == null) {

                if (dialog.isShowing())
                    dialog.dismiss()
                paymentcart(
                    it.body()?.phone ?: "",
                    it.body()?.token ?: "",
                    it.body()?.txnid ?: "",
                    it.body()?.furl ?: "",
                    it.body()?.surl ?: "",
                    it.body()?.productinfo ?: "",
                    it.body()?.firstname ?: "",
                    it.body()?.email ?: "",
                    it.body()?.amount ?: "",
                    it.body()?.merchant_key ?: ""
                )


            }
        }
    }

    private fun getTokenforsingle() {


        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val date = simpleDateFormat.format(Date())


        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {
            viewModel.getTokenforsingle(
                totalamount,
                addressid,
                item_id,
                date,
                pay_by_wallet = false
            )
            viewModel.responTokenforsingle.observe(viewLifecycleOwner) {
                if (it.body()?.errorType == null) {

                    if (dialog.isShowing())
                        dialog.dismiss()
                    paymentcart(
                        it.body()?.phone ?: "",
                        it.body()?.token ?: "",
                        it.body()?.txnid ?: "",
                        it.body()?.furl ?: "",
                        it.body()?.surl ?: "",
                        it.body()?.productinfo ?: "",
                        it.body()?.firstname ?: "",
                        it.body()?.email ?: "",
                        it.body()?.amount ?: "",
                        it.body()?.merchant_key ?: ""
                    )
                }

            }
        }
    }


    private fun getCartlist() {

        job = lifecycleScope.launch {
            viewModel.getCart2().collect {
                Log.d("TAG", "getCartlist: size ${it.size}")
                if (it.isNotEmpty()) {
                    cartlist.addAll(it)
                }
                Log.d("TAG", "getCartlist2: size ${cartlist.size}")

            }

        }
    }

    private fun getTokenforcart() {

        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val date = simpleDateFormat.format(Date())


        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {
            viewModel.getcartToken(
                totalamount,
                addressid,
                cartlist,
                date,
                pay_by_wallet = false
            )
            viewModel.responCartToken.observe(viewLifecycleOwner) {
                if (it.body()?.errorType == null) {

                    if (dialog.isShowing())
                        dialog.dismiss()
                    paymentcart(
                        it.body()?.phone ?: "",
                        it.body()?.token ?: "",
                        it.body()?.txnid ?: "",
                        it.body()?.furl ?: "",
                        it.body()?.surl ?: "",
                        it.body()?.productinfo ?: "",
                        it.body()?.firstname ?: "",
                        it.body()?.email ?: "",
                        it.body()?.amount ?: "",
                        it.body()?.merchant_key ?: ""
                    )
                }

            }
        }
    }

    private fun paymentcart(
        phone: String,
        token: String,
        txnid: String,
        furl: String,
        surl: String,
        productinfo: String,
        firstname: String,
        email: String,
        amount: String,
        merchant_key: String
    ) {
        val payUPaymentParams = PayUPaymentParams.Builder()
            .setAmount(amount)
            .setIsProduction(Constants.payU_IsProduction)
            .setKey(merchant_key)
            .setPhone(phone)
            .setProductInfo(productinfo)
            .setTransactionId(txnid)
            .setFirstName(firstname)
            .setEmail(email)
            .setSurl(surl)
            .setFurl(furl)
            .build()
        viewModel.payment_count = 0

        PayUCheckoutPro.open(
            requireActivity(),
            payUPaymentParams,
            object : PayUCheckoutProListener {

                override fun onPaymentSuccess(response: Any) {
                    response as HashMap<*, *>
                    val payUResponse = response[PayUCheckoutProConstants.CP_PAYU_RESPONSE]
                    val merchantResponse = response[PayUCheckoutProConstants.CP_MERCHANT_RESPONSE]
                    /*  Toast.makeText(
                          requireContext(),
                          "$payUResponse $merchantResponse",
                          Toast.LENGTH_SHORT
                      ).show()*/
                    Navigation.findNavController(binding.root)
                        .popBackStack()
                }


                override fun onPaymentFailure(response: Any) {
                    response as HashMap<*, *>
                    val payUResponse = response[PayUCheckoutProConstants.CP_PAYU_RESPONSE]
                    val merchantResponse = response[PayUCheckoutProConstants.CP_MERCHANT_RESPONSE]
                }


                override fun onPaymentCancel(isTxnInitiated: Boolean) {
                    Log.d("TAG", "onPaymentCancel: ")
                }


                override fun onError(errorResponse: ErrorResponse) {
                    val errorMessage: String
                    if (errorResponse.errorMessage != null && errorResponse.errorMessage!!.isNotEmpty())
                        errorMessage = errorResponse.errorMessage!!
                    else
                        errorMessage = resources.getString(R.string.some_error_occurred)
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }

                override fun setWebViewProperties(webView: WebView?, bank: Any?) {
                    //For setting webview properties, if any. Check Customized Integration section for more details on this
                }

                override fun generateHash(
                    map: HashMap<String, String?>,
                    hashGenerationListener: PayUHashGenerationListener
                ) {
                    if (map.containsKey(PayUCheckoutProConstants.CP_HASH_STRING)
                        && map.containsKey(PayUCheckoutProConstants.CP_HASH_STRING) != null
                        && map.containsKey(PayUCheckoutProConstants.CP_HASH_NAME)
                        && map.containsKey(PayUCheckoutProConstants.CP_HASH_NAME) != null
                    ) {

                        val hashData = map[PayUCheckoutProConstants.CP_HASH_STRING]
                        val hashName = map[PayUCheckoutProConstants.CP_HASH_NAME]



                        if (viewModel.payment_count == 0) {
                            viewModel.payment_count++
                            if (!TextUtils.isEmpty(token)) {
                                val hashMap: HashMap<String, String?> = HashMap()
                                hashMap[hashName!!] = token!!
                                Log.d("TAG", "generateHash:2 $token")
                                hashGenerationListener.onHashGenerated(hashMap)

                            }
                        } else {
                            val call: Call<CartToken> =
                                RetrofitClient().retrofitApiSerInterceptor(token = viewModel.token)
                                    .paymentHash(hashData = hashData!!)
                            call.enqueue(object : Callback<CartToken> {
                                override fun onResponse(
                                    call: Call<CartToken>,
                                    response: Response<CartToken>
                                ) {
                                    try {
                                        if (response.isSuccessful) {
                                            val data = response.body();
                                            if (!TextUtils.isEmpty(data?.hash)) {
                                                val hashMap: HashMap<String, String?> = HashMap()
                                                hashMap[hashName!!] = data?.hash
                                                hashGenerationListener.onHashGenerated(hashMap)
                                            }
                                        } else {
                                            val gson = Gson()
                                            val type = object : TypeToken<CartToken>() {}.type
                                            val errorRes: CartToken? = gson.fromJson(
                                                response.errorBody()!!.charStream(),
                                                type
                                            )
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }

                                override fun onFailure(call: Call<CartToken>, t: Throwable) {
                                    Log.d("TAG", "onFailure: ${t.message}")
                                }

                            })

                        }

                    }
                }
            })


    }

    private fun addressList() {
        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {
            viewModel.responAddress.observe(viewLifecycleOwner) {

                if (dialog.isShowing())
                    dialog.dismiss()

                if (it?.errorType != null) {

                    if (it.errors != null)
                        if (it.errors!![0].message == "Signature has expired.")
                            Navigation.findNavController(binding.recAddress)
                                .navigate(R.id.action_nav_address_list_to_nav_sigin1)

                } else {
                    if (it.addresses != null) {
//                        addressAdapter = AddressAdapter(it.addresses)
                        binding.apply {
                            if (it.addresses != null)
                                addressid = it.addresses[0].id ?: -1
                            addressAdapter?.submitList(it.addresses)
//                            recAddress.adapter = addressAdapter
//                            (recAddress.adapter as AddressAdapter).notifyDataSetChanged()
                        }
                    }
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        job.cancel()
    }
}