package com.eggoz.ecommerce.view.wallet.view

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
import com.eggoz.ecommerce.view.home.viewmodel.HomeRepository
import com.eggoz.ecommerce.view.home.viewmodel.HomeViewModel
import com.eggoz.ecommerce.view.home.viewmodel.HomeViewModelFactory
import com.eggoz.ecommerce.view.wallet.viewmodel.WalletViewModel
import com.eggoz.ecommerce.view.wallet.adapter.PromoAdapter
import com.eggoz.ecommerce.view.wallet.adapter.WalletHistoryAdapter
import com.eggoz.ecommerce.view.wallet.viewmodel.WalletRepository
import com.eggoz.ecommerce.view.wallet.viewmodel.WalletViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class WalletFragment : Fragment() {

    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: WalletViewModel
    private lateinit var walletadapter: WalletHistoryAdapter
    private lateinit var promoAdapter: PromoAdapter

    private lateinit var dialog: Loadinddialog

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
        val userPreferences = UserPreferences(requireContext())
        val repository = WalletRepository(userPreferences)
        val viewmodelFat = WalletViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewmodelFat)[WalletViewModel::class.java]
        walletadapter = WalletHistoryAdapter()
        promoAdapter = PromoAdapter()

        walletBalance()
        orderhistory()
        Promo()

        binding.apply {
            binding.lifecycleOwner = this@WalletFragment
            walletviewModel = viewModel
            viewwalletadapter=walletadapter
            viewpromoAdapter=promoAdapter

            txtADDMONEY.setOnClickListener {

                val bundle = Bundle()
                bundle.putDouble("bal", viewModel.walletBalanceval)
                bundle.putInt("walletid", viewModel.walletId)
                Navigation.findNavController(root)
                    .navigate(R.id.action_nav_wallet_to_nav_addmoney, bundle)
            }
          /*  recWallethis.apply {
                layoutManager = LinearLayoutManager(requireContext())
                itemAnimator = DefaultItemAnimator()
                isNestedScrollingEnabled = false
            }*/

        }

    }

    private fun Promo() {

        if (!dialog.isShowing())
            dialog.create(requireContext())
        viewModel.walletPromo()
        viewModel.responWalletPromo.observe(viewLifecycleOwner, {

            if (dialog.isShowing())
                dialog.dismiss()
            if (it?.errorType == null){
                promoAdapter.submitList(it.results)
            }
        })
    }

    private fun orderhistory() {
        if (!dialog.isShowing())
            dialog.create(requireContext())
        viewModel.responWallet.observe(viewLifecycleOwner, {
            if (it?.errorType == null) {
                walletadapter.submitList(it.results)
               /* walletadapter = WalletHistoryAdapter(result = it.results!!)
                binding.apply {
                    recWallethis.adapter = walletadapter
                    (recWallethis.adapter as WalletHistoryAdapter).notifyDataSetChanged()
                }*/
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun walletBalance() {

        if (!dialog.isShowing())
            dialog.create(requireContext())

        viewModel.wallet()
        /*viewModel.responWallet.observe(viewLifecycleOwner, {

            if (dialog.isShowing())
                dialog.dismiss()
            if (it?.errorType != null) {

            } else {
                binding.apply {
                    viewModel.wallet_Balanceval = it.results!![0].totalBalance.toString().toDouble()
                    viewModel.walletId = it.results[0].id ?: -1
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
        })*/
    }
}