package com.eggoz.ecommerce

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eggoz.ecommerce.view.home.viewmodel.*
import com.eggoz.ecommerce.view.profile.viewModel.ProfileRepository
import com.eggoz.ecommerce.view.profile.viewModel.ProfileViewModel

class ViewModelFactory<T>(val repository: T) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (repository is HomeRepository)
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(repository) as T
            }
        if (repository is ProfileRepository)
                if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                    return ProfileViewModel(repository) as T
                }
        if (repository is DetailDateRepository)
                if (modelClass.isAssignableFrom(DateDetailViewModel::class.java)) {
                    return DateDetailViewModel(repository) as T
                }
        if (repository is MyCalendarRepository)
                if (modelClass.isAssignableFrom(MyCalendarViewModel::class.java)) {
                    return MyCalendarViewModel(repository) as T
                }
        throw IllegalStateException("Unknown view Model Class")
    }
}