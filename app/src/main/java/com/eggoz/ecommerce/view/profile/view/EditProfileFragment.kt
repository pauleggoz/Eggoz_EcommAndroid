package com.eggoz.ecommerce.view.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.eggoz.ecommerce.ViewModelFactory
import com.eggoz.ecommerce.databinding.FragmentEditProfileBinding
import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.utils.Validation
import com.eggoz.ecommerce.view.profile.viewModel.ProfileRepository
import com.eggoz.ecommerce.view.profile.viewModel.ProfileViewModel
import kotlinx.coroutines.launch


class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!


    private lateinit var dialog: Loadinddialog
    private lateinit var viewModel: ProfileViewModel

    private var userid: Int = -1
    private var isverifide = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {

        val repository = ProfileRepository(UserPreferences(requireContext()))
        val viewmodelFat = ViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewmodelFat)[ProfileViewModel::class.java]
        dialog = Loadinddialog()

        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(binding.root)
                    .popBackStack()
            }
            edtName.setText(this@EditProfileFragment.arguments?.getString("name", "") ?: "")
            edtEmail.setText(this@EditProfileFragment.arguments?.getString("email", "") ?: "")
            edtPhone.setText(
                this@EditProfileFragment.arguments?.getString("phoneNo", "")?.replace("+91", "")
                    ?: ""
            )
            isverifide =
                this@EditProfileFragment.arguments?.getBoolean("isverifide", false) ?: false
        }

        binding.btnSubmit.setOnClickListener {
            submit()
        }
    }

    private fun submit() {
        if (Validation.emptyField(
                binding.edtName,
                binding.edtNameLayout,
                "Enter Name"
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
            )
        ) {
            if (!dialog.isShowing())
                dialog.create(requireContext())
            viewModel.Edituser(
                userid,
                name = binding.edtName.text.toString(),
                email = binding.edtEmail.text.toString(),
                "+91" + binding.edtPhone.text.toString()
            ).observe(viewLifecycleOwner) {

                if (dialog.isShowing())
                    dialog.dismiss()
                if (it.errorType == null) {
                    lifecycleScope.launch {
                        if (it.userTokenData != null)
                            if (it.userTokenData.token != null) {
                                viewModel.saveToken(token = it.userTokenData.token)
                                Navigation.findNavController(binding.txtBtn)
                                    .popBackStack()
                            }
                    }
                }
            }
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