package com.eggoz.ecommerce.view.address.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eggoz.ecommerce.view.home.viewmodel.HomeRepository
import com.eggoz.ecommerce.view.home.viewmodel.HomeViewModel

class AddressViewModelFactory(private val repository: AddressRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddressViewModel::class.java))
        {
            return AddressViewModel(repository) as T
        }

        throw IllegalStateException("Unknown view Model Class")
    }
}