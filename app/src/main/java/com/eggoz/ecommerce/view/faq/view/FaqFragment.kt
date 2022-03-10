package com.eggoz.ecommerce.view.faq.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.ViewModelFactory
import com.eggoz.ecommerce.databinding.FragmentFaqBinding
import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.utils.Validation
import com.eggoz.ecommerce.view.faq.viewmodel.FaqRepository
import com.eggoz.ecommerce.view.faq.viewmodel.FaqViewModel
import com.eggoz.ecommerce.view.referAndEarn.viewmodel.UpdateReferEarnRepository
import com.eggoz.ecommerce.view.referAndEarn.viewmodel.UpdateReferEarnViewModel


class FaqFragment : Fragment() {


    private var _binding: FragmentFaqBinding? = null
    private val binding get() = _binding!!


    private lateinit var viewModel: FaqViewModel
    private lateinit var dialog: Loadinddialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaqBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView(){
        dialog = Loadinddialog()
        val repository =
            FaqRepository(UserPreferences(requireContext()))
        val viewmodelFat = ViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewmodelFat)[FaqViewModel::class.java]

        binding.apply {
            btnSubmit.setOnClickListener {
                submit()
            }
            viewModel.faq.observe(viewLifecycleOwner) {
                if (it.errorType==null) {
                    Navigation.findNavController(root)
                        .navigate(R.id.nav_home)
                    Toast.makeText(context,"Message Send",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,"${it.errorType}",Toast.LENGTH_SHORT).show()
                }
            }

        }


    }

    private fun submit(){
        if (Validation.emptyField(
                binding.edtFirstName,
                binding.edtFirstNameLayout,
                "Enter First Name"
            ) && Validation.FieldWithLength(
                binding.edtPhone,
                binding.edtPhoneLayout,
                "Enter Mobile No",
                10
            )
            && Validation.emailField(
                binding.edtEmail,
                binding.edtEmailLayout,
                "Enter Email",
                "Enter Valid Email"
            )&&
            Validation.emptyField(
                binding.edtFirstName,
                binding.edtFirstNameLayout,
                "Enter First Name"
            )
        ){
            viewModel.faq(binding.edtFirstName.text.toString(),
                binding.edtLastName.text.toString(),
                binding.edtEmail.text.toString(),
                binding.edtPhone.text.toString(),
                binding.edtMessage.text.toString())

        }
    }

}