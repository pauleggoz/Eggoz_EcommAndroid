package com.eggoz.ecommerce.view.home.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentDateDetailBinding
import com.eggoz.ecommerce.databinding.FragmentHomeBinding
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.home.viewmodel.*
import com.eggoz.ecommerce.view.web.viewmodel.WebSearchViewModel


class DateDetailFragment : Fragment() {
    private lateinit var binding: FragmentDateDetailBinding
    private lateinit var viewModel: DateDetailViewModel

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
        val viewmodelFat = DetailDateViewModelFactory(repository)

        viewModel = ViewModelProvider(this,viewmodelFat)[DateDetailViewModel::class.java]
        this.arguments.let {
            viewModel.date=it?.getString("date", "") ?: ""
        }

        viewModel.orderlist().observe(viewLifecycleOwner,  {
            if (loadingdialog.isShowing())
                loadingdialog.dismiss()
            it.results?.let {
                binding.apply {
                    NoOrder.visibility=View.GONE
                    layoutData.visibility=View.VISIBLE
                }
            }

        })

        Log.d("TAG", "date: ${viewModel.date}")
    }

}