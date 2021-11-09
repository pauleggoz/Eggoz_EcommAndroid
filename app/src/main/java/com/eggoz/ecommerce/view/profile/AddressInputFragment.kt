package com.eggoz.ecommerce.view.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentAddressInputBinding
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.utils.Validation
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AddressInputFragment : Fragment() {

    private var _binding: FragmentAddressInputBinding? = null
    private val binding get() = _binding!!

    private var address_id: Int = -1
    private var user_id: Int = -1
    private lateinit var addressName: String
    private lateinit var Mobile: String
    private lateinit var buildingAddress: String
    private lateinit var streetAddress: String
    private lateinit var landmark: String
    private lateinit var cityName: String
    private var state: String = ""
    private var pincode = -1
    private lateinit var viewModel: ProfileViewModel
    private var userPreferences: UserPreferences? = null
    private lateinit var dialog: Loadinddialog
    private lateinit var cityList: ArrayList<String>
    private lateinit var cityListid: ArrayList<Int>
    private lateinit var LocalityList: ArrayList<String>
    private lateinit var LocalityListid: ArrayList<String>
    private var stateid: Int = -1
    private var cityid: Int = -1

    //    private lateinit var cityid: String
    private var selloc: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressInputBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        userPreferences = UserPreferences(requireContext())
        dialog = Loadinddialog()

        cityList = ArrayList()
        cityListid = ArrayList()
        LocalityList = ArrayList()
        LocalityListid = ArrayList()



        lifecycleScope.launch {
            user_id = userPreferences?.Customer_id?.buffer()?.first() ?: -1
        }
        address_id = this.arguments?.getInt("id", -1) ?: -1
        addressName = this.arguments?.getString("addressName", "") ?: ""
        Mobile = this.arguments?.getString("Mobile", "") ?: ""
        buildingAddress = this.arguments?.getString("buildingAddress", "") ?: ""
        streetAddress = this.arguments?.getString("streetAddress", "") ?: ""
        landmark = this.arguments?.getString("landmark", "") ?: ""
        cityName = this.arguments?.getString("cityName", "") ?: ""

        cityid = this.arguments?.getInt("cityid", -1) ?: -1
        stateid = this.arguments?.getInt("stateid", -1) ?: -1

        pincode = this.arguments?.getInt("pinCode", -1) ?: -1


        Log.d("data", "address_id $address_id stateid $stateid cityid $cityid")


        binding.apply {

            edtState.setOnItemClickListener { parent, view, position, id ->
                stateid = LocalityListid[position].toInt()
                getLocality(stateid)
            }

            edtCity.setOnItemClickListener { parent, view, position, id ->
                cityid = cityListid[position]
                selloc = position
            }
            btnBack.setOnClickListener {
                Navigation.findNavController(binding.root)
                    .popBackStack()
            }

            edtCity.keyListener = null
            edtCity.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.app_color
                )
            )
            edtState.keyListener = null
            edtState.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.app_color
                )
            )


        }

        if (address_id != -1) {
            binding.apply {
                edtAddressName.setText(addressName)
                edtPhone.setText(Mobile.replace("+91", ""))
                edtBuildingAddress.setText(buildingAddress)
                edtStreetAddress.setText(streetAddress)
                edtLandmark.setText(landmark)
//                edtCity.setText(cityName)
//                edtState.setText(state)
                edtPincode.setText("$pincode")

            }
            getCityEditMode()
            updateaddressSubmit()
        } else {
            getCity()
            addaddressSubmit()
        }

        checkPermission()


    }

    private fun checkPermission() {

    }

    private fun updateaddressSubmit() {

        binding.apply {
            btnSubmit.setOnClickListener {

                if (Validation.emptyField(
                        binding.edtAddressName,
                        binding.edtAddressNameLayout,
                        "Enter Address Name"
                    ) && Validation.emptyField(
                        binding.edtBuildingAddress,
                        binding.edtBuildingAddressLayout,
                        "Enter Building Address"
                    )
                    && Validation.emptyField(
                        binding.edtStreetAddress,
                        binding.edtStreetAddressLayout,
                        "Enter Street Address"
                    ) && Validation.emptyField(
                        binding.edtLandmark,
                        binding.edtLandmarkLayout,
                        "Enter Landmark"
                    ) && Validation.FieldWithLength(
                        binding.edtPhone,
                        binding.edtPhoneLayout,
                        "Enter Mobile No",
                        10
                    ) && Validation.FieldWithLength(
                        binding.edtPincode,
                        binding.edtPincodeLayout,
                        "Enter Pincode",
                        6
                    )
                ) {
                    if (stateid == -1)
                        Toast.makeText(context, "Select State", Toast.LENGTH_SHORT).show()
                    else if (cityid == -1)
                        Toast.makeText(context, "Select City", Toast.LENGTH_SHORT).show()
                    else
                        updateAdress()
                }
            }
        }
    }

    private fun updateAdress() {

        lifecycleScope.launch {
            if (!dialog.isShowing())
                dialog.create(requireContext())
            binding.apply {
                viewModel.UpdateAddress(
                    user_id = user_id,
                    addressName = edtAddressName.text.toString(),
                    phone = "+91${edtPhone.text.toString()}",
                    address_id = "$address_id",
                    BuildingAddress = edtBuildingAddress.text.toString(),
                    StreetAddress = edtStreetAddress.text.toString(),
                    Landmark = edtLandmark.text.toString(),
                    City = cityid,
                    State = stateid,
                    Pincode = edtPincode.text.toString(),
                    context = requireContext()
                )
            }

            viewModel.responUser.observe(viewLifecycleOwner, {
                if (it.errorType == null)
                    Navigation.findNavController(binding.root)
                        .popBackStack()

                if (dialog.isShowing())
                    dialog.dismiss()

            })
        }
    }

    private fun getCity() {
        lifecycleScope.launch {
            if (!dialog.isShowing())
                dialog.create(requireContext())

            viewModel.getCity()
            viewModel.responsecity.observe(viewLifecycleOwner,
                {

                    if (dialog.isShowing())
                        dialog.dismiss()

                    Log.d("data", it.toString())
                    if (it?.errorType != null) {
                        Toast.makeText(requireContext(), it.errorType, Toast.LENGTH_SHORT).show()

                    } else {

                        if (it != null) {
                            LocalityList.clear()
                            LocalityListid.clear()
                            if (it.results != null)
                                for (i in 0 until it.results?.size!!) {
                                    LocalityList.add(it.results!![i].zoneName ?: "")
                                    LocalityListid.add("${it.results!![i].id ?: -1}")
                                }
                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.select_dialog_item,
                                LocalityList
                            )
                            binding.apply {
                                edtState.threshold = 1
                                edtState.setAdapter(adapter)

                            }

                        }
                    }
                })
        }
    }

    private fun getCityEditMode() {
        lifecycleScope.launch {
            if (!dialog.isShowing())
                dialog.create(requireContext())

            viewModel.getCity()
            viewModel.responsecity.observe(viewLifecycleOwner,
                {

                    if (dialog.isShowing())
                        dialog.dismiss()

                    Log.d("data", it.toString())
                    if (it?.errorType != null) {
                        Toast.makeText(requireContext(), it.errorType, Toast.LENGTH_SHORT).show()

                    } else {

                        if (it != null) {
                            LocalityList.clear()
                            LocalityListid.clear()
                            if (it.results != null)
                                for (i in 0 until it.results?.size!!) {
                                    LocalityList.add(it.results!![i].zoneName ?: "")
                                    LocalityListid.add("${it.results!![i].id ?: -1}")
                                    if (stateid == it.results!![i].id ?: -1)
                                        binding.edtState.setText(
                                            it.results!![i].zoneName ?: "",
                                            false
                                        )
                                }
                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.select_dialog_item,
                                LocalityList
                            )
                            binding.apply {
                                edtState.threshold = 1
                                edtState.setAdapter(adapter)
/*
                                edtState.setText(edtState.adapter.getItem(0).toString(),false)
                                stateid = LocalityListid[0].toInt()*/
                                getLocalityEditMode(stateid)

                            }

                        }
                    }
                })
        }
    }

    private fun getLocality(id: Int) {
        lifecycleScope.launch {
            if (!dialog.isShowing())
                dialog.create(requireContext())

            viewModel.getLocality(id = id)
            viewModel.responselocality.observe(viewLifecycleOwner, {

                if (dialog.isShowing())
                    dialog.dismiss()

                if (it?.errorType != null) {

                } else {

                    if (it?.cities != null) {
                        if (it.cities!![0].ecommerceSectors != null) {

                            cityList.clear()
                            cityListid.clear()
                            for (i in 0 until it.cities!![0].ecommerceSectors?.size!!) {
                                cityList.add(it.cities!![0].ecommerceSectors!![i].sectorName ?: "")
                                cityListid.add(it.cities!![0].ecommerceSectors!![i].id ?: -1)
                                if (it.cities!![0].ecommerceSectors!![i].id ?: -1 == stateid)
                                    selloc = i
                            }
                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.select_dialog_item,
                                cityList
                            )
                            binding.apply {
                                edtCity.threshold = 1
                                edtCity.setAdapter(adapter)

                                edtCity.setText(edtCity.adapter.getItem(selloc).toString(), false)
                            }
                        }
                    }

                }
            })
        }
    }

    private fun getLocalityEditMode(id: Int) {
        lifecycleScope.launch {
            if (!dialog.isShowing())
                dialog.create(requireContext())

            viewModel.getLocality(id = id)
            viewModel.responselocality.observe(viewLifecycleOwner, {

                if (dialog.isShowing())
                    dialog.dismiss()

                if (it?.errorType != null) {

                } else {

                    if (it?.cities != null) {
                        if (it.cities!![0].ecommerceSectors != null) {

                            cityList.clear()
                            cityListid.clear()
                            if (it.cities!![0].ecommerceSectors != null)
                                for (i in 0 until it.cities!![0].ecommerceSectors?.size!!) {
                                    cityList.add(
                                        it.cities!![0].ecommerceSectors!![i].sectorName ?: ""
                                    )
                                    cityListid.add(it.cities!![0].ecommerceSectors!![i].id ?: -1)
                                    Log.d(
                                        "data",
                                        "cityid ${it.cities!![0].ecommerceSectors!![i].id ?: -1}"
                                    )
                                    if (it.cities!![0].ecommerceSectors!![i].id ?: -1 == cityid) {
                                        selloc = i
                                        Log.d(
                                            "data",
                                            "sectorName ${it.cities!![0].ecommerceSectors!![i].sectorName}"
                                        )
                                        binding.edtCity.setText(
                                            it.cities!![0].ecommerceSectors!![i].sectorName ?: "",
                                            false
                                        )
                                    }
                                }
                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.select_dialog_item,
                                cityList
                            )
                            binding.apply {
                                edtCity.threshold = 1
                                edtCity.setAdapter(adapter)
                                Log.d("data", "id $cityid loc $selloc")
                            }
                        }
                    }

                }
            })
        }
    }

    private fun addaddressSubmit() {
        binding.apply {
            btnSubmit.setOnClickListener {

                if (Validation.emptyField(
                        binding.edtAddressName,
                        binding.edtAddressNameLayout,
                        "Enter Address Name"
                    ) && Validation.emptyField(
                        binding.edtBuildingAddress,
                        binding.edtBuildingAddressLayout,
                        "Enter Building Address"
                    )
                    && Validation.emptyField(
                        binding.edtStreetAddress,
                        binding.edtStreetAddressLayout,
                        "Enter Street Address"
                    ) && Validation.emptyField(
                        binding.edtLandmark,
                        binding.edtLandmarkLayout,
                        "Enter Landmark"
                    ) && Validation.FieldWithLength(
                        binding.edtPhone,
                        binding.edtPhoneLayout,
                        "Enter Mobile No",
                        10
                    ) && Validation.FieldWithLength(
                        binding.edtPincode,
                        binding.edtPincodeLayout,
                        "Enter Pincode",
                        6
                    )
                ) {
                    if (stateid == -1)
                        Toast.makeText(context, "Select State", Toast.LENGTH_SHORT).show()
                    else if (cityid == -1)
                        Toast.makeText(context, "Select City", Toast.LENGTH_SHORT).show()
                    else
                        addAdress()
                }
            }
        }
    }

    private fun addAdress() {
        lifecycleScope.launch {
            if (!dialog.isShowing())
                dialog.create(requireContext())
            binding.apply {
                viewModel.addAddress(
                    user_id = user_id,
                    addressName = edtAddressName.text.toString(),
                    phone = "+91${edtPhone.text.toString()}",
                    address_id = "$address_id",
                    BuildingAddress = edtBuildingAddress.text.toString(),
                    StreetAddress = edtStreetAddress.text.toString(),
                    Landmark = edtLandmark.text.toString(),
                    City = cityid,
                    State = stateid,
                    Pincode = edtPincode.text.toString(),
                    context = requireContext()
                )
            }

            viewModel.responUser.observe(viewLifecycleOwner, {
                if (dialog.isShowing())
                    dialog.dismiss()

                if (it.errorType == null)
                    Navigation.findNavController(binding.root)
                        .popBackStack()

                Log.d("data", it.toString())
            })
        }
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