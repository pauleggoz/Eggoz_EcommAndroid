package com.eggoz.ecommerce.view.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.ViewModelFactory
import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentProfileBinding
import com.eggoz.ecommerce.network.model.OrderModel
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.profile.adapter.OrderAdapter
import com.eggoz.ecommerce.view.profile.viewModel.ProfileRepository
import com.eggoz.ecommerce.view.profile.viewModel.ProfileViewModel
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var orderadapter: OrderAdapter
    private lateinit var dialog: Loadinddialog
    private lateinit var viewModel: ProfileViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        val repository = ProfileRepository(UserPreferences(requireContext()))
        val viewmodelFat = ViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewmodelFat)[ProfileViewModel::class.java]


        dialog = Loadinddialog()
        binding.lifecycleOwner = this
        binding.apply {
            orderadapter = OrderAdapter(callback = { order ->
                val bundle = Bundle()
                bundle.putInt("id", order.orderid)
                Navigation.findNavController(root)
                    .navigate(R.id.action_nav_profile_to_nav_orderdetail, bundle)
            })
            viewoderAdapter = orderadapter

            txtViewAll.setOnClickListener {
                Navigation.findNavController(root)
                    .navigate(R.id.action_nav_profile_to_nav_orderlist)
            }

            layoutSubscribe.setOnClickListener {
                Navigation.findNavController(root)
                    .navigate(R.id.nav_subscribeinfro)
            }
            txtEditProfile.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("name", viewModel.name)
                bundle.putString("email", viewModel.email)
                bundle.putString("phoneNo", viewModel.mobile)
                bundle.putBoolean("isverifide", viewModel.isverifide)
                Navigation.findNavController(root)
                    .navigate(R.id.action_nav_profile_detail_to_nav_edt_profile, bundle)
            }
            layoutManageAddresses.setOnClickListener {
                Navigation.findNavController(root)
                    .navigate(R.id.action_nav_profile_detail_to_nav_manageAddress)
            }
            layoutMembership.setOnClickListener {
                Navigation.findNavController(root)
                    .navigate(R.id.nav_membership)
            }
            layoutRegion.setOnClickListener {
                Navigation.findNavController(root)
                    .navigate(R.id.action_nav_profile_to_nav_city)
            }

        }
        getData()
    }

    private fun getData() {

        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {

            viewModel.order.observe(viewLifecycleOwner) {

                if (dialog.isShowing())
                    dialog.dismiss()

                if (it?.errorType != null) {
                    if (it.errors != null)
                        if (it.errors[0].message == "Signature has expired.")
                            Navigation.findNavController(binding.recycleOrder)
                                .navigate(R.id.action_nav_profile_to_nav_sigin1)

                } else {
                    if (it.results != null) {
                        viewModel.ordermodel.clear()
                        for (i in it.results.indices) {
                            if (it.results[i].orderLines != null) {
                                if (it.results[i].orderLines?.orderItems != null)
                                    for (j in it.results[i].orderLines?.orderItems?.indices!!) {
                                        val order = OrderModel(
                                            it.results[i].orderLines?.orderItems!![j],
                                            it.results[i].generationDate ?: "",
                                            it.results[i].id ?: -1,
                                            it.results[i].status ?: ""
                                        )
                                        viewModel.ordermodel.add(order)
                                    }
                            }
                        }
                        if (viewModel.ordermodel.size > 0) {
                            binding.layoutPastOrders.visibility = View.VISIBLE
                            orderadapter.submitList(viewModel.ordermodel)
                        }
                    }


                }
            }

            viewModel.user.observe(viewLifecycleOwner) {
                if (it?.errorType != null) {
                    if (it.errors != null)
                        if (it.errors!![0].message == "Invalid signature.")
                            Navigation.findNavController(binding.recycleOrder)
                                .navigate(R.id.action_nav_profile_to_nav_sigin1)

                } else {
                    binding.apply {
                        personDetail = it
                        viewModel.name = it.name ?: ""
                        viewModel.email = it.email ?: ""
                        viewModel.mobile = it.phoneNo ?: ""
                        viewModel.isverifide = it.isProfileVerified ?: false

                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        _binding = null
    }
}