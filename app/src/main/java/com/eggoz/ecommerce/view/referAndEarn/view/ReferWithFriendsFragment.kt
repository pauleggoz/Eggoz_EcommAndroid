package com.eggoz.ecommerce.view.referAndEarn.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.ViewModelFactory
import com.eggoz.ecommerce.databinding.FragmentReferWithFriendsBinding
import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.referAndEarn.viewmodel.ReferAndEarnRepository
import com.eggoz.ecommerce.view.referAndEarn.viewmodel.ReferAndEarnViewModel

class ReferWithFriendsFragment : Fragment() {

    private var _binding: FragmentReferWithFriendsBinding? = null
    private val binding get() = _binding!!


    private lateinit var dialog: Loadinddialog
    private lateinit var viewModel: ReferAndEarnViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReferWithFriendsBinding.inflate(inflater, container, false)
        init()

        return binding.root
    }

    private fun init() {
        dialog = Loadinddialog()


        val repository = ReferAndEarnRepository(UserPreferences(requireContext()))
        val viewmodelFat = ViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewmodelFat)[ReferAndEarnViewModel::class.java]

        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(binding.root)
                    .popBackStack()
            }
            crdUrl.setOnClickListener {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_nav_referearnwithfriends_to_nav_invitation_send)
            }
        }
        getReferId()
    }

    private fun getReferId() {
        if (!dialog.isShowing())
            dialog.create(requireContext())
        viewModel.referAndEarn.observe(viewLifecycleOwner) {
            if (dialog.isShowing())
                dialog.dismiss()
            it.customerReferralCode.let { rId->
                binding.txtReferId.text = rId
            }
        }
    }

}