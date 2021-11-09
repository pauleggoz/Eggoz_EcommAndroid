package com.eggoz.ecommerce.view.address

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashfree.pg.CFPaymentService
import com.eggoz.ecommerce.MainActivity
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentAddressBinding
import com.eggoz.ecommerce.room.RoomCart
import com.eggoz.ecommerce.utils.Constants
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.address.adapter.AddressAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddressFragment : Fragment() {

    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!

    private lateinit var addressAdapter: AddressAdapter
    private lateinit var viewModel: AddressViewModel
    private var mid = -1
    private var userPreferences: UserPreferences? = null
    private lateinit var dialog: Loadinddialog

    private var totalamount = 0.0
    private var ordertype = ""
    private var addressid = -1
    private lateinit var job: Job
    private lateinit var cartlist: ArrayList<RoomCart>

    private var item_id: Int = -1


    private var start_date = ""
    private var expiry_date = ""
    private var quantity = ""
    private var subitem = "-1"
    private var days: ArrayList<Int>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddressBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        cartlist = ArrayList()

        days = ArrayList()

        userPreferences = UserPreferences(requireContext())
        viewModel = ViewModelProvider(this).get(AddressViewModel::class.java)
        dialog = Loadinddialog()
        lifecycleScope.launch {
            mid = userPreferences!!.userid.first() ?: -1
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
            if (mid != -1)
                addressList()
        }
        binding.apply {
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

            recAddress.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                itemAnimator = DefaultItemAnimator()
                isNestedScrollingEnabled = false
            }
            txtAddAddress.setOnClickListener {
                Navigation.findNavController(root)
                    .navigate(R.id.action_nav_address_list_to_nav_manageAddress)
            }
        }
        getCartlist()

    }

    private fun getTokenforsubitem() {/*

        expiry_date = getArguments()?.getString("expiry_date", "") ?:""
        quantity = getArguments()?.getString("quantity", "0") ?:"0"
        subitem = getArguments()?.getString("product", "0") ?:"0"
        days = getArguments()?.getIntegerArrayList("product" ) as ArrayList<Int>

        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {
            viewModel.getTokenforsubitem(
                mid,
                requireContext(),
                totalamount,
                addressid,
                item_id,
                start_date,
                expiry_date,
                quantity,
                subitem,
                days,
                pay_by_wallet = false
            )
            viewModel.responTokenforsingle.observe(viewLifecycleOwner, {
                if (it.errorType == null) {

                    if (dialog.isShowing())
                        dialog.dismiss()
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
        }*/
    }

    private fun getTokenforsingle() {


        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val date = simpleDateFormat.format(Date())


        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {
            viewModel.getTokenforsingle(
                mid,
                requireContext(),
                totalamount,
                addressid,
                item_id,
                date,
                pay_by_wallet = false
            )
            viewModel.responTokenforsingle.observe(viewLifecycleOwner, {
                if (it.errorType == null) {

                    if (dialog.isShowing())
                        dialog.dismiss()
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
                mid,
                requireContext(),
                totalamount,
                addressid,
                cartlist,
                date,
                pay_by_wallet = false
            )
            viewModel.responCartToken.observe(viewLifecycleOwner, {
                if (it.errorType == null) {

                    if (dialog.isShowing())
                        dialog.dismiss()
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

        CFPaymentService.getCFPaymentServiceInstance()
            .doPayment(requireActivity(), parms, ctoken, Constants.cashFree_Server)

        try {

            (requireActivity() as MainActivity).paymentType=ordertype
            (requireActivity() as MainActivity).totalamount=totalamount
        }catch (e:Exception){
            Log.d("TAG", "paymentcart: ${e.message}")

        }

    }

    private fun addressList() {
        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {
            viewModel.userAddress(mid, requireContext())
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
                        addressAdapter = AddressAdapter(it.addresses)
                        binding.apply {
                            if (it.addresses != null)
                                addressid = it.addresses[0].id ?: -1
                            recAddress.adapter = addressAdapter
                            (recAddress.adapter as AddressAdapter).notifyDataSetChanged()
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