package com.eggoz.ecommerce.view.Subscribe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.FragmentSubscribeBinding
import com.eggoz.ecommerce.databinding.FragmentSubscribeCostBinding

class SubscribeCostFragment : Fragment() {

    private var _binding: FragmentSubscribeCostBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscribeCostBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {

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