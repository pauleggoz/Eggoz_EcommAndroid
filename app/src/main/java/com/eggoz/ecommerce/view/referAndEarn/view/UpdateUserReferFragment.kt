package com.eggoz.ecommerce.view.referAndEarn.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.ViewModelFactory
import com.eggoz.ecommerce.databinding.FragmentAddressBinding
import com.eggoz.ecommerce.databinding.FragmentUpdateUserReferBinding
import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.localdata.room.MyDatabase
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.utils.Validation
import com.eggoz.ecommerce.view.address.viewmodel.AddressRepository
import com.eggoz.ecommerce.view.address.viewmodel.AddressViewModel
import com.eggoz.ecommerce.view.referAndEarn.viewmodel.UpdateReferEarnRepository
import com.eggoz.ecommerce.view.referAndEarn.viewmodel.UpdateReferEarnViewModel
import kotlinx.coroutines.launch


class UpdateUserReferFragment : Fragment() {

    private var _binding: FragmentUpdateUserReferBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: UpdateReferEarnViewModel
    private lateinit var dialog: Loadinddialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateUserReferBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        dialog = Loadinddialog()
        val repository =
            UpdateReferEarnRepository(UserPreferences(requireContext()))
        val viewmodelFat = ViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewmodelFat)[UpdateReferEarnViewModel::class.java]

        binding.btnSubmit.setOnClickListener { submit() }
    }

    private fun submit() {
        if (Validation.emptyField(
                binding.edtName,
                binding.edtNameLayout,
                "Enter Name"
            )
            && Validation.emailField(
                binding.edtEmail,
                binding.edtEmailLayout,
                "Enter Email",
                "Enter Valid Email"
            )
        ) {
            if (!dialog.isShowing())
                dialog.create(requireContext())
            viewModel.referAndEarn(
                binding.edtName.text.toString(),
                binding.edtEmail.text.toString(),
                binding.edtReferalCode.text.toString()
            ).observe(viewLifecycleOwner) {

                if (dialog.isShowing())
                    dialog.dismiss()
                if (it.errorType == null) {
                    lifecycleScope.launch {
                        it.referralData?.referralCode?.let { referralCode->
                            if (referralCode.length>3)
                                Navigation.findNavController(binding.root)
                                    .navigate(R.id.nav_home)
                        }}
                }
            }
        }
    }

}