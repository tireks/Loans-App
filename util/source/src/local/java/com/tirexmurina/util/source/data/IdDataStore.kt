package com.tirexmurina.util.source.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class IdDataStore(
    context: Context
) {
    companion object {
        private const val ENCRYPTED_PREFS_NAME = "encrypted_prefs"
        const val LOCAL_USER_ID = "local_user_id"
    }

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        ENCRYPTED_PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getUserId(): String? = sharedPreferences.getString(LOCAL_USER_ID, null)

    fun setUserId(userId: String) {
        sharedPreferences.edit().putString(LOCAL_USER_ID, userId).apply()
    }

    fun isUserIdSaved(): Boolean {
        val id = getUserId()
        return !id.isNullOrEmpty()
    }

    fun clearUserId() {
        sharedPreferences.edit().remove(LOCAL_USER_ID).apply()
    }
}