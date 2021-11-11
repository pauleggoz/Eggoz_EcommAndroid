package com.eggoz.ecommerce.view.Subscribe

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
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.data.UserPreferences
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

    private var eggType = ""
    private var boxType = -1
    private var schduleTime = ""
    private lateinit var days: ArrayList<Int>
    private lateinit var dialog: Loadinddialog
    private var city_id = -1
    private var user_id = -1
    private var userPreferences: UserPreferences? = null

    private var result: List<Products.Result>? = null

    private var selitem = -1

    lateinit var startdatePickerDialog: DatePickerDialog
    lateinit var enddatePickerDialog: DatePickerDialog


    var year: Int = -1
    var month: Int = -1
    var day = -1
    var mont: Int = -1
    var box: Int = 0

    var price: Double = 0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscribeBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        val bundle = Bundle()
        viewModel = ViewModelProvider(this).get(SubscribeViewModel::class.java)
        network = NetworkConnect(requireContext())
        days = ArrayList()
        dialog = Loadinddialog()
        userPreferences = UserPreferences(requireContext())


        binding.apply {
            val c = Calendar.getInstance()
            year = c[Calendar.YEAR]
            month = c[Calendar.MONTH]
            day = c[Calendar.DAY_OF_MONTH]
            mont = month + 1


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
                if (loc8am.isChecked) {
                    schduleTime = "1"
                } else if (loc11am.isChecked) {
                    schduleTime = "2"
                } else if (loc2pm.isChecked) {
                    schduleTime = "3"
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
                if (locOneweekly.isChecked) {
                    days.clear()
                    clearDays()
                    radioDays.visibility = View.VISIBLE
                    layoutCheckdayys.visibility = View.GONE
                } else if (locOne2weekly.isChecked) {
                    days.clear()
                    clearDays()
                    radioDays.visibility = View.VISIBLE
                    layoutCheckdayys.visibility = View.GONE
                } else if (locCustom.isChecked) {
                    days.clear()
                    clearDays()
                    radioDays.visibility = View.GONE
                    layoutCheckdayys.visibility = View.VISIBLE
                }
                layDays.visibility = View.VISIBLE
            }

            radioDays.setOnCheckedChangeListener { radioGroup, i ->
                layPlan.visibility = View.VISIBLE
                if (locMonday.isChecked) {
                    days.clear()
                    days.add(1)
                } else if (locTuesday.isChecked) {
                    days.clear()
                    days.add(2)
                } else if (locWednesday.isChecked) {
                    days.clear()
                    days.add(3)
                } else if (locThursday.isChecked) {
                    days.clear()
                    days.add(4)
                } else if (locFriday.isChecked) {
                    days.clear()
                    days.add(5)
                } else if (locSaturday.isChecked) {
                    days.clear()
                    days.add(6)
                } else if (locSunday.isChecked) {
                    days.clear()
                    days.add(7)
                }
                layFrequency.visibility = View.VISIBLE
                if (!layFrequency.isVisible) {
                    txt2.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.active_dot)
                    txt3.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.current_active_dot)
                }
            }

            checMonday.setOnCheckedChangeListener { compoundButton, b ->
                if (days.contains(1) && !b)
                    days.remove(1)
                else if (!days.contains(1) && b)
                    days.add(1)
                if (!layPlan.isVisible)
                    layPlan.visibility = View.VISIBLE
            }
            checTuesday.setOnCheckedChangeListener { compoundButton, b ->
                if (days.contains(2) && !b)
                    days.remove(2)
                else if (!days.contains(2) && b)
                    days.add(2)
                if (!layPlan.isVisible)
                    layPlan.visibility = View.VISIBLE
            }
            checWednesday.setOnCheckedChangeListener { compoundButton, b ->
                if (days.contains(3) && !b)
                    days.remove(3)
                else if (!days.contains(3) && b)
                    days.add(3)
                if (!layPlan.isVisible)
                    layPlan.visibility = View.VISIBLE
            }
            checThursday.setOnCheckedChangeListener { compoundButton, b ->
                if (days.contains(4) && !b)
                    days.remove(4)
                else if (!days.contains(4) && b)
                    days.add(4)
                if (!layPlan.isVisible)
                    layPlan.visibility = View.VISIBLE
            }
            checFriday.setOnCheckedChangeListener { compoundButton, b ->
                if (days.contains(5) && !b)
                    days.remove(5)
                else if (!days.contains(5) && b)
                    days.add(5)
                if (!layPlan.isVisible)
                    layPlan.visibility = View.VISIBLE
            }
            checSaturday.setOnCheckedChangeListener { compoundButton, b ->
                if (days.contains(6) && !b)
                    days.remove(6)
                else if (!days.contains(6) && b)
                    days.add(6)
                if (!layPlan.isVisible)
                    layPlan.visibility = View.VISIBLE
            }
            checSunday.setOnCheckedChangeListener { compoundButton, b ->
                if (days.contains(7) && !b)
                    days.remove(7)
                else if (!days.contains(7) && b)
                    days.add(7)
                if (!layPlan.isVisible)
                    layPlan.visibility = View.VISIBLE
            }

            edtStartDate.setOnClickListener {
                startDataOn()
            }
            radioPlan.setOnCheckedChangeListener { radioGroup, i ->
                if (loc4weeks.isChecked) {
                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    val cal = Calendar.getInstance()
                    var skipdays = 4 * 7
                    cal.add(Calendar.DAY_OF_YEAR, skipdays)
                    val enddate: Date = cal.time

                    binding.edtEndDate.setText(sdf.format(enddate))

                } else if (loc8weeks.isChecked) {
                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    val cal = Calendar.getInstance()
                    var skipdays = 8 * 7
                    cal.add(Calendar.DAY_OF_YEAR, skipdays)
                    val enddate: Date = cal.time

                    binding.edtEndDate.setText(sdf.format(enddate))

                } else if (locPowerpack.isChecked) {
                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    val cal = Calendar.getInstance()
                    var skipdays = 4 * 7
                    cal.add(Calendar.DAY_OF_YEAR, skipdays)
                    val enddate: Date = cal.time

                    binding.edtEndDate.setText(sdf.format(enddate))

                }
                layDate.visibility = View.VISIBLE
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
//                selectdays()

                bundle.putDouble("amount", price)
                bundle.putString("start_date", binding.edtStartDate.text.toString())
                bundle.putString("expiry_date", binding.edtEndDate.text.toString())
                bundle.putString("quantity", "$box")
                bundle.putString("product", "$selitem")
                bundle.putString("ordertype", "subitem")
                bundle.putIntegerArrayList("days", days)

                if (!dialog.isShowing())
                    dialog.create(requireContext())
                viewModel.subscriptions(
                    start_date = binding.edtStartDate.text.toString(),
                    expiry_date = binding.edtEndDate.text.toString(),
                    customer = user_id,
                    quantity = box,
                    product = selitem,
                    slot = "",
                    days = days,
                    context = requireContext()
                )
            }

            viewModel.responsemembershiprecharge.observe(viewLifecycleOwner, {
                if (dialog.isShowing())
                    dialog.dismiss()
                Navigation.findNavController(root)
                    .navigate(R.id.action_nav_subscribe_to_nav_address_list, bundle)
            })
        }


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
        }
    }

    private fun startDataOn() {

        startdatePickerDialog.show()
    }

    private fun endDataOn() {

        enddatePickerDialog.show()
    }


    private fun prodList() {
        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {
            city_id = userPreferences?.city?.buffer()?.first() ?: -1
            Log.d("data", "city$city_id")
            viewModel.productList(
                city = city_id, is_available = 1
            )
            viewModel.responProduct.observe(viewLifecycleOwner, {


                if (dialog.isShowing())
                    dialog.dismiss()

                if (it?.errorType != null) {
                    Toast.makeText(requireContext(), it.errorType, Toast.LENGTH_SHORT)
                        .show()

                } else {
                    if (it.results != null) {
                        this@SubscribeFragment.result = it.results
                    }
                }
            })
        }


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
            if (locPowerpack.isChecked)
                skipdays = 4 * 7
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