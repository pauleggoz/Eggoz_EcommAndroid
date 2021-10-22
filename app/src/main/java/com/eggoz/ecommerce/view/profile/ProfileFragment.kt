package com.eggoz.ecommerce.view.profile

import android.os.Bundle
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
import com.eggoz.ecommerce.databinding.FragmentProfileBinding
import com.eggoz.ecommerce.network.model.Orderhistory
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.profile.adapter.OrderAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var orderadapter: OrderAdapter
    private lateinit var dialog: Loadinddialog
    private var userPreferences: UserPreferences? = null
    private lateinit var viewModel: ProfileViewModel
    private var userid: Int = -1
    private val orderdate = ArrayList<String>()
    private val orderitem = ArrayList<Orderhistory.Result.OrderLines.OrderItem>()
    private val orderid = ArrayList<String>()
    private val orderstatus = ArrayList<String>()

    private var name = ""
    private var email = ""
    private var mobile = ""
    private var isverifide = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        userPreferences = UserPreferences(requireContext())

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        dialog = Loadinddialog()
        binding.lifecycleOwner=this
        binding.apply {
            layoutSubscribe.setOnClickListener {
                Navigation.findNavController(root)
                    .navigate(R.id.nav_subscribeinfro)
            }
            txtEditProfile.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("name", name)
                bundle.putString("email", email)
                bundle.putString("phoneNo", mobile)
                bundle.putBoolean("isverifide", isverifide)
                Navigation.findNavController(root)
                    .navigate(R.id.action_nav_profile_detail_to_nav_edt_profile, bundle)
            }
            recycleOrder.apply {
                layoutManager = LinearLayoutManager(
                    activity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                itemAnimator = DefaultItemAnimator()
                isNestedScrollingEnabled = false
            }
            layoutManageAddresses.setOnClickListener {
                Navigation.findNavController(root)
                    .navigate(R.id.action_nav_profile_detail_to_nav_manageAddress)
            }
            layoutMembership.setOnClickListener {
                Navigation.findNavController(root)
                    .navigate(R.id.nav_membership)
            }

        }
        lifecycleScope.launch {
            userid = userPreferences!!.Customer_id.first() ?: -1
            if (userid != -1) {
                getData()
            }
        }
    }

    private fun getData() {

        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {
            viewModel.orderhistory(
                customer = userid,
                context = requireContext()
            )
            viewModel.user(userid, requireContext())
        }
        viewModel.responOrderhistory.observe(viewLifecycleOwner, {

            if (dialog.isShowing())
                dialog.dismiss()
            if (it?.errorType != null) {
                if (it.errors != null)
                    if (it.errors[0].message == "Signature has expired.")
                        Navigation.findNavController(binding.recycleOrder)
                            .navigate(R.id.action_nav_profile_to_nav_sigin1)

            } else {
                if (it.results != null) {
                    for (i in it.results.indices) {
                        if (it.results[i].orderLines != null) {
                            if (it.results[i].orderLines?.orderItems != null)
                                for (j in it.results[i].orderLines?.orderItems?.indices!!) {
                                    orderitem.add(it.results[i].orderLines?.orderItems!![j])
                                    orderdate.add(it.results[i].generationDate ?: "")
                                    orderid.add(it.results[i].orderId ?: "")
                                    orderstatus.add(it.results[i].status ?: "")
                                }
                        }
                    }
                    if (it.results.isNotEmpty())
                        binding.layoutPastOrders.visibility = View.VISIBLE

                }

                orderadapter = OrderAdapter(
                    date = orderdate,
                    orderitem = orderitem,
                    orderid = orderid,
                    orderstatus = orderstatus
                )
                binding.apply {
                    recycleOrder.adapter = orderadapter
                    (recycleOrder.adapter as OrderAdapter).notifyDataSetChanged()
                }
            }
        })

        binding.personDetail=viewModel

        viewModel.responUser.observe(viewLifecycleOwner, {
            if (it?.errorType != null) {
                if (it.errors != null)
                    if (it.errors!![0].message == "Invalid signature.")
                        Navigation.findNavController(binding.recycleOrder)
                            .navigate(R.id.action_nav_profile_to_nav_sigin1)

            } else {

                binding.apply {
                    name = it.name ?: ""
                    email = it.email ?: ""
                    mobile = it.phoneNo ?: ""
                    isverifide = it.isProfileVerified ?: false

                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        _binding = null
    }
}