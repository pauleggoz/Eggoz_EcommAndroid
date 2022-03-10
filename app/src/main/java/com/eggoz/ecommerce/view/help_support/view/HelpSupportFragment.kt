package com.eggoz.ecommerce.view.help_support.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.FragmentFaqBinding
import com.eggoz.ecommerce.databinding.FragmentHelpSupportBinding
import com.eggoz.ecommerce.view.help_support.adapter.FaqAdapter
import com.google.android.material.internal.NavigationMenu


class HelpSupportFragment : Fragment() {

    private var _binding: FragmentHelpSupportBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter:FaqAdapter
    private val list = listOf("nams","nams","nams","nams","nams","nams","nams","nams","nams","nams","nams","nams","nams","nams")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpSupportBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView(){
        adapter= FaqAdapter()
        binding.apply {
            faqAdapter=adapter

            adapter.submitList(list)

            btnSendmessage.setOnClickListener {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_nav_help_support_to_nav_faq)
            }

        }

    }

}