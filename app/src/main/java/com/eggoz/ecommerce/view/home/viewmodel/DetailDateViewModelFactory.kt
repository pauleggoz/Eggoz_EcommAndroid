package com.eggoz.ecommerce.view.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DetailDateViewModelFactory(private val repository: DetailDateRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DateDetailViewModel::class.java))
        {
            return DateDetailViewModel(repository) as T
        }

        throw IllegalStateException("Unknown view Model Class")
    }
}