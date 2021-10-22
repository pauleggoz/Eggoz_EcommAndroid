package com.eggoz.ecommerce.view.starter

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.FragmentStarterBinding


class StarterFragment : Fragment() {

    private var _binding: FragmentStarterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStarterBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }
    private fun init(){
        binding.root.isFocusableInTouchMode = true
        binding.root.requestFocus()
        binding.root.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                return@setOnKeyListener true
            } else false
        }

        binding.btnSubmit.setOnClickListener {
            Navigation.findNavController(binding.root)
            .navigate(R.id.nav_sigin1) }
    }

    override fun onStop() {
        super.onStop()
        _binding=null
    }
}