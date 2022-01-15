package com.eggoz.ecommerce.view.wallet

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentWalletBinding
import com.eggoz.ecommerce.network.model.Orderhistory
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.wallet.adapter.PromoAdapter
import com.eggoz.ecommerce.view.wallet.adapter.WalletHistoryAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class WalletFragment : Fragment() {

    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: WalletViewModel
    private lateinit var walletadapter: WalletHistoryAdapter
    private lateinit var promoAdapter:PromoAdapter
    private var userPreferences: UserPreferences? = null
    private var userid: Int = -1
    private var Customer_id: Int = -1
    private lateinit var dialog: Loadinddialog

    private lateinit var orderitem: ArrayList<Orderhistory.Result.OrderLines.OrderItem?>
    private lateinit var orderid: ArrayList<String?>
    private lateinit var orderdate: ArrayList<String?>
    private var walletBalance=0.0
    private var walletId=-1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        dialog = Loadinddialog()
        userPreferences = UserPreferences(requireContext())
        viewModel = ViewModelProvider(this).get(WalletViewModel::class.java)


        orderitem = ArrayList()
        orderdate = ArrayList()
        orderid = ArrayList()


        lifecycleScope.launch {
            Customer_id = userPreferences!!.Customer_id.first() ?: -1
            userid = userPreferences!!.userid.first() ?: -1
            Log.d("data","userid $userid Customer_id $Customer_id")
            if (userid != -1) {
                walletBalance()
                orderhistory()
                Promo()
            }
        }

        binding.apply {
            txtADDMONEY.setOnClickListener {

                val bundle = Bundle()
                bundle.putDouble("bal", walletBalance)
                bundle.putInt("walletid", walletId)
                Navigation.findNavController(root)
                    .navigate(R.id.action_nav_wallet_to_nav_addmoney,bundle)
            }
            recWallethis.apply {
                layoutManager = LinearLayoutManager(requireContext())
                itemAnimator = DefaultItemAnimator()
                isNestedScrollingEnabled = false
            }
            recyPromo.apply {
                itemAnimator = DefaultItemAnimator()
                isNestedScrollingEnabled = false
            }

        }

    }
    private fun Promo(){

        if (!dialog.isShowing())
            dialog.create(requireContext())
        viewModel.walletPromo(requireContext())
        viewModel.responWalletPromo.observe(viewLifecycleOwner, {

            if (dialog.isShowing())
                dialog.dismiss()
            if (it?.errorType != null) {

            }else{
                promoAdapter = PromoAdapter(result = it.results!!)
                binding.apply {
                    recyPromo.adapter = promoAdapter
                    (recyPromo.adapter as PromoAdapter).notifyDataSetChanged()
                }
            }
        })
    }

    private fun orderhistory() {
        if (!dialog.isShowing())
            dialog.create(requireContext())
        viewModel.responWallet.observe(viewLifecycleOwner, {
            if (it?.errorType != null) {

            }else{
                walletadapter = WalletHistoryAdapter(result = it.results!!)
                binding.apply {
                    recWallethis.adapter = walletadapter
                    (recWallethis.adapter as WalletHistoryAdapter).notifyDataSetChanged()
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun walletBalance() {

        if (!dialog.isShowing())
            dialog.create(requireContext())

        viewModel.wallet(customer = userid)
        binding.wallet=viewModel
        binding.lifecycleOwner=this
        viewModel.responWallet.observe(viewLifecycleOwner, {

            if (dialog.isShowing())
                dialog.dismiss()
            if (it?.errorType != null) {
               
            } else {
                binding.apply {
                    walletBalance=it.results!![0].totalBalance.toString().toDouble()
                    walletId= it.results[0].id ?:-1
                    txtTotalBalance.text =
                        "₹ " + String.format(
                            "%.2f",
                            it.results[0].totalBalance.toString().toDouble()
                        )
                    txtPromotionalBalance.text =
                        "₹ " + String.format(
                            "%.2f",
                            it.results[0].totalBalance.toString()
                                .toDouble() - it.results[0].rechargeBalance.toString().toDouble()
                        )

                }
            }
        })
    }

 /*   override fun onDetach() {
        super.onDetach()
        if (dialog != null && dialog.isShowing())
            dialog.dismiss()
    }*/
}