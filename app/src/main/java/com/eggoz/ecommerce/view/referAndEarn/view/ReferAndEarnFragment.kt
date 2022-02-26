package com.eggoz.ecommerce.view.referAndEarn.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.ViewModelFactory
import com.eggoz.ecommerce.databinding.FragmentProductDetailBinding
import com.eggoz.ecommerce.databinding.FragmentReferAndEarnBinding
import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.address.viewmodel.AddressViewModel
import com.eggoz.ecommerce.view.referAndEarn.viewmodel.ReferAndEarnRepository
import com.eggoz.ecommerce.view.referAndEarn.viewmodel.ReferAndEarnViewModel
import com.eggoz.ecommerce.view.wallet.viewmodel.WalletRepository
import com.eggoz.ecommerce.view.wallet.viewmodel.WalletViewModel


class ReferAndEarnFragment : Fragment() {

    private var _binding: FragmentReferAndEarnBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReferAndEarnBinding.inflate(inflater, container, false)
        init()

        return binding.root
    }

    private fun init() {
        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(binding.root)
                    .popBackStack()
            }
            btnReferNow.setOnClickListener {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_nav_referearn_to_nav_referearnwithfriends)
            }
        }

    }



}