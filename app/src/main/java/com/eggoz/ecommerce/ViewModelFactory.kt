package com.eggoz.ecommerce

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eggoz.ecommerce.view.home.viewmodel.*
import com.eggoz.ecommerce.view.order.viewmodel.OrderListRepository
import com.eggoz.ecommerce.view.order.viewmodel.OrderListViewModel
import com.eggoz.ecommerce.view.order.viewmodel.OrderStatusRepository
import com.eggoz.ecommerce.view.order.viewmodel.OrderStatusViewModel
import com.eggoz.ecommerce.view.profile.viewModel.ProfileRepository
import com.eggoz.ecommerce.view.profile.viewModel.ProfileViewModel
import com.eggoz.ecommerce.view.referAndEarn.viewmodel.ReferAndEarnRepository
import com.eggoz.ecommerce.view.referAndEarn.viewmodel.ReferAndEarnViewModel
import com.eggoz.ecommerce.view.wallet.viewmodel.WalletRepository
import com.eggoz.ecommerce.view.wallet.viewmodel.WalletViewModel

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
        if (repository is OrderStatusRepository)
            if (modelClass.isAssignableFrom(OrderStatusViewModel::class.java)) {
                return OrderStatusViewModel(repository) as T
            }
        if (repository is OrderListRepository)
            if (modelClass.isAssignableFrom(OrderListViewModel::class.java)) {
                return OrderListViewModel(repository) as T
            }
        if (repository is WalletRepository)
            if (modelClass.isAssignableFrom(WalletViewModel::class.java)) {
                return WalletViewModel(repository) as T
            }
        if (repository is ReferAndEarnRepository)
            if (modelClass.isAssignableFrom(ReferAndEarnViewModel::class.java)) {
                return ReferAndEarnViewModel(repository) as T
            }
        throw IllegalStateException("Unknown view Model Class")
    }
}