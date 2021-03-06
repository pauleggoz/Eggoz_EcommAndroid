package com.eggoz.ecommerce.view.wallet.view

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.ViewModelFactory
import com.eggoz.ecommerce.databinding.FragmentAddMoneyBinding
import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.network.repository.RetrofitClient
import com.eggoz.ecommerce.utils.Constants
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.address.model.CartToken
import com.eggoz.ecommerce.view.wallet.adapter.PromoAdapter
import com.eggoz.ecommerce.view.wallet.viewmodel.WalletRepository
import com.eggoz.ecommerce.view.wallet.viewmodel.WalletViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.payu.base.models.ErrorResponse
import com.payu.base.models.PayUPaymentParams
import com.payu.checkoutpro.PayUCheckoutPro
import com.payu.checkoutpro.utils.PayUCheckoutProConstants
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_HASH_NAME
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_HASH_STRING
import com.payu.ui.model.listeners.PayUCheckoutProListener
import com.payu.ui.model.listeners.PayUHashGenerationListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddMoneyFragment : Fragment() {

    private lateinit var binding: FragmentAddMoneyBinding
//    private val binding get() = _binding!!


    private lateinit var dialog: Loadinddialog
    private lateinit var viewModel: WalletViewModel

    private var promolist: ArrayList<String> = ArrayList()
    private var promoamount: ArrayList<String> = ArrayList()
    private var promoid: ArrayList<Int> = ArrayList()

    private var amount = 0.0
    private var walletbal = 0.0
    private var walletid = -1
    private var selectpromoid = -1
    private var promocodeName = ""

    private lateinit var promoAdapter: PromoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMoneyBinding.inflate(inflater, container, false)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        init()
        return binding.root
    }

    private fun init() {
        dialog = Loadinddialog()

        val repository = WalletRepository(UserPreferences(requireContext()))
        val viewmodelFat = ViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewmodelFat)[WalletViewModel::class.java]
        walletbal = this.arguments?.getDouble("bal", 0.0) ?: 0.0
        walletid = this.arguments?.getInt("walletid", -1) ?: -1

        promoAdapter = PromoAdapter()

        binding.apply {
            viewpromoAdapter = promoAdapter
            btnBack.setOnClickListener {
                Navigation.findNavController(root)
                    .popBackStack()
            }
            txtWalletbal.text = "Available Eggoz Balance ??? $walletbal"
            txtbtnAddmoney.setOnClickListener {
                if (edtAmount.text.toString().isNotEmpty() && edtAmount.text.toString()
                        .toInt() >= 100
                )
                    getWalletToken()
                else Toast.makeText(requireContext(), "Minimum Amount is ??? 100", Toast.LENGTH_LONG)
                    .show()
            }
            edtAmount.addTextChangedListener {
                if (it.toString() == "")
                    amount = 0.0
                else {
                    amount = it.toString().toDouble()
                    txtbtnAddmoney.text = "Add Money"
                    for (i in 0 until promoamount.size) {
                        if (amount >= promoamount[i].toDouble()) {
                            txtbtnAddmoney.text = "Add ??? ${amount.toInt()} & Promocode"
                            promocodeName = promolist[i]
                            selectpromoid = promoid[i]

                        }
                    }
                }
            }
        }
        getPromo()
    }

    private fun getWalletToken() {
        if (!dialog.isShowing())
            dialog.create(requireContext())
        viewModel.getWalletToken(
            walletid,
            selectpromoid,
            binding.edtAmount.text.toString().toInt()
        ).observe(viewLifecycleOwner) {
            if (dialog.isShowing())
                dialog.dismiss()
            if (it.errorType == null) {
                paymentwallet(
                    it.phone ?: "",
                    it.token ?: "",
                    it.txnid ?: "",
                    it.furl ?: "",
                    it.surl ?: "",
                    it.productinfo ?: "",
                    it.firstname ?: "",
                    it.email ?: "",
                    it.amount ?: "",
                    it.merchant_key ?: ""
                )
            }

        }
    }

    private fun paymentwallet(
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
        viewModel.walletCount = 0

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
                    if (map.containsKey(CP_HASH_STRING)
                        && map.containsKey(CP_HASH_STRING) != null
                        && map.containsKey(CP_HASH_NAME)
                        && map.containsKey(CP_HASH_NAME) != null
                    ) {

                        val hashData = map[CP_HASH_STRING]
                        val hashName = map[CP_HASH_NAME]


                        /* var hash2: String = token
                         if (hashName?.equals(PayUCheckoutProConstants.CP_LOOKUP_API_HASH) == true) {

                             //Calculate HmacSHA1 hash using the hashData and merchant secret key
                             hash2 = HashGenerationUtils.generateHashFromSDK(
                                 hashData!!,
                                 "xXCIflyU",
                                 "Txzn3U"
                             ) ?: ""
                         } else {
                             //calculate SDH-512 hash using hashData and salt
                             hash2 = HashGenerationUtils.generateHashFromSDK(
                                 hashData!!,
                                 "xXCIflyU"
                             ) ?: ""
                         }*/

//                        Log.d("TAG", "generateHash: $hash2")

//                        var hash: String = token

                        //Below hash should be calculated only when integrating Multi-currency support. If not integrating MCP
                        // then no need to have this if check.
                        /*      if (hashName?.equals(PayUCheckoutProConstants.CP_LOOKUP_API_HASH) == true){

                                  //Calculate HmacSHA1 hash using the hashData and merchant secret key
                                  hash = HashGenerationUtils.generateHashFromSDK(
                                      hashData!!,
                                      "xXCIflyU",
                                      "Txzn3U"
                                  ) ?:""
                              } else {
                                  //calculate SDH-512 hash using hashData and salt
                                  hash = HashGenerationUtils.generateHashFromSDK(
                                      hashData!!,
                                      "xXCIflyU"
                                  ) ?:""
                              }
      */



                        if (viewModel.walletCount == 0) {
                            viewModel.walletCount++
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

    private fun getPromo() {
        if (!dialog.isShowing())
            dialog.create(requireContext())
        viewModel.walletPromo()
        viewModel.responWalletPromo.observe(viewLifecycleOwner) {

            if (dialog.isShowing())
                dialog.dismiss()

            if (it?.errorType == null) {
                promoAdapter.submitList(it.results)
                if (it.results != null)
                    for (i in it.results.indices) {
                        promolist.add(it.results[i].promo ?: "")
                        promoamount.add(it.results[i].amount ?: "")
                        promoid.add(it.results[i].id ?: -1)
                    }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
//        _binding = null
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}