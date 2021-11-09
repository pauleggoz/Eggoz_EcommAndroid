package com.eggoz.ecommerce.view.SignIn

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentSigin2Binding
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.utils.Validation
import com.eggoz.ecommerce.view.MainViewModel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class Sigin2Fragment : Fragment() {
    private var _binding: FragmentSigin2Binding? = null
    private val binding get() = _binding!!

    private var mobile = ""
    private var otp = ""
    private var city_id = -1
    private var loc_id = -1
    private var userPreferences: UserPreferences? = null
    private lateinit var viewModel: MainViewModel
    private lateinit var dialog: Loadinddialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSigin2Binding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        dialog = Loadinddialog()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mobile = this.arguments?.getString("mobile", "") ?: ""
        otp = this.arguments?.getString("otp", "") ?: ""


        userPreferencesdata()

        binding.apply {
//            edtOtp.setText(otp)
            otpInput.setText(otp)

            otpInput.setOtpCompletionListener {
                validation()
            }

            root.isFocusableInTouchMode = true
            root.requestFocus()
            root.setOnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                    return@setOnKeyListener true
                } else false
            }

            btnSubmit.setOnClickListener {
                validation()
            }
        }
    }

    private fun userPreferencesdata() {
        userPreferences = UserPreferences(requireContext())

        lifecycleScope.launch {
            city_id = userPreferences?.city?.buffer()?.first() ?: -1
            loc_id = userPreferences?.loc?.buffer()?.first() ?: -1
        }
    }

    private fun validation() {


        lifecycleScope.launch {

            if (!dialog.isShowing())
                dialog.create(requireContext())
            viewModel.validate(
                mobile = "+91$mobile",
                otp = binding.otpInput.text.toString(),
                loc_id = loc_id,
                city_id = city_id
            )
            viewModel.responOtpverify.observe(viewLifecycleOwner, {

                    var sas = it.toString();

                    if (dialog.isShowing())
                        dialog.dismiss()

                    if (it?.errorType != null) {
                        Toast.makeText(requireContext(), it.errorType, Toast.LENGTH_SHORT)
                            .show()

                    } else {
                        lifecycleScope.launch {
                            userPreferences!!.saveAuthtoke(token = it.token ?: "")
                            userPreferences!!.saveCustomerid(id = it.user?.id ?: -1)
                            if (it.user?.userProfile?.departmentProfiles?.size ?: 0 > 0)
                                for (i in it.user?.userProfile?.departmentProfiles?.indices!!) {
                                    if (it.user?.userProfile?.departmentProfiles!![i].customerProfile != null)
                                        userPreferences!!.saveUserid(
                                            id = it.user?.userProfile?.departmentProfiles!![i].customerProfile
                                                ?: -1
                                        )
                                }
                        }
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.nav_home)
                    }
                })
        }


    }

    override fun onStop() {
        super.onStop()
        _binding = null
    }

}