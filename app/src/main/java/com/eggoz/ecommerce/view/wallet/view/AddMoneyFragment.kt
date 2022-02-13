package com.eggoz.ecommerce.view.wallet.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashfree.pg.CFPaymentService
import com.eggoz.ecommerce.MainActivity
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentAddMoneyBinding
import com.eggoz.ecommerce.utils.Constants
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.wallet.viewmodel.WalletViewModel
import com.eggoz.ecommerce.view.wallet.adapter.PromoAdapter
import com.eggoz.ecommerce.view.wallet.viewmodel.WalletRepository
import com.eggoz.ecommerce.view.wallet.viewmodel.WalletViewModelFactory
import java.util.HashMap


class AddMoneyFragment : Fragment() {

    private var _binding: FragmentAddMoneyBinding? = null
    private val binding get() = _binding!!


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
        _binding = FragmentAddMoneyBinding.inflate(inflater, container, false)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        init()
        return binding.root
    }

    private fun init() {
        dialog = Loadinddialog()

        val userPreferences = UserPreferences(requireContext())
        val repository = WalletRepository(userPreferences)
        val viewmodelFat = WalletViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewmodelFat)[WalletViewModel::class.java]
        walletbal = this.arguments?.getDouble("bal", 0.0) ?: 0.0
        walletid = this.arguments?.getInt("walletid", -1) ?: -1

        promoAdapter = PromoAdapter()

        binding.apply {
            viewpromoAdapter = promoAdapter
            btnBack.setOnClickListener {
                Navigation.findNavController(binding.root)
                    .popBackStack()
            }
            txtWalletbal.text = "Available Eggoz Balance ₹ $walletbal"
            txtbtnAddmoney.setOnClickListener {
                if (edtAmount.text.toString().isNotEmpty() && binding.edtAmount.text.toString()
                        .toInt() >= 100
                )
                    getWalletToken()
                else Toast.makeText(requireContext(), "Minimum Amount is ₹ 100", Toast.LENGTH_LONG)
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
                            txtbtnAddmoney.text = "Add ₹ ${amount.toInt()} & Promocode"
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
        )
        viewModel.responCartToken.observe(viewLifecycleOwner, {
            if (dialog.isShowing())
                dialog.dismiss()
            if (it.errorType == null) {
                paymentcart(
                    it.payload?.appId ?: "",
                    it.payload?.orderId ?: "",
                    it.payload?.orderAmount ?: 0.0,
                    it.payload?.orderNote ?: "",
                    it.payload?.customerName ?: "",
                    it.payload?.customerPhone ?: "",
                    it.payload?.customerEmail ?: "",
                    it.payload?.notifyUrl ?: "",
                    it.gatewayResponse?.cftoken ?: ""
                )
            }

        })
    }

    private fun paymentcart(
        appId: String,
        orderId: String,
        orderAmount: Double,
        orderNote: String,
        customerName: String,
        customerPhone: String,
        customerEmail: String,
        notifyUrl: String,
        ctoken: String
    ) {
        val parms = HashMap<String, String>()
        parms.put("appId", appId)
        parms.put("orderId", orderId)
        parms.put("orderAmount", orderAmount.toString())
        parms.put("orderNote", orderNote)
        parms.put("customerName", customerName)
        parms.put("customerPhone", customerPhone)
        parms.put("customerEmail", customerEmail)
        parms.put("notifyUrl", notifyUrl)

        (requireActivity() as MainActivity).paymentType = "wallet"
        Log.d("TAG", "paymentcart: $parms \n cftoken $ctoken")

        CFPaymentService.getCFPaymentServiceInstance()
            .doPayment(requireActivity(), parms, ctoken, Constants.cashFree_Server)
    }

    private fun getPromo() {
        if (!dialog.isShowing())
            dialog.create(requireContext())
        viewModel.walletPromo()
        viewModel.responWalletPromo.observe(viewLifecycleOwner, {

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
        })
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}