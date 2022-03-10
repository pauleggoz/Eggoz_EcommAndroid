package com.eggoz.ecommerce.view.contactus.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.eggoz.ecommerce.ViewModelFactory
import com.eggoz.ecommerce.databinding.FragmentContactUsBinding
import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.contactus.viewmodel.ContactUsViewModel
import com.eggoz.ecommerce.view.contactus.viewmodel.ContactusRepository
import kotlinx.coroutines.launch


class ContactUsFragment : Fragment() {
    private lateinit var binding: FragmentContactUsBinding
    private lateinit var viewModel: ContactUsViewModel
    private lateinit var dialog: Loadinddialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactUsBinding.inflate(inflater, container, false)

        init()
        return binding.root
    }

    private fun init() {
        val repository = ContactusRepository(UserPreferences(requireContext()))
        val viewmodelFat = ViewModelFactory(repository)
        dialog = Loadinddialog()

        viewModel = ViewModelProvider(this, viewmodelFat)[ContactUsViewModel::class.java]

        getDetail()

        binding.apply {
            txtAddressOpen.setOnClickListener {
                val map = "http://maps.google.co.in/maps?q=${viewModel.companyAddress}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(map))
                startActivity(intent)
            }
            txtMobileOpen.setOnClickListener {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + viewModel.companyMobile))
                startActivity(intent)
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun getDetail() {
        if (!dialog.isShowing())
            dialog.create(requireContext())

        lifecycleScope.launch {
            viewModel.contact.observe(viewLifecycleOwner) {
                if (dialog.isShowing())
                    dialog.dismiss()
                binding.apply {
                    txtAddress.text = it.companyAddress ?: ""
                    txtMobile.text = "${it.companyMobile ?: ""}\n${it.companyEmail ?: ""}"
                    txtTime1.text = "Monday-Friday\n${it.officeTimings?.get(0)?.monFri ?: "Na"}"
                    txtTime2.text = "Saturday-Sunday\n${it.officeTimings?.get(1)?.satSun ?: "Na"}"
                    viewModel.companyAddress=it.companyAddress
                    viewModel.companyMobile=it.companyMobile
                }
            }
        }

    }

}