package com.eggoz.ecommerce.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_pref")

class UserPreferences(context: Context) {
    private val applicationcontext = context.applicationContext

    val authtoken: Flow<String?>
        get() = applicationcontext.dataStore.data.cancellable().map { preferences ->
            preferences[KEy_Auth]
        }
    val userid: Flow<Int?>
        get() = applicationcontext.dataStore.data.cancellable().map { preferences ->
            preferences[User_id]
        }
    val Customer_id: Flow<Int?>
        get() = applicationcontext.dataStore.data.cancellable().map { preferences ->
            preferences[Cust_id]
        }
    val city: Flow<Int?>
        get() = applicationcontext.dataStore.data.cancellable().map { preferences ->
            preferences[KEy_city]
        }

    val loc: Flow<Int?>
        get() = applicationcontext.dataStore.data.cancellable().map { preferences ->
            preferences[KEy_loc]
        }

    suspend fun saveAuthtoke(token: String) {
        applicationcontext.dataStore.edit { transform ->
            transform[KEy_Auth] = token
        }
    }
    suspend fun saveCustomerid(id: Int) {
        applicationcontext.dataStore.edit { transform ->
            transform[Cust_id] = id
        }
    }
    suspend fun saveUserid(id: Int) {
        applicationcontext.dataStore.edit { transform ->
            transform[User_id] = id
        }
    }

    suspend fun clear() {
        applicationcontext.dataStore.edit {
            clear()
        }
    }

    suspend fun saveciy(city: Int) {
        applicationcontext.dataStore.edit { transform ->
            transform[KEy_city] = city
        }
    }

    suspend fun saveloc(loc: Int) {
        applicationcontext.dataStore.edit { transform ->
            transform[KEy_loc] = loc
        }
    }


    companion object {
        private val KEy_Auth = stringPreferencesKey(name = "key_auth")
        private val User_id = intPreferencesKey(name = "user_id")
        private val Cust_id = intPreferencesKey(name = "Cust_id")
        private val KEy_city = intPreferencesKey(name = "city")
        private val KEy_loc = intPreferencesKey(name = "loc")
    }
}