package com.eggoz.ecommerce.view.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentManageAdderssesBinding
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.profile.adapter.ManageAdderssesAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class ManageAddressFragment : Fragment(), ManageAdderssesAdapter.callback {

    private var _binding: FragmentManageAdderssesBinding? = null
    private val binding get() = _binding!!

    private var userPreferences: UserPreferences? = null
    private var userid: Int = -1
    private lateinit var viewModel: ProfileViewModel
    private lateinit var dialog: Loadinddialog
    private lateinit var adapter: ManageAdderssesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageAdderssesBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        userPreferences = UserPreferences(requireContext())
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        dialog = Loadinddialog()
        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(binding.root)
                    .popBackStack()
            }
            recManageAdd.apply {
                layoutManager = LinearLayoutManager(
                    activity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                itemAnimator = DefaultItemAnimator()
                isNestedScrollingEnabled = false
            }
            btnAddAddress.setOnClickListener {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_nav_manageAddress_to_nav_inputAddress)
            }
        }
        lifecycleScope.launch {
            userid = userPreferences!!.Customer_id.first() ?: -1
            if (userid != -1) {
                getUser()
            }
        }
    }

    private fun getUser() {


        if (!dialog.isShowing())
            dialog.create(requireContext())
        Log.d("data", "id$userid")
        lifecycleScope.launch {
            viewModel.user(userid, requireContext())
        }
        viewModel.responUser.observe(viewLifecycleOwner, {

            if (dialog.isShowing())
                dialog.dismiss()
            if (it?.errorType != null) {


            } else {
                if (it.addresses != null)

                    binding.apply {
                        adapter = ManageAdderssesAdapter(
                            result = it.addresses,
                            this@ManageAddressFragment
                        )
                        binding.apply {
                            recManageAdd.adapter = adapter
                            (recManageAdd.adapter as ManageAdderssesAdapter).notifyDataSetChanged()
                        }
                    }
            }
        })
    }

    override fun deleteAddress(id: Int) {
        Log.d("data", "id $id")

        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {
            viewModel.deleteAddress(id, requireContext())
        }
        viewModel.responOtpgenerate.observe(viewLifecycleOwner, {
            if (it.errorType == null) {
                getUser()
            }
            if (dialog.isShowing())
                dialog.dismiss()
        })
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