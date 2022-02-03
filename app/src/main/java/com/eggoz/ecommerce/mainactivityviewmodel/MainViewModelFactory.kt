package com.eggoz.ecommerce.mainactivityviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory(private val repository: MainRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java))
        {
            return MainViewModel(repository) as T
        }

        throw IllegalStateException("Unknown view Model Class")
    }
}