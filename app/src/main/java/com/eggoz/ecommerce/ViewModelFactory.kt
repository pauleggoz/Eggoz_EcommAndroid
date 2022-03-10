package com.eggoz.ecommerce

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eggoz.ecommerce.view.address.viewmodel.AddressRepository
import com.eggoz.ecommerce.view.address.viewmodel.AddressViewModel
import com.eggoz.ecommerce.view.cart.viewmodel.CartProductViewModel
import com.eggoz.ecommerce.view.cart.viewmodel.ProductRepository
import com.eggoz.ecommerce.view.contactus.viewmodel.ContactUsViewModel
import com.eggoz.ecommerce.view.contactus.viewmodel.ContactusRepository
import com.eggoz.ecommerce.view.faq.viewmodel.FaqRepository
import com.eggoz.ecommerce.view.faq.viewmodel.FaqViewModel
import com.eggoz.ecommerce.view.home.viewmodel.*
import com.eggoz.ecommerce.view.order.viewmodel.OrderListRepository
import com.eggoz.ecommerce.view.order.viewmodel.OrderListViewModel
import com.eggoz.ecommerce.view.order.viewmodel.OrderStatusRepository
import com.eggoz.ecommerce.view.order.viewmodel.OrderStatusViewModel
import com.eggoz.ecommerce.view.profile.viewModel.ProfileRepository
import com.eggoz.ecommerce.view.profile.viewModel.ProfileViewModel
import com.eggoz.ecommerce.view.referAndEarn.viewmodel.ReferAndEarnRepository
import com.eggoz.ecommerce.view.referAndEarn.viewmodel.ReferAndEarnViewModel
import com.eggoz.ecommerce.view.referAndEarn.viewmodel.UpdateReferEarnRepository
import com.eggoz.ecommerce.view.referAndEarn.viewmodel.UpdateReferEarnViewModel
import com.eggoz.ecommerce.view.wallet.viewmodel.WalletRepository
import com.eggoz.ecommerce.view.wallet.viewmodel.WalletViewModel

class ViewModelFactory<T>(val repository: T) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when (repository) {
            is HomeRepository -> {
                if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                    return HomeViewModel(repository) as T
                }
            }
            is ProfileRepository -> {
                if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                    return ProfileViewModel(repository) as T
                }
            }
            is DetailDateRepository -> {
                if (modelClass.isAssignableFrom(DateDetailViewModel::class.java)) {
                    return DateDetailViewModel(repository) as T
                }
            }
            is MyCalendarRepository -> {
                if (modelClass.isAssignableFrom(MyCalendarViewModel::class.java)) {
                    return MyCalendarViewModel(repository) as T
                }
            }
            is OrderStatusRepository -> {
                if (modelClass.isAssignableFrom(OrderStatusViewModel::class.java)) {
                    return OrderStatusViewModel(repository) as T
                }
            }
            is OrderListRepository -> {
                if (modelClass.isAssignableFrom(OrderListViewModel::class.java)) {
                    return OrderListViewModel(repository) as T
                }
            }
            is WalletRepository -> {
                if (modelClass.isAssignableFrom(WalletViewModel::class.java)) {
                    return WalletViewModel(repository) as T
                }
            }
            is ReferAndEarnRepository -> {
                if (modelClass.isAssignableFrom(ReferAndEarnViewModel::class.java)) {
                    return ReferAndEarnViewModel(repository) as T
                }
            }
            is AddressRepository -> {
                if (modelClass.isAssignableFrom(AddressViewModel::class.java)) {
                    return AddressViewModel(repository) as T
                }
            }
            is ProductRepository -> {
                if (modelClass.isAssignableFrom(CartProductViewModel::class.java)) {
                    return CartProductViewModel(repository) as T
                }
            }
            is UpdateReferEarnRepository -> {
                if (modelClass.isAssignableFrom(UpdateReferEarnViewModel::class.java)) {
                    return UpdateReferEarnViewModel(repository) as T
                }
            }
            is ContactusRepository -> {
                if (modelClass.isAssignableFrom(ContactUsViewModel::class.java)) {
                    return ContactUsViewModel(repository) as T
                }
            }
            is FaqRepository -> {
                if (modelClass.isAssignableFrom(FaqViewModel::class.java)) {
                    return FaqViewModel(repository) as T
                }
            }
        }
        throw IllegalStateException("Unknown view Model Class")
    }
}