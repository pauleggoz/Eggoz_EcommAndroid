package com.eggoz.ecommerce.view.referAndEarn.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.FragmentInvitationSendBinding
import com.eggoz.ecommerce.databinding.FragmentReferWithFriendsBinding


class InvitationSendFragment : Fragment() {

    private var _binding: FragmentInvitationSendBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvitationSendBinding.inflate(inflater, container, false)
        init()

        return binding.root
    }

    private fun init(){
        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(binding.root)
                    .popBackStack()
            }

        }
    }
}