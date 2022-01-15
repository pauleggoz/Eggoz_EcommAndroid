package com.eggoz.ecommerce.view.home.viewmodel

import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.room.RoomCart
import kotlinx.coroutines.flow.Flow

class HomeRepository(private var userPreferences: UserPreferences) {

    val city_id: Flow<Int?> by lazy { userPreferences.city }

    val user_id: Flow<Int?> by lazy { userPreferences.userid }

}