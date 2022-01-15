package com.eggoz.ecommerce.view.cart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CartViewModelFactory(private val repository: ProductRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartProductViewModel::class.java))
        {
            return CartProductViewModel(repository) as T
        }

        throw IllegalStateException("Unknown view Model Class")
    }
}