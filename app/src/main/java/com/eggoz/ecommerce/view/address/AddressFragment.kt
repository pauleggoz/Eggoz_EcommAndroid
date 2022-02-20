package com.eggoz.ecommerce.view.address

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
//import com.cashfree.pg.CFPaymentService
import com.eggoz.ecommerce.MainActivity
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentAddressBinding
import com.eggoz.ecommerce.room.MyDatabase
import com.eggoz.ecommerce.room.RoomCart
import com.eggoz.ecommerce.utils.Constants
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.address.adapter.AddressAdapter
import com.eggoz.ecommerce.view.address.viewmodel.AddressRepository
import com.eggoz.ecommerce.view.address.viewmodel.AddressViewModel
import com.eggoz.ecommerce.view.address.viewmodel.AddressViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


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
                if (ordertype == "cart") {
                    getTokenforcart()
                } else if (ordertype == "single") {
                    getTokenforsingle()
                } else if (ordertype == "subitem") {
                    getTokenforsubitem()
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
            viewModel.responTokenforsingle.observe(viewLifecycleOwner, {
                // if (it.body()?.errorType == null) {

                if (dialog.isShowing())
                    dialog.dismiss()
                paymentcart(
                    it.body()?.txnid?:"",
                    it.body()?.token?:"",
                    it.body()?.surl?:"",
                    it.body()?.furl?:""
                )


            })
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
            viewModel.responTokenforsingle.observe(viewLifecycleOwner, {
                if (it.body()?.errorType == null) {

                    if (dialog.isShowing())
                        dialog.dismiss()
                    paymentcart(
                        it.body()?.txnid?:"",
                        it.body()?.token?:"",
                        it.body()?.surl?:"",
                        it.body()?.furl?:""
                    )
                }

            })
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
            viewModel.responCartToken.observe(viewLifecycleOwner, {
                if (it.body()?.errorType == null) {

                    if (dialog.isShowing())
                        dialog.dismiss()
                    paymentcart(
                        it.body()?.txnid?:"",
                        it.body()?.token?:"",
                        it.body()?.surl?:"",
                        it.body()?.furl?:""
                    )
                }

            })
        }
    }

    private fun paymentcart(
        txnid:String,
    token:String,
    surl:String,
    furl:String
    ) {/*
        val parms = HashMap<String, String>()
        parms.put("appId", appId)
        parms.put("orderId", orderId)
        parms.put("orderAmount", orderAmount.toString())
        parms.put("orderNote", orderNote)
        parms.put("customerName", customerName)
        parms.put("customerPhone", customerPhone)
        parms.put("customerEmail", customerEmail)
        parms.put("notifyUrl", notifyUrl)

        CFPaymentService.getCFPaymentServiceInstance()
            .doPayment(requireActivity(), parms, ctoken, Constants.cashFree_Server)

        try {

            (requireActivity() as MainActivity).paymentType = ordertype
            (requireActivity() as MainActivity).totalamount = totalamount
        } catch (e: Exception) {
            Log.d("TAG", "paymentcart: ${e.message}")

        }*/

    }

    private fun addressList() {
        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {
            viewModel.responAddress.observe(viewLifecycleOwner, {

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

            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        job.cancel()
    }
}