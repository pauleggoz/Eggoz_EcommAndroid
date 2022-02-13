package com.eggoz.ecommerce.view.order.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eggoz.ecommerce.view.profile.viewModel.ProfileRepository
import com.eggoz.ecommerce.view.profile.viewModel.ProfileViewModel

class OrderListViewModelFactory(private val repository: OrderListRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderListViewModel::class.java))
        {
            return OrderListViewModel(repository) as T

        }

        throw IllegalStateException("Unknown view Model Class")
    }
}