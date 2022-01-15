package com.eggoz.ecommerce.view.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HomeViewModelFactory(private val repository: HomeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java))
        {
            return HomeViewModel(repository) as T
        }

        throw IllegalStateException("Unknown view Model Class")
    }
}