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
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentSelectCityBinding
import com.eggoz.ecommerce.network.model.CityData
import com.eggoz.ecommerce.room.RoomCart
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.MainViewModel
import com.eggoz.ecommerce.view.starter.adapter.CityAdapter
import kotlinx.coroutines.launch

class SelectCityFragment : Fragment() {

    private var _binding: FragmentSelectCityBinding? = null
    private val binding get() = _binding!!

    private lateinit var cityadapter: CityAdapter
    private lateinit var viewModel: MainViewModel
    private var userPreferences: UserPreferences? = null
    private lateinit var dialog: Loadinddialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectCityBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        dialog = Loadinddialog()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        userPreferences = UserPreferences(requireContext())

        getCity()
        binding.apply {
            root.isFocusableInTouchMode = true
            root.requestFocus()
            root.setOnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                    return@setOnKeyListener true
                } else false
            }

          /*  reccity.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.HORIZONTAL, false)
                itemAnimator = DefaultItemAnimator()
                isNestedScrollingEnabled = false
            }*/
        }
    }

    private fun getCity() {
        lifecycleScope.launch {
            if (!dialog.isShowing())
                dialog.create(requireContext())

            cityadapter =
                CityAdapter { item: CityData.Result ->
                    lifecycleScope.launch {
                        userPreferences?.saveciy(city = item.id ?: -1)
                    }
                }
            binding.viewadapter=cityadapter

            viewModel.getCity().observe(viewLifecycleOwner,
                {

                    if (dialog.isShowing())
                        dialog.dismiss()
                    if (it?.errorType != null) {
                        Toast.makeText(requireContext(), it.errorType, Toast.LENGTH_SHORT).show()

                    } else {
                        it.let {
                            cityadapter.submitList(it.results)
                        }

                    }
                })
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}