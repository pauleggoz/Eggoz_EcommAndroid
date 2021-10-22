package com.eggoz.ecommerce.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.io.IOException
import java.security.GeneralSecurityException

class PrefrenceUtils {

    companion object {
        var isPreviousFrmMap = false
        lateinit var masterKeyAlias: String
        lateinit var sharedPreferences: SharedPreferences

        // use the shared preferences and editor as you normally would
//    var editor:SharedPreferences.Editor = sharedPreferences.edit()

        fun getPrefs(context: Context): SharedPreferences {
            run {
                try {
                    masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
                    sharedPreferences = EncryptedSharedPreferences.create(
                        "secret_shared_prefs",
                        masterKeyAlias,
                        context,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                    )
                } catch (e: GeneralSecurityException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return sharedPreferences
        }

        fun insertData(context: Context, key: String, value: String?) {
            val editor = getPrefs(context).edit()
            editor.putString(key, value!!)
            editor.apply()
        }

        fun insertLongData(context: Context, key: String, value: Long) {
            val editor = getPrefs(context).edit()
            editor.putLong(key, value)
            editor.apply()
        }


        fun insertStringSet(context: Context, key: String, value: Set<String>) {
            getPrefs(context).edit().putStringSet(key, value).apply()
        }

        fun retriveData(context: Context, key: String): String? {
            return getPrefs(context).getString(key, "")
        }

        fun retriveLongData(context: Context, key: String): Long {
            return getPrefs(context).getLong(key, 0)
        }

        fun getStringSet(context: Context, key: String): MutableSet<String>? {
            return getPrefs(context).getStringSet(key, null)
        }

        fun deleteData(context: Context, key: String) {
            val editor = getPrefs(context).edit()
            editor.remove(key)
            editor.apply()
        }

        fun checkContains(ctx: Context, key: String): Boolean {
            return getPrefs(ctx).contains(key)
        }
    }
}