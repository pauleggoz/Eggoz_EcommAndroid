package com.eggoz.ecommerce.view.SignIn

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.Navigation
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentSignIn1Binding
import com.eggoz.ecommerce.utils.Constants.Companion.RESOLVE_HINT
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.utils.Validation
import com.eggoz.ecommerce.view.MainViewModel
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest
import kotlinx.coroutines.launch

class SignIn1Fragment : Fragment() {

    private var _binding: FragmentSignIn1Binding? = null
    private val binding get() = _binding!!


    private var number = ""
    private lateinit var viewModel: MainViewModel
    private var userPreferences: UserPreferences? = null

    private lateinit var dialog: Loadinddialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignIn1Binding.inflate(inflater, container, false)
        init()
        getNumber()

        return binding.root
    }

    private fun init() {
        userPreferences = UserPreferences(requireContext())
        dialog = Loadinddialog()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        lifecycleScope.launch {
            userPreferences!!.saveAuthtoke("")
        }

        binding.apply {
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

    private fun validation() {
        if (Validation.FieldWithLength(
                data = binding.edtMobileNo,
                error = binding.edtOtpLayout,
                message = "Pls Enter Mobile Number",
                length = 10
            )
        ) {
            lifecycleScope.launch {
                if (!dialog.isShowing())
                    dialog.create(requireContext())
                viewModel.Login(mobile = "+91" + binding.edtMobileNo.text.toString())
                    .observe(viewLifecycleOwner,
                    Observer {

                        if (dialog.isShowing())
                            dialog.dismiss()

                        if (it?.errorType != null) {
                            Toast.makeText(requireContext(), it.errorType, Toast.LENGTH_SHORT)
                                .show()

                        } else {
                            Log.d("data", "otp=${it.otpval}")

                            binding.apply {
                                val bundle = Bundle()
                                bundle.putString("mobile", edtMobileNo.text.toString())
                                bundle.putString("otp", it.otpval)
                                Navigation.findNavController(root)
                                    .navigate(R.id.nav_sigin2, bundle)
                            }
                        }
                    })
            }
        }
    }

    private fun getNumber() {
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()
        val intent = Credentials.getClient(requireActivity()).getHintPickerIntent(hintRequest)
        this.startIntentSenderForResult(
            intent.intentSender,
            RESOLVE_HINT, null, 0, 0, 0, null
        )
    }

    override fun onStop() {
        super.onStop()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val credential: Credential = data?.getParcelableExtra(Credential.EXTRA_KEY)!!
                number = credential.id.replace(oldValue = "+91", newValue = "")
                binding.edtMobileNo.setText(number)
//                apiCall(number)
            }
        }
    }

}