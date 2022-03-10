package com.eggoz.ecommerce.view.starter

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentLocalityBinding
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.MainViewModel
import com.eggoz.ecommerce.view.starter.adapter.LocAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LocalityFragment : Fragment() {


    private var _binding: FragmentLocalityBinding? = null
    private val binding get() = _binding!!

    private lateinit var locAdapter: LocAdapter

    private var mid: Int = -1
    private lateinit var viewModel: MainViewModel
    private var userPreferences: UserPreferences? = null
    private var locid = -1
    private lateinit var dialog: Loadinddialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocalityBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        dialog = Loadinddialog()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mid = this.arguments?.getInt("id", -1) ?: -1
        userPreferences = UserPreferences(requireContext())

        val auth_token: Flow<String?> by lazy { userPreferences!!.authtoken }

        if (mid == -1)
            userPreferencesdata()
        else
            getLocality(mid)

        binding.apply {

            root.isFocusableInTouchMode = true
            root.requestFocus()
            root.setOnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                    return@setOnKeyListener true
                } else false
            }
            locAdapter = LocAdapter { sector, current ->
                locid = sector?.id ?: -1

                viewModel.cities!![0].ecommerceSectors!![viewModel.selectedLoc].isSelected = false
                viewModel.cities!![0].ecommerceSectors!![current].isSelected = true
                viewModel.selectedLoc = current

                locAdapter.submitList(viewModel.cities!![0].ecommerceSectors)
            }
            viewlocAdapter = locAdapter


            btnSubmit.setOnClickListener {
                if (locid != -1) {
                    lifecycleScope.launch {
                        userPreferences!!.saveloc(loc = locid)
                        if ((auth_token.buffer().first() ?: "").isEmpty())
                            Navigation.findNavController(root)
                                .navigate(R.id.nav_sigin1)
                        else
                            Navigation.findNavController(binding.root)
                                .navigate(R.id.nav_home)
                    }
                } else {
                    Toast.makeText(requireContext(), "Select valid locality", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun userPreferencesdata() {
        lifecycleScope.launch {
            mid = userPreferences?.city?.buffer()?.first() ?: -1
            if (mid != -1)
                getLocality(mid)
        }
    }


    private fun getLocality(id: Int) {
        lifecycleScope.launch {
            if (!dialog.isShowing())
                dialog.create(requireContext())

            viewModel.getLocality(id = id).observe(viewLifecycleOwner) {

                if (dialog.isShowing())
                    dialog.dismiss()

                if (it?.errorType != null) {
                    Toast.makeText(requireContext(), it.errorType, Toast.LENGTH_SHORT).show()

                } else {
                    it?.cities?.let { city ->
                        viewModel.cities = city
                        viewModel.cities!![0].ecommerceSectors.let { ecomSec ->
                            if (ecomSec!!.isNotEmpty()) {
                                locid = ecomSec[0].id ?: -1
                                ecomSec[0].isSelected = true
                            }
                            locAdapter.submitList(ecomSec)

                        }

                    }


                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}