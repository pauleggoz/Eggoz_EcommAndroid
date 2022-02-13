package com.eggoz.ecommerce.view.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.eggoz.ecommerce.ViewModelFactory
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentDateDetailBinding
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.home.viewmodel.DateDetailViewModel
import com.eggoz.ecommerce.view.home.viewmodel.*
import com.eggoz.ecommerce.view.order.adapter.OrderListAdapter


class DateDetailFragment : Fragment() {
    private lateinit var binding: FragmentDateDetailBinding
    private lateinit var viewModel: DateDetailViewModel
    private lateinit var adapter: OrderListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { binding = FragmentDateDetailBinding.inflate(inflater, container, false)

        init()
        return binding.root
    }
    private fun init(){
        val loadingdialog = Loadinddialog()
        loadingdialog.create(requireContext())

        val userPreferences = UserPreferences(requireContext())
        val repository = DetailDateRepository(userPreferences)
        val viewmodelFat = ViewModelFactory(repository)

        viewModel = ViewModelProvider(this,viewmodelFat)[DateDetailViewModel::class.java]
        adapter=OrderListAdapter()

        this.arguments.let {
            viewModel.date=it?.getString("date", "") ?: ""
        }
        binding.apply {
            viewOrderadapter=adapter
        }

        viewModel.orderlist().observe(viewLifecycleOwner,  {
            if (loadingdialog.isShowing())
                loadingdialog.dismiss()
            adapter.submitList(it.results)
            it.results?.let { listOrder->
                binding.apply {
                    NoOrder.visibility=View.GONE
                    layoutData.visibility=View.VISIBLE
                }
            }
            if (it.results==null){
                binding.apply {
                    NoOrder.visibility=View.VISIBLE
                    layoutData.visibility=View.GONE
                }
            }

        })
    }

}