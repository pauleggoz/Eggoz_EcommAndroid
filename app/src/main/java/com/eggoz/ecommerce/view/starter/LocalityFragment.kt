package com.eggoz.ecommerce.view.starter

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentLocalityBinding
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.MainViewModel
import com.eggoz.ecommerce.view.starter.adapter.LocAdapter
import com.eggoz.ecommerce.view.starter.callback.LocCallback
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LocalityFragment : Fragment(), LocCallback {


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
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mid = this.arguments?.getInt("id", -1) ?: -1
        userPreferences = UserPreferences(requireContext())

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

            recloc.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                itemAnimator = DefaultItemAnimator()
                isNestedScrollingEnabled = false
            }


            btnSubmit.setOnClickListener {
                if (locid != -1) {
                    lifecycleScope.launch {
                        userPreferences!!.saveloc(loc = locid)
                        Navigation.findNavController(root)
                            .navigate(R.id.nav_sigin1)
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

            viewModel.getLocality(id = id)
            viewModel.responselocality.observe(viewLifecycleOwner, {

                if (dialog.isShowing())
                    dialog.dismiss()

                if (it?.errorType != null) {
                    Toast.makeText(requireContext(), it.errorType, Toast.LENGTH_SHORT).show()

                } else {

                    if (it?.cities != null) {
                        if (it.cities!![0].ecommerceSectors != null) {
                            locid = it.cities!![0].ecommerceSectors!![0].id ?: -1
                            locAdapter = LocAdapter(
                                it.cities!![0].ecommerceSectors!!,
                                contextloc = this@LocalityFragment
                            )
                            binding.apply {
                                recloc.adapter = locAdapter
                                (recloc.adapter as LocAdapter).notifyDataSetChanged()
                            }
                        }
                    }

                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun locId(id: Int?) {
        locid = id ?: -1
    }

}