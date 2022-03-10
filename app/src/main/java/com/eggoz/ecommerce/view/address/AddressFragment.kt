package com.eggoz.ecommerce.view.address

import android.annotation.SuppressLint
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
import com.eggoz.ecommerce.ViewModelFactory
import com.eggoz.ecommerce.databinding.FragmentAddressBinding
import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.localdata.room.MyDatabase
import com.eggoz.ecommerce.localdata.room.RoomCart
import com.eggoz.ecommerce.network.repository.RetrofitClient
import com.eggoz.ecommerce.utils.Constants
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.address.adapter.AddressAdapter
import com.eggoz.ecommerce.view.address.bottomsheet.PaymentOptionBottomSheet
import com.eggoz.ecommerce.view.address.model.CartToken
import com.eggoz.ecommerce.view.address.viewmodel.AddressRepository
import com.eggoz.ecommerce.view.address.viewmodel.AddressViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.payu.base.models.ErrorResponse
import com.payu.base.models.PayUPaymentParams
import com.payu.checkoutpro.PayUCheckoutPro
import com.payu.checkoutpro.utils.PayUCheckoutProConstants
import com.payu.ui.model.listeners.PayUCheckoutProListener
import com.payu.ui.model.listeners.PayUHashGenerationListener
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class AddressFragment : Fragment() {

    private lateinit var binding: FragmentAddressBinding

    private lateinit var addressAdapter: AddressAdapter
    private lateinit var viewModel: AddressViewModel

    private lateinit var dialog: Loadinddialog

    private var cartlist: ArrayList<RoomCart> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddressBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val repository =
            AddressRepository(
                UserPreferences(requireContext()),
                MyDatabase.getInstance(context = requireContext())
            )
        val viewmodelFat = ViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewmodelFat)[AddressViewModel::class.java]

        dialog = Loadinddialog()
        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {
            val bundle: Bundle? = arguments
            if (bundle != null) {
                viewModel.ordertype = bundle.getString("ordertype", "")
                when (viewModel.ordertype) {
                    "cart" -> {
                        viewModel.totalamount = bundle.getDouble("amount", 0.0)

                    }
                    "single" -> {
                        viewModel.totalamount = bundle.getDouble("amount", 0.0)
                        viewModel.item_id = bundle.getInt("product", -1)

                    }
                    "subitem" -> {

                        viewModel.totalamount = bundle.getDouble("totalamount", 0.0)
                        viewModel.start_date = bundle.getString("start_date", "")
                        viewModel.expiry_date = bundle.getString("expiry_date", "")
                        viewModel.quantity = bundle.getString("quantity", "0")
                        viewModel.subitem = bundle.getString("product", "0")
                        viewModel.schduleTime = bundle.getString("schduleTime", "0")
                        viewModel.days = bundle.getIntegerArrayList("days") as ArrayList<Int>
                        viewModel.dates = bundle.getStringArrayList("dates") as ArrayList<String>

                        viewModel.amount = bundle.getDouble("amount", 0.0)
                        viewModel.slot = bundle.getString("slot", "")
                        viewModel.qnt = bundle.getInt("qnt", -1)
                        viewModel.item_id = bundle.getInt("item_id", -1)
                        viewModel.subId = bundle.getInt("subId", -1)
                    }
                }
            }
            addressList()
        }
        binding.apply {

            addressAdapter = AddressAdapter {iteam,current->

                viewModel.addressid=iteam?.id ?:-1
                viewModel.addresses[viewModel.selectedLoc].isSelected =false
                viewModel.addresses[current].isSelected =true
                viewModel.selectedLoc=current

                addressAdapter.submitList(viewModel.addresses)

            }
            viewaddressAdapter = addressAdapter
            btnBack.setOnClickListener {
                Navigation.findNavController(root)
                    .popBackStack()
            }
            btnSubmit.setOnClickListener {
                PaymentOptionBottomSheet { isWallet ->
                    when (viewModel.ordertype) {
                        "cart" -> {
                            viewModel.isCart = true
                            getTokenforcart(isWallet)
                        }
                        "single" -> {
                            viewModel.isCart = false
                            getTokenforsingle(isWallet)
                        }
                        "subitem" -> {
                            viewModel.isCart = false
                            getTokenforsubitem(isWallet)
                        }
                    }
                }.show(childFragmentManager, "paymentoption")
            }

            txtAddAddress.setOnClickListener {
                Navigation.findNavController(root)
                    .navigate(R.id.action_nav_address_list_to_nav_manageAddress)
            }
        }
        getCartlist()

    }



    private fun getTokenforsubitem(payByWallet: Boolean) {


        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {

             viewModel.getTokenforsubitem(
                 pay_by_wallet = payByWallet
             )
             viewModel.responTokenforsub.observe(viewLifecycleOwner) {

                 if (dialog.isShowing())
                     dialog.dismiss()

                 it.body()?.errorType?.let { _errorty->
                     Toast.makeText(
                         requireContext(),
                        "${ it.body()?.errorType} ${ it.body()?.errors?.get(0)?.message} ${ it.body()?.errors?.get(0)?.field} ",
                         Toast.LENGTH_SHORT
                     ).show()

                 }


                 it.body()?.hash.let { _has->
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

    @SuppressLint("SimpleDateFormat")
    private fun getTokenforsingle(pay_by_wallet: Boolean) {


        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val date = simpleDateFormat.format(Date())


        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {
            viewModel.getTokenforsingle(
                date,
                pay_by_wallet = pay_by_wallet
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

        lifecycleScope.launch {
            viewModel.getCart().collect {
                Log.d("TAG", "getCartlist: size ${it.size}")
                if (it.isNotEmpty()) {
                    cartlist.addAll(it)
                }
                Log.d("TAG", "getCartlist2: size ${cartlist.size}")

            }

        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getTokenforcart(pay_by_wallet: Boolean) {

        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val date = simpleDateFormat.format(Date())


        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {
            viewModel.getcartToken(
                cartlist,
                date,
                pay_by_wallet = pay_by_wallet
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
//                    val payUResponse = response[PayUCheckoutProConstants.CP_PAYU_RESPONSE]
//                    val merchantResponse = response[PayUCheckoutProConstants.CP_MERCHANT_RESPONSE]
                    if (viewModel.isCart)
                        viewModel.CartClear()
                    Navigation.findNavController(binding.root)
                        .popBackStack()
                }


                override fun onPaymentFailure(response: Any) {
                    response as HashMap<*, *>
                    val payUResponse = response[PayUCheckoutProConstants.CP_PAYU_RESPONSE]
//                    val merchantResponse = response[PayUCheckoutProConstants.CP_MERCHANT_RESPONSE]

                    Toast.makeText(requireContext(), "$payUResponse", Toast.LENGTH_LONG).show()
                }


                override fun onPaymentCancel(isTxnInitiated: Boolean) {
                    Log.d("TAG", "onPaymentCancel: ")
                }


                override fun onError(errorResponse: ErrorResponse) {
                    val errorMessage: String =
                        if (errorResponse.errorMessage != null && errorResponse.errorMessage!!.isNotEmpty())
                            errorResponse.errorMessage!!
                        else
                            resources.getString(R.string.some_error_occurred)
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
                                hashMap[hashName!!] = token
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
                                            val data = response.body()
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
                                            errorRes?.errors?.let {
                                                Toast.makeText(
                                                    requireContext(),
                                                    it[0].message,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
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
                        binding.apply {
                            viewModel.addressid = it.addresses[0].id ?: -1
                            viewModel.addresses=it.addresses
                            viewModel.addresses[0].isSelected=true
                            addressAdapter.submitList(viewModel.addresses)
                        }
                    }
                }

            }
        }
    }

   /* override fun onDestroy() {
        super.onDestroy()
        _binding = null
//        job.cancel()
    }*/
}