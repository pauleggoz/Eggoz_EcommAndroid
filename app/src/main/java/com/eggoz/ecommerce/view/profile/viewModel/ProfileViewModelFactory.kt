package com.eggoz.ecommerce.view.profile.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProfileViewModelFactory(private val repository: ProfileRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java))
        {
            return ProfileViewModel(repository) as T
        }

        throw IllegalStateException("Unknown view Model Class")
    }
}