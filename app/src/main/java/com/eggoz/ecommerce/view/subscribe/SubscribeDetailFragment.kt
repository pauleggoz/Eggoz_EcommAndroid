package com.eggoz.ecommerce.view.subscribe

import android.os.Bundle
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
import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentSubscribeDetailBinding
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.subscribe.adapter.SubscribeAdapter
import kotlinx.coroutines.launch


class SubscribeDetailFragment : Fragment() {
    private var _binding: FragmentSubscribeDetailBinding? = null
    private val binding get() = _binding!!


    private var userPreferences: UserPreferences? = null
    private lateinit var viewModel: SubscribeViewModel
    private lateinit var dialog: Loadinddialog
    private lateinit var adapter: SubscribeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscribeDetailBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }
    private fun initView(){

        viewModel = ViewModelProvider(this).get(SubscribeViewModel::class.java)
        dialog = Loadinddialog()
        userPreferences = UserPreferences(requireContext())

        binding.apply {
            recSubscription.apply {
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(requireContext())
                itemAnimator = DefaultItemAnimator()
                isNestedScrollingEnabled = false
            }
            btnBack.setOnClickListener {
                Navigation.findNavController(binding.root)
                    .popBackStack()
            }
        }
        getSubscribe()

    }
    private fun getSubscribe(){


        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {
            viewModel.getSubscribe(requireContext())
        }
        viewModel.responseSubscribe.observe(viewLifecycleOwner) {

            if (dialog.isShowing())
                dialog.dismiss()
            if (it?.errorType != null) {
                if (it.errors != null)
                    if (it.errors!![0].message == "Signature has expired.")
                        Navigation.findNavController(binding.recSubscription)
                            .navigate(R.id.action_nav_profile_to_nav_sigin1)

            } else {

                if (it?.results?.isNotEmpty() == true) {
                    adapter = SubscribeAdapter(it.results, this@SubscribeDetailFragment)
                    binding.recSubscription.adapter =
                        adapter
                    (binding.recSubscription.adapter as SubscribeAdapter).notifyDataSetChanged()
                }


            }
        }
    }
    override fun onStop() {
        super.onStop()
        _binding = null
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