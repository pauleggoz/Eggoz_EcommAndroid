package com.eggoz.ecommerce.view.profile.viewModel

import com.eggoz.ecommerce.data.UserPreferences
import kotlinx.coroutines.flow.Flow

class ProfileRepository(private var userPreferences: UserPreferences) {

    val city_id: Flow<Int?> by lazy { userPreferences.city }

    val user_id: Flow<Int?> by lazy { userPreferences.userid }

    val auth_token: Flow<String?> by lazy { userPreferences.authtoken }

}