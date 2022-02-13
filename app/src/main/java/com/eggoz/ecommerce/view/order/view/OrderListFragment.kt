package com.eggoz.ecommerce.view.order.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentOrderListBinding
import com.eggoz.ecommerce.databinding.FragmentSubscribeBinding
import com.eggoz.ecommerce.network.model.OrderModel
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.order.adapter.OrderListAdapter
import com.eggoz.ecommerce.view.order.viewmodel.OrderListRepository
import com.eggoz.ecommerce.view.order.viewmodel.OrderListViewModel
import com.eggoz.ecommerce.view.order.viewmodel.OrderListViewModelFactory
import com.eggoz.ecommerce.view.profile.viewModel.ProfileRepository
import com.eggoz.ecommerce.view.profile.viewModel.ProfileViewModel
import com.eggoz.ecommerce.view.profile.viewModel.ProfileViewModelFactory
import kotlinx.coroutines.launch


class OrderListFragment : Fragment() {

    private var _binding: FragmentOrderListBinding? = null
    private val binding get() = _binding!!

    private lateinit var orderlistadapter: OrderListAdapter
    private lateinit var viewModel: OrderListViewModel
    private lateinit var dialog: Loadinddialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderListBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init(){

        dialog = Loadinddialog()
        orderlistadapter = OrderListAdapter()
        val repository = OrderListRepository(UserPreferences(requireContext()))
        val viewmodelFat = OrderListViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewmodelFat)[OrderListViewModel::class.java]

        binding.apply {
            viewoderAdapter =orderlistadapter
        }
        orderList()
    }

    private fun orderList(){

        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {
            viewModel.orderhistory(
                customer = 3
            ).observe(viewLifecycleOwner, {

                if (dialog.isShowing())
                    dialog.dismiss()

                if (it?.errorType != null) {
                    if (it.errors != null)
                        if (it.errors[0].message == "Signature has expired.")
                            Navigation.findNavController(binding.btnBack)
                                .navigate(R.id.action_nav_profile_to_nav_sigin1)

                } else {
                    if (it.results != null) {
                    }


                }
            })
        }
    }
}