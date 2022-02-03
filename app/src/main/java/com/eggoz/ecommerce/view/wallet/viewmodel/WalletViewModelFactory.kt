package com.eggoz.ecommerce.view.wallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WalletViewModelFactory(private val repository: WalletRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WalletViewModel::class.java))
        {
            return WalletViewModel(repository) as T
        }

        throw IllegalStateException("Unknown view Model Class")
    }
}