package com.eggoz.ecommerce.view.MembershipPlans

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashfree.pg.CFPaymentService
import com.cashfree.pg.ui.web_checkout.CFPaymentActivity
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentMembershipPlansBinding
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.utils.NetworkConnect
import com.eggoz.ecommerce.view.MembershipPlans.adapter.MembershipAdapter
import com.eggoz.ecommerce.view.MembershipPlans.callback.MembershipCallback
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

class MembershipPlansFragment : Fragment(), MembershipCallback {


    private var _binding: FragmentMembershipPlansBinding? = null
    private val binding get() = _binding!!


    private lateinit var network: NetworkConnect
    private lateinit var viewModel: MembershipModel
    private lateinit var dialog: Loadinddialog
    private lateinit var adapter: MembershipAdapter


    var year: Int = -1
    var month: Int = -1
    var day = -1
    var mont: Int = -1

    var endyear:Int=-1
    var endday:Int=-1
    var endmonths:Int=-1

    var startdate = ""
    var enddate = ""
    private var user_id = -1
    private var userPreferences: UserPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMembershipPlansBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        network = NetworkConnect(requireContext())


        viewModel = ViewModelProvider(this).get(MembershipModel::class.java)
        dialog = Loadinddialog()
        userPreferences = UserPreferences(requireContext())

        binding.apply {
            lifecycleScope.launch {
                user_id = userPreferences!!.Customer_id.first() ?: -1

            }

            val c = Calendar.getInstance()
            year = c[Calendar.YEAR]
            month = c[Calendar.MONTH]
            day = c[Calendar.DAY_OF_MONTH]
            mont = month + 1

            if (mont < 10) startdate = "$year-0$mont-$day 00:00:00"
            else startdate =
                "$year-$mont-$day 00:00:00"


            endday=day
            endmonths=mont+3
            if (endmonths>12)
                endyear=year+1
            else
                endyear=year

            if (endmonths < 10) enddate = "$endyear-0$endmonths-$endday 00:00:00"
            else enddate ="$endyear-$endmonths-$endday 00:00:00"


            recMember.apply {
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(requireContext())
                itemAnimator = DefaultItemAnimator()
                isNestedScrollingEnabled = false

            }
            txt3months.setOnClickListener {
                endday=day
                endmonths=mont+3
                if (endmonths>12)
                    endyear=year+1
                else
                    endyear=year

                if (endmonths < 10) enddate = "$endyear-0$endmonths-$endday 00:00:00"
                else enddate ="$endyear-$endmonths-$endday 00:00:00"




                adapter != null
                adapter.setmMonths(3)
                txt3months.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.curvedbacksmall)
                txt6months.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.cartitemsmallborder)
            }
            txt6months.setOnClickListener {
                endday=day
                endmonths=mont+6
                if (endmonths>12)
                    endyear=year+1
                else
                    endyear=year

                if (endmonths < 10) enddate = "$endyear-0$endmonths-$endday 00:00:00"
                else enddate ="$endyear-$endmonths-$endday 00:00:00"


                adapter != null
                adapter.setmMonths(6)
                txt6months.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.curvedbacksmall)
                txt3months.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.cartitemsmallborder)
            }


        }
        binding.btnBack.setOnClickListener {
            Navigation.findNavController(binding.root)
                .popBackStack()
        }

        network.observe(viewLifecycleOwner, {
            if (it) {
                binding.apply {
                    layoutData.visibility = View.VISIBLE

                    layoutNoNetwork.visibility = View.GONE
                }

            } else {
                binding.apply {
                    layoutData.visibility = View.GONE

                    layoutNoNetwork.visibility = View.VISIBLE
                }
            }
        })

        viewModel.responsemembershiprecharge.observe(viewLifecycleOwner, {

            if (dialog.isShowing())
                dialog.dismiss()

            payment()
        })

        getMembership()

    }

    private fun getMembership() {

        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {
            viewModel.getMembership(requireContext())
        }
        viewModel.responsemembership.observe(viewLifecycleOwner, {

            if (dialog.isShowing())
                dialog.dismiss()
            if (it?.errorType != null) {
                if (it.errors != null)
                    if (it.errors!![0].message == "Signature has expired.")
                        Navigation.findNavController(binding.recMember)
                            .navigate(R.id.action_nav_profile_to_nav_sigin1)

            } else {

                if (it?.results?.isNotEmpty() == true) {
                    adapter = MembershipAdapter(it.results, this@MembershipPlansFragment, 3)
                    binding.recMember.adapter =
                        adapter
                    (binding.recMember.adapter as MembershipAdapter).notifyDataSetChanged()
                }


            }
        })

    }

    private fun payment(){
        val parms=HashMap<String,String>()
        parms.put("appId","appId")
        parms.put("orderId","Ads123")
//        parms.put("orderCurrency","appId")
        parms.put("orderAmount","2300")
        parms.put("orderNote","appId")
        parms.put("customerName","appId")
        parms.put("customerPhone","8273217889")
        parms.put("customerEmail","asdk@asd.com")
        parms.put("notifyUrl","appId")
        val ptoken="dasdadlasdla;s"

        CFPaymentService.getCFPaymentServiceInstance().doPayment(requireActivity(),parms,ptoken,"TEST")
    }

    override fun selectmambership(id: Int,price:Double) {

        if (!dialog.isShowing())
            dialog.create(requireContext())
        viewModel.membershiprecharge(
            start_date = startdate,
            expiry_date = enddate,
            customer = user_id,
            amount = price,
            wallet = 1.toString(),
            membership = id.toString(),
            pay_by_wallet = false,
            context = requireContext()
        )

    }

}