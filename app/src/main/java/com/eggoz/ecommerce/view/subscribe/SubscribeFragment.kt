package com.eggoz.ecommerce.view.subscribe

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentSubscribeBinding
import com.eggoz.ecommerce.network.model.Products
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.utils.NetworkConnect
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SubscribeFragment : Fragment(), OnDateSetListener {

    private var _binding: FragmentSubscribeBinding? = null
    private val binding get() = _binding!!

    private lateinit var network: NetworkConnect

    private lateinit var viewModel: SubscribeViewModel

    private var schduleTime = ""
    private var days: ArrayList<Int> = ArrayList()
    private lateinit var dialog: Loadinddialog
    private var user_id = -1
    private var userPreferences: UserPreferences? = null

    val bundle = Bundle()
    private lateinit var min: Calendar


    lateinit var startdatePickerDialog: DatePickerDialog
    lateinit var enddatePickerDialog: DatePickerDialog


    var year: Int = -1
    var month: Int = -1
    var day = -1
    var mont: Int = -1
    var box: Int = 0
    val event: ArrayList<EventDay> = ArrayList()
    private val eventshash: HashSet<EventDay> = HashSet()
    val dates = ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscribeBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        viewModel = ViewModelProvider(this)[SubscribeViewModel::class.java]
        network = NetworkConnect(requireContext())
        dialog = Loadinddialog()
        userPreferences = UserPreferences(requireContext())

        viewModel.item_id = this.arguments?.getInt("id", -1) ?: -1
        viewModel.price = this.arguments?.getDouble("price", 0.0) ?: 0.0

        get2Day()
        binding.apply {

            min = Calendar.getInstance()
            min.add(Calendar.MONTH, -1)
            min.set(Calendar.DAY_OF_MONTH, min.getActualMinimum(Calendar.DATE))
            calander.setMinimumDate(min)
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            calander.setOnDayClickListener(object : OnDayClickListener {
                override fun onDayClick(eventDay: EventDay) {
                    val clickedDayCalendar = eventDay.calendar
                    if (dates.contains(sdf.format(clickedDayCalendar.time)))
                        dates.remove(sdf.format(clickedDayCalendar.time))
                    else
                        dates.add(sdf.format(clickedDayCalendar.time))

                    val currDate: Date = sdf.parse(sdf.format(clickedDayCalendar.time))
                    val cc: Calendar = Calendar.getInstance()
                    cc.time = currDate

                    val eve = EventDay(
                        cc,
                        R.drawable.ic_baseline_fiber_manual_record_24
                    )

                    if (eventshash.contains(eve)) {
                        eventshash.remove(eve)
                        event.remove(eve)
                    } else {
                        eventshash.add(eve)
                        event.add(eve)

                    }
                    binding.calander.setEvents(event)
                }
            })

            val c = Calendar.getInstance()
            year = c[Calendar.YEAR]
            month = c[Calendar.MONTH]
            day = c[Calendar.DAY_OF_MONTH]
            mont = month + 1

            slideBox.setValues(1f)


            binding.edtStartDate.keyListener = null
            binding.edtEndDate.keyListener = null

            enddatePickerDialog = DatePickerDialog(
                requireContext(), this@SubscribeFragment, year, mont, day
            )
            startdatePickerDialog = DatePickerDialog(
                requireContext(), this@SubscribeFragment, year, mont, day
            )


            if (mont < 10) edtStartDate.setText("$year-0$mont-$day")
            else edtStartDate.setText(
                "$year-$mont-$day"
            )


            txt1.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.current_active_dot)
            txt2.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.non_active_dot_border)
            txt3.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.non_active_dot_border)
            txt4.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.non_active_dot_border)
            btnBack.setOnClickListener {
                Navigation.findNavController(binding.root)
                    .popBackStack()
            }
        }
        network.observe(viewLifecycleOwner) {
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
        }


        binding.apply {


            slideBox.addOnChangeListener { slider, value, fromUser ->
                box = value.toInt()
                laySlot.visibility = View.VISIBLE

                if (!laySlot.isVisible) {
                    txt1.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.active_dot)
                    txt2.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.current_active_dot)
                }
            }
            radioSlot.setOnCheckedChangeListener { radioGroup, i ->
                when {
                    loc8am.isChecked -> {
                        schduleTime = "1"
                    }
                    loc11am.isChecked -> {
                        schduleTime = "2"
                    }
                    loc2pm.isChecked -> {
                        schduleTime = "3"
                    }
                }
                layFrequency.visibility = View.VISIBLE
                if (!layFrequency.isVisible) {
                    txt2.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.active_dot)
                    txt3.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.current_active_dot)
                }
            }
            radioFrequency.setOnCheckedChangeListener { radioGroup, i ->
                when {
                    locOneweekly.isChecked -> {
                        days.clear()
                        clearDays()
                        radioDays.visibility = View.VISIBLE
                        layoutCheckdayys.visibility = View.GONE
                        layoutCheck2dayys.visibility = View.GONE
                    }
                    locOne2weekly.isChecked -> {
                        days.clear()
                        clearDays()
                        radioDays.visibility = View.GONE
                        layoutCheckdayys.visibility = View.GONE
                        layoutCheck2dayys.visibility = View.VISIBLE
                    }
                    locCustom.isChecked -> {
                        days.clear()
                        clearDays()
                        radioDays.visibility = View.GONE
                        layoutCheckdayys.visibility = View.VISIBLE
                        layoutCheck2dayys.visibility = View.GONE
                    }
                }
                layDays.visibility = View.VISIBLE
            }

            radioDays.setOnCheckedChangeListener { radioGroup, i ->
                layPlan.visibility = View.VISIBLE
                when {
                    locSunday.isChecked -> {
                        days.clear()
                        days.add(0)
                    }
                    locMonday.isChecked -> {
                        days.clear()
                        days.add(1)
                    }
                    locTuesday.isChecked -> {
                        days.clear()
                        days.add(2)
                    }
                    locWednesday.isChecked -> {
                        days.clear()
                        days.add(3)
                    }
                    locThursday.isChecked -> {
                        days.clear()
                        days.add(4)
                    }
                    locFriday.isChecked -> {
                        days.clear()
                        days.add(5)
                    }
                    locSaturday.isChecked -> {
                        days.clear()
                        days.add(6)
                    }

                }
                layFrequency.visibility = View.VISIBLE
                if (!layFrequency.isVisible) {
                    txt2.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.active_dot)
                    txt3.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.current_active_dot)
                }
                if (binding.loc4weeks.isChecked || binding.loc8weeks.isChecked)
                    getDate()
            }

            checMonday.setOnCheckedChangeListener { compoundButton, b ->
                if (days.contains(1) && !b)
                    days.remove(1)
                else if (!days.contains(1) && b)
                    days.add(1)
                if (!layPlan.isVisible)
                    layPlan.visibility = View.VISIBLE

                if (loc4weeks.isChecked||loc8weeks.isChecked)
                    getDate()
            }
            checTuesday.setOnCheckedChangeListener { compoundButton, b ->
                if (days.contains(2) && !b)
                    days.remove(2)
                else if (!days.contains(2) && b)
                    days.add(2)
                if (!layPlan.isVisible)
                    layPlan.visibility = View.VISIBLE

                if (loc4weeks.isChecked||loc8weeks.isChecked)
                    getDate()
            }
            checWednesday.setOnCheckedChangeListener { compoundButton, b ->
                if (days.contains(3) && !b)
                    days.remove(3)
                else if (!days.contains(3) && b)
                    days.add(3)
                if (!layPlan.isVisible)
                    layPlan.visibility = View.VISIBLE

                if (loc4weeks.isChecked||loc8weeks.isChecked)
                    getDate()
            }
            checThursday.setOnCheckedChangeListener { compoundButton, b ->
                if (days.contains(4) && !b)
                    days.remove(4)
                else if (!days.contains(4) && b)
                    days.add(4)
                if (!layPlan.isVisible)
                    layPlan.visibility = View.VISIBLE

                if (loc4weeks.isChecked||loc8weeks.isChecked)
                    getDate()
            }
            checFriday.setOnCheckedChangeListener { compoundButton, b ->
                if (days.contains(5) && !b)
                    days.remove(5)
                else if (!days.contains(5) && b)
                    days.add(5)
                if (!layPlan.isVisible)
                    layPlan.visibility = View.VISIBLE

                if (loc4weeks.isChecked||loc8weeks.isChecked)
                    getDate()
            }
            checSaturday.setOnCheckedChangeListener { compoundButton, b ->
                if (days.contains(6) && !b)
                    days.remove(6)
                else if (!days.contains(6) && b)
                    days.add(6)
                if (!layPlan.isVisible)
                    layPlan.visibility = View.VISIBLE

                if (loc4weeks.isChecked||loc8weeks.isChecked)
                    getDate()
            }
            checSunday.setOnCheckedChangeListener { compoundButton, b ->
                if (days.contains(7) && !b)
                    days.remove(7)
                else if (!days.contains(7) && b)
                    days.add(7)
                if (!layPlan.isVisible)
                    layPlan.visibility = View.VISIBLE

                if (loc4weeks.isChecked||loc8weeks.isChecked)
                    getDate()
            }

            edtStartDate.setOnClickListener {
                startDataOn()
            }
            radioPlan.setOnCheckedChangeListener { radioGroup, i ->
                when {
                    loc4weeks.isChecked -> {
                        val sdf = SimpleDateFormat("yyyy-MM-dd")
                        val cal = Calendar.getInstance()
                        var skipdays = 4 * 7
                        cal.add(Calendar.DAY_OF_YEAR, skipdays)
                        val enddate: Date = cal.time

                        binding.edtEndDate.setText(sdf.format(enddate))

                    }
                    loc8weeks.isChecked -> {
                        val sdf = SimpleDateFormat("yyyy-MM-dd")
                        val cal = Calendar.getInstance()
                        val skipdays = 8 * 7
                        cal.add(Calendar.DAY_OF_YEAR, skipdays)
                        val enddate: Date = cal.time

                        binding.edtEndDate.setText(sdf.format(enddate))

                    }
                }
                layDate.visibility = View.VISIBLE
                getDate()
                if (!layDate.isVisible) {
                    txt3.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.active_dot)
                    txt4.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.current_active_dot)
                }
            }

            lifecycleScope.launch {
                user_id = userPreferences!!.Customer_id.first() ?: -1
            }
            btnSubmit.setOnClickListener {
                if (box > 0) {
                    getDate()
                    if (!dialog.isShowing())
                        dialog.create(requireContext())
                    viewModel.subscriptions(
                        start_date = binding.edtStartDate.text.toString(),
                        expiry_date = binding.edtEndDate.text.toString(),
                        customer = user_id,
                        quantity = box,
                        product = viewModel.item_id,
                        slot = schduleTime,
                        days = days,
                        context = requireContext()
                    )
                } else Toast.makeText(requireContext(), "Select box", Toast.LENGTH_SHORT).show()
            }

            viewModel.responsemembershiprecharge.observe(viewLifecycleOwner) {
                if (dialog.isShowing())
                    dialog.dismiss()
                Navigation.findNavController(root)
                    .navigate(R.id.action_nav_subscribe_to_nav_address_list, bundle)
            }
        }


    }


    private fun get2Day() {
        binding.apply {
            chec2Monday.setOnCheckedChangeListener { compoundButton, b ->
                if (days.contains(1) && !b)
                    days.remove(1)
                else if (!days.contains(1) && b && days.size < 2)
                    days.add(1)
                else {
                    Toast.makeText(requireContext(), "Select 2 Days", Toast.LENGTH_SHORT).show()
                    chec2Monday.isChecked = false
                }
                if (!layPlan.isVisible && days.size == 2)
                    layPlan.visibility = View.VISIBLE
                if (loc4weeks.isChecked||loc8weeks.isChecked)
                    getDate()
            }
            chec2Tuesday.setOnCheckedChangeListener { compoundButton, b ->
                if (days.contains(2) && !b)
                    days.remove(2)
                else if (!days.contains(2) && b && days.size < 2)
                    days.add(2)
                else {
                    Toast.makeText(requireContext(), "Select 2 Days", Toast.LENGTH_SHORT).show()
                    chec2Tuesday.isChecked = false
                }
                if (!layPlan.isVisible && days.size == 2)
                    layPlan.visibility = View.VISIBLE
                if (loc4weeks.isChecked||loc8weeks.isChecked)
                    getDate()
            }
            chec2Wednesday.setOnCheckedChangeListener { compoundButton, b ->
                if (days.contains(3) && !b)
                    days.remove(3)
                else if (!days.contains(3) && b && days.size < 2)
                    days.add(3)
                else {
                    Toast.makeText(requireContext(), "Select 2 Days", Toast.LENGTH_SHORT).show()
                    chec2Wednesday.isChecked = false
                }
                if (!layPlan.isVisible && days.size == 2)
                    layPlan.visibility = View.VISIBLE
                if (loc4weeks.isChecked||loc8weeks.isChecked)
                    getDate()
            }
            chec2Thursday.setOnCheckedChangeListener { compoundButton, b ->
                if (days.contains(4) && !b)
                    days.remove(4)
                else if (!days.contains(4) && b && days.size < 2)
                    days.add(4)
                else {
                    Toast.makeText(requireContext(), "Select 2 Days", Toast.LENGTH_SHORT).show()
                    chec2Thursday.isChecked = false
                }
                if (!layPlan.isVisible && days.size == 2)
                    layPlan.visibility = View.VISIBLE
                if (loc4weeks.isChecked||loc8weeks.isChecked)
                    getDate()
            }
            chec2Friday.setOnCheckedChangeListener { compoundButton, b ->
                if (days.contains(5) && !b)
                    days.remove(5)
                else if (!days.contains(5) && b && days.size < 2)
                    days.add(5)
                else {
                    Toast.makeText(requireContext(), "Select 2 Days", Toast.LENGTH_SHORT).show()
                    chec2Friday.isChecked = false
                }
                if (!layPlan.isVisible && days.size == 2)
                    layPlan.visibility = View.VISIBLE
                if (loc4weeks.isChecked||loc8weeks.isChecked)
                    getDate()
            }
            chec2Saturday.setOnCheckedChangeListener { compoundButton, b ->
                if (days.contains(6) && !b)
                    days.remove(6)
                else if (!days.contains(6) && b && days.size < 2)
                    days.add(6)
                else {
                    Toast.makeText(requireContext(), "Select 2 Days", Toast.LENGTH_SHORT).show()
                    chec2Saturday.isChecked = false
                }
                if (!layPlan.isVisible && days.size == 2)
                    layPlan.visibility = View.VISIBLE
                if (loc4weeks.isChecked||loc8weeks.isChecked)
                    getDate()
            }
            chec2Sunday.setOnCheckedChangeListener { compoundButton, b ->
                if (days.contains(7) && !b)
                    days.remove(7)
                else if (!days.contains(7) && b && days.size < 2)
                    days.add(7)
                else {
                    Toast.makeText(requireContext(), "Select 2 Days", Toast.LENGTH_SHORT).show()
                    chec2Sunday.isChecked = false
                }
                if (!layPlan.isVisible && days.size == 2)
                    layPlan.visibility = View.VISIBLE
                if (loc4weeks.isChecked||loc8weeks.isChecked)
                    getDate()
            }

        }
    }

    private fun getDate() {
        var no_days = 0
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val cal = Calendar.getInstance()
        cal.time = sdf.parse(binding.edtStartDate.text.toString())
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        dates.clear()
        binding.apply {
            when {
                loc4weeks.isChecked -> {
                    no_days = 4
                    viewModel.subId = 1
                }
                loc8weeks.isChecked -> {
                    no_days = 8

                    viewModel.subId = 2
                }


//                locPowerpack.isChecked -> no_days = 4
            }
            when {
                locOneweekly.isChecked -> {
                    var skipDays = 0
                    when {
                        locSunday.isChecked -> {
                            skipDays = if (dayOfWeek > 1)
                                7 - dayOfWeek + 1
                            else
                                1 - dayOfWeek
                            if (skipDays == 0) skipDays = 7
                            cal.add(Calendar.DAY_OF_YEAR, skipDays)
                        }
                        locMonday.isChecked -> {
                            skipDays = if (dayOfWeek > 2)
                                7 - dayOfWeek + 2
                            else
                                2 - dayOfWeek
                            if (skipDays == 0) skipDays = 7
                            cal.add(Calendar.DAY_OF_YEAR, skipDays)
                        }
                        locTuesday.isChecked -> {
                            if (dayOfWeek > 3)
                                skipDays = 7 - dayOfWeek + 3
                            else
                                3 - dayOfWeek
                            if (skipDays == 0) skipDays = 7
                            cal.add(Calendar.DAY_OF_YEAR, skipDays)
                        }
                        locWednesday.isChecked -> {
                            skipDays = if (dayOfWeek > 4)
                                7 - dayOfWeek + 4
                            else 4 - dayOfWeek
                            if (skipDays == 0) skipDays = 7
                            cal.add(Calendar.DAY_OF_YEAR, skipDays)
                        }
                        locTuesday.isChecked -> {
                            skipDays = if (dayOfWeek > 5)
                                7 - dayOfWeek + 5
                            else
                                5 - dayOfWeek
                            if (skipDays == 0) skipDays = 7
                            cal.add(Calendar.DAY_OF_YEAR, skipDays)
                        }
                        locFriday.isChecked -> {
                            skipDays = if (dayOfWeek > 6)
                                7 - dayOfWeek + 6
                            else 6 - dayOfWeek
                            if (skipDays == 0) skipDays = 7
                            cal.add(Calendar.DAY_OF_YEAR, skipDays)
                        }
                        locSaturday.isChecked -> {
                            skipDays = if (dayOfWeek > 7)
                                7 - dayOfWeek + 7
                            else 7 - dayOfWeek
                            if (skipDays == 0) skipDays = 7
                            cal.add(Calendar.DAY_OF_YEAR, skipDays)
                        }
                    }
                    dates.add(sdf.format(cal.time))
                    for (i in 0 until no_days - 1) {
                        cal.add(Calendar.DAY_OF_YEAR, 7)
                        dates.add(sdf.format(cal.time))
                    }
                    Log.d("Datelist", "getDate: ${dates.toString()}")

                }
                locOne2weekly.isChecked -> {
                    var skipDays = 0


                    if (chec2Sunday.isChecked) {
                        cal.time = sdf.parse(binding.edtStartDate.text.toString())
                        skipDays = if (dayOfWeek > 1)
                            7 - dayOfWeek + 1
                        else
                            1 - dayOfWeek
                        if (skipDays == 0) skipDays = 7
                        cal.add(Calendar.DAY_OF_YEAR, skipDays)
                        dates.add(sdf.format(cal.time))
                        for (i in 0 until no_days - 1) {
                            cal.add(Calendar.DAY_OF_YEAR, 7)
                            dates.add(sdf.format(cal.time))
                        }
                    }
                    if (chec2Monday.isChecked) {
                        cal.time = sdf.parse(binding.edtStartDate.text.toString())
                        skipDays = if (dayOfWeek > 2)
                            7 - dayOfWeek + 2
                        else
                            2 - dayOfWeek
                        if (skipDays == 0) skipDays = 7
                        cal.add(Calendar.DAY_OF_YEAR, skipDays)

                        dates.add(sdf.format(cal.time))
                        for (i in 0 until no_days - 1) {
                            cal.add(Calendar.DAY_OF_YEAR, 7)
                            dates.add(sdf.format(cal.time))
                        }
                    }
                    if (chec2Tuesday.isChecked) {
                        cal.time = sdf.parse(binding.edtStartDate.text.toString())
                        if (dayOfWeek > 3)
                            skipDays = 7 - dayOfWeek + 3
                        else
                            3 - dayOfWeek
                        if (skipDays == 0) skipDays = 7
                        cal.add(Calendar.DAY_OF_YEAR, skipDays)

                        dates.add(sdf.format(cal.time))
                        for (i in 0 until no_days - 1) {
                            cal.add(Calendar.DAY_OF_YEAR, 7)
                            dates.add(sdf.format(cal.time))
                        }
                    }
                    if (chec2Wednesday.isChecked) {
                        cal.time = sdf.parse(binding.edtStartDate.text.toString())
                        skipDays = if (dayOfWeek > 4)
                            7 - dayOfWeek + 4
                        else 4 - dayOfWeek
                        if (skipDays == 0) skipDays = 7
                        cal.add(Calendar.DAY_OF_YEAR, skipDays)

                        dates.add(sdf.format(cal.time))
                        for (i in 0 until no_days - 1) {
                            cal.add(Calendar.DAY_OF_YEAR, 7)
                            dates.add(sdf.format(cal.time))
                        }
                    }
                    if (chec2Tuesday.isChecked) {
                        cal.time = sdf.parse(binding.edtStartDate.text.toString())
                        skipDays = if (dayOfWeek > 5)
                            7 - dayOfWeek + 5
                        else
                            5 - dayOfWeek
                        if (skipDays == 0) skipDays = 7
                        cal.add(Calendar.DAY_OF_YEAR, skipDays)

                        dates.add(sdf.format(cal.time))
                        for (i in 0 until no_days - 1) {
                            cal.add(Calendar.DAY_OF_YEAR, 7)
                            dates.add(sdf.format(cal.time))
                        }
                    }
                    if (chec2Friday.isChecked) {
                        cal.time = sdf.parse(binding.edtStartDate.text.toString())
                        skipDays = if (dayOfWeek > 6)
                            7 - dayOfWeek + 6
                        else 6 - dayOfWeek
                        if (skipDays == 0) skipDays = 7
                        cal.add(Calendar.DAY_OF_YEAR, skipDays)

                        dates.add(sdf.format(cal.time))
                        for (i in 0 until no_days - 1) {
                            cal.add(Calendar.DAY_OF_YEAR, 7)
                            dates.add(sdf.format(cal.time))
                        }
                    }
                    if (chec2Saturday.isChecked) {
                        cal.time = sdf.parse(binding.edtStartDate.text.toString())
                        skipDays = if (dayOfWeek > 7)
                            7 - dayOfWeek + 7
                        else 7 - dayOfWeek
                        if (skipDays == 0) skipDays = 7
                        cal.add(Calendar.DAY_OF_YEAR, skipDays)

                        dates.add(sdf.format(cal.time))
                        for (i in 0 until no_days - 1) {
                            cal.add(Calendar.DAY_OF_YEAR, 7)
                            dates.add(sdf.format(cal.time))
                        }
                    }

                    Log.d("Datelist", "getDate: ${dates.toString()}")


                }
                locCustom.isChecked -> {
                    var skipDays = 0
                    if (checSunday.isChecked) {
                        cal.time = sdf.parse(binding.edtStartDate.text.toString())
                        skipDays = if (dayOfWeek > 1)
                            7 - dayOfWeek + 1
                        else
                            1 - dayOfWeek
                        if (skipDays == 0) skipDays = 7
                        cal.add(Calendar.DAY_OF_YEAR, skipDays)
                        dates.add(sdf.format(cal.time))
                        for (i in 0 until no_days - 1) {
                            cal.add(Calendar.DAY_OF_YEAR, 7)
                            dates.add(sdf.format(cal.time))
                        }

                    }
                    if (checMonday.isChecked) {
                        cal.time = sdf.parse(binding.edtStartDate.text.toString())
                        skipDays = if (dayOfWeek > 2)
                            7 - dayOfWeek + 2
                        else
                            2 - dayOfWeek
                        if (skipDays == 0) skipDays = 7
                        cal.add(Calendar.DAY_OF_YEAR, skipDays)

                        dates.add(sdf.format(cal.time))
                        for (i in 0 until no_days - 1) {
                            cal.add(Calendar.DAY_OF_YEAR, 7)
                            dates.add(sdf.format(cal.time))
                        }
                    }
                    if (checTuesday.isChecked) {
                        cal.time = sdf.parse(binding.edtStartDate.text.toString())
                        if (dayOfWeek > 3)
                            skipDays = 7 - dayOfWeek + 3
                        else
                            3 - dayOfWeek
                        if (skipDays == 0) skipDays = 7
                        cal.add(Calendar.DAY_OF_YEAR, skipDays)

                        dates.add(sdf.format(cal.time))
                        for (i in 0 until no_days - 1) {
                            cal.add(Calendar.DAY_OF_YEAR, 7)
                            dates.add(sdf.format(cal.time))
                        }
                    }
                    if (checWednesday.isChecked) {
                        cal.time = sdf.parse(binding.edtStartDate.text.toString())
                        skipDays = if (dayOfWeek > 4)
                            7 - dayOfWeek + 4
                        else 4 - dayOfWeek
                        if (skipDays == 0) skipDays = 7
                        cal.add(Calendar.DAY_OF_YEAR, skipDays)

                        dates.add(sdf.format(cal.time))
                        for (i in 0 until no_days - 1) {
                            cal.add(Calendar.DAY_OF_YEAR, 7)
                            dates.add(sdf.format(cal.time))
                        }
                    }
                    if (checTuesday.isChecked) {
                        cal.time = sdf.parse(binding.edtStartDate.text.toString())
                        skipDays = if (dayOfWeek > 5)
                            7 - dayOfWeek + 5
                        else
                            5 - dayOfWeek
                        if (skipDays == 0) skipDays = 7
                        cal.add(Calendar.DAY_OF_YEAR, skipDays)

                        dates.add(sdf.format(cal.time))
                        for (i in 0 until no_days - 1) {
                            cal.add(Calendar.DAY_OF_YEAR, 7)
                            dates.add(sdf.format(cal.time))
                        }
                    }
                    if (checFriday.isChecked) {
                        cal.time = sdf.parse(binding.edtStartDate.text.toString())
                        skipDays = if (dayOfWeek > 6)
                            7 - dayOfWeek + 6
                        else 6 - dayOfWeek
                        if (skipDays == 0) skipDays = 7
                        cal.add(Calendar.DAY_OF_YEAR, skipDays)

                        dates.add(sdf.format(cal.time))
                        for (i in 0 until no_days - 1) {
                            cal.add(Calendar.DAY_OF_YEAR, 7)
                            dates.add(sdf.format(cal.time))
                        }
                    }
                    if (checSaturday.isChecked) {
                        cal.time = sdf.parse(binding.edtStartDate.text.toString())
                        skipDays = if (dayOfWeek > 7)
                            7 - dayOfWeek + 7
                        else 7 - dayOfWeek
                        if (skipDays == 0) skipDays = 7
                        cal.add(Calendar.DAY_OF_YEAR, skipDays)

                        dates.add(sdf.format(cal.time))
                        for (i in 0 until no_days - 1) {
                            cal.add(Calendar.DAY_OF_YEAR, 7)
                            dates.add(sdf.format(cal.time))
                        }
                    }

                    Log.d("Datelist", "getDate: ${dates.toString()}")
                }
            }
            eventshash.clear()
            event.clear()
            for (i in 0 until dates.size) {
                val currDate: Date = sdf.parse(dates[i])
                val cc: Calendar = Calendar.getInstance()
                cc.time = currDate
                eventshash.add(
                    EventDay(
                        cc,
                        R.drawable.ic_baseline_fiber_manual_record_24
                    )
                )
            }
            event.addAll(eventshash)
            binding.calander.setEvents(event)

        }

        bundle.putDouble("totalamount", viewModel.price * dates.size * box)
        bundle.putString("start_date", binding.edtStartDate.text.toString())
        bundle.putString("expiry_date", binding.edtEndDate.text.toString())
        bundle.putString("quantity", "$box")
        bundle.putString("product", "${viewModel.item_id}")
        bundle.putString("ordertype", "subitem")
        bundle.putIntegerArrayList("days", days)
        bundle.putStringArrayList("dates", dates)
        bundle.putString("schduleTime", schduleTime)

        bundle.putDouble("amount", viewModel.price)
        bundle.putString("slot", schduleTime)
        bundle.putInt("qnt", box)
        bundle.putInt("item_id", viewModel.item_id)
        bundle.putInt("subId", viewModel.subId)


    }


    private fun clearDays() {
        binding.apply {
            radioDays.clearCheck()
            checMonday.isChecked = false
            checTuesday.isChecked = false
            checWednesday.isChecked = false
            checThursday.isChecked = false
            checFriday.isChecked = false
            checSaturday.isChecked = false
            checSunday.isChecked = false

            chec2Monday.isChecked = false
            chec2Tuesday.isChecked = false
            chec2Wednesday.isChecked = false
            chec2Thursday.isChecked = false
            chec2Friday.isChecked = false
            chec2Saturday.isChecked = false
            chec2Sunday.isChecked = false
        }
    }

    private fun startDataOn() {

        startdatePickerDialog.show()
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val mmonth: Int = month + 1
        val dday: Int = dayOfMonth
        var monthShow = ""
        var dayShow = ""
        monthShow = if (mmonth.toString().length == 1) {
            "0$mmonth"
        } else {
            "" + mmonth
        }

        dayShow = if (dday.toString().length == 1) {
            "0$dayOfMonth"
        } else {
            "" + dayOfMonth
        }
        binding.edtStartDate.setText("$year-$monthShow-$dayShow")
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val cal = Calendar.getInstance()
        var skipdays = 0
        binding.apply {
            if (loc4weeks.isChecked)
                skipdays = 4 * 7
            if (loc8weeks.isChecked)
                skipdays = 8 * 7
        }
        cal.add(Calendar.DAY_OF_YEAR, skipdays)
        val enddate: Date = cal.time

        binding.edtEndDate.setText(sdf.format(enddate))
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