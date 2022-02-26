package com.eggoz.ecommerce.view.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.ViewModelFactory
import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentMyCalendarBinding
import com.eggoz.ecommerce.network.model.OrderModel
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.home.viewmodel.MyCalendarRepository
import com.eggoz.ecommerce.view.home.viewmodel.MyCalendarViewModel
import com.eggoz.ecommerce.view.profile.adapter.OrderAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class MyCalendarFragment : Fragment() {
    private var _binding: FragmentMyCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var orderadapter: OrderAdapter
    private lateinit var viewModel: MyCalendarViewModel

    private val eventshash: HashSet<EventDay> = HashSet()
    val event: ArrayList<EventDay> = ArrayList()
    private lateinit var sdf: SimpleDateFormat
    private lateinit var min: Calendar
    private lateinit var max: Calendar
    private lateinit var loadingdialog: Loadinddialog
    private lateinit var sdfl: SimpleDateFormat


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyCalendarBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        loadingdialog = Loadinddialog()
        loadingdialog.create(requireContext())

        val userPreferences = UserPreferences(requireContext())
        val repository = MyCalendarRepository(userPreferences)
        val viewmodelFat = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewmodelFat)[MyCalendarViewModel::class.java]

        sdf = SimpleDateFormat("dd/MM/yyyy")
        sdfl = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        min = Calendar.getInstance()
        max = Calendar.getInstance()



        min.add(Calendar.MONTH, -1)
        min.set(Calendar.DAY_OF_MONTH, min.getActualMinimum(Calendar.DATE))
        max.add(Calendar.MONTH, 1)
        max.set(Calendar.DAY_OF_MONTH, max.getActualMaximum(Calendar.DATE))

        binding.apply {
            orderadapter = OrderAdapter(callback = {order->
                val bundle = Bundle()
                bundle.putInt("id", order.orderid)
                Navigation.findNavController(calander)
                    .navigate(R.id.action_nav_my_calendar_to_nav_orderdetail, bundle)
            })
            viewoderAdapter = orderadapter

            calander.setMinimumDate(min)
            calander.setMaximumDate(max)
            calander.setOnDayClickListener(object : OnDayClickListener {
                override fun onDayClick(eventDay: EventDay) {
                    val clickedDayCalendar = eventDay.calendar
                    val bundle = Bundle()
                    bundle.putString("date", sdf.format(clickedDayCalendar.time))
                    Navigation.findNavController(binding.txtMyorders)
                        .navigate(R.id.action_nav_my_calendar_to_nav_date_detail, bundle)
                }
            })
        }
        getOrder()
    }

    private fun getOrder() {

        lifecycleScope.launch {
            viewModel.orderlist(
                sdf.format(min.time), sdf.format(max.time)
            ).observe(viewLifecycleOwner) {

                if (loadingdialog.isShowing())
                    loadingdialog.dismiss()
                it.results?.let { orderlist ->
                    viewModel.ordermodel.clear()
                    orderlist.let { inorderlist ->
                        for (order in inorderlist) {
                            order.deliveryDate?.let { ddate ->
                                val currDate: Date = sdfl.parse(ddate)
                                val cc: Calendar = Calendar.getInstance()
                                cc.time = currDate
                                eventshash.add(
                                    EventDay(
                                        cc,
                                        R.drawable.ic_baseline_fiber_manual_record_24
                                    )
                                )
                                if (order.secondaryStatus.equals("created")) {
                                }
                            }
                        }
                        try {
                            event.addAll(eventshash)
                            Log.d("TAG", "getOrder: ${eventshash.size} ${event.size}")
                            binding.calander.setEvents(event)
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }

                    }


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
                    if (viewModel.ordermodel.size>0) {
                        orderadapter.submitList(viewModel.ordermodel)
                        binding.txtMyorders.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}