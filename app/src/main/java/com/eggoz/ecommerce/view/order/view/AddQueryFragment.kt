package com.eggoz.ecommerce.view.order.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.eggoz.ecommerce.databinding.FragmentAddQueryBinding

class AddQueryFragment : Fragment() {
    private var _binding: FragmentAddQueryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddQueryBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }
    private fun init(){
        binding.apply {

        }
    }

}