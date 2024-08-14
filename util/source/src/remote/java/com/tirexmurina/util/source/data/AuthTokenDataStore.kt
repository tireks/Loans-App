package com.tirexmurina.util.source.data

import android.content.SharedPreferences

class AuthTokenDataStore(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        const val ACCESS_TOKEN_KEY = "access_token"
    }

    fun getAccessToken(): String? = sharedPreferences.getString(ACCESS_TOKEN_KEY, null)

    fun setAccessToken(accessToken: String) {
        sharedPreferences.edit().putString(ACCESS_TOKEN_KEY, accessToken).apply()
    }

    fun isAccessTokenSet(): Boolean {
        val token = getAccessToken()
        return !token.isNullOrEmpty()
    }

    fun clearAccessToken() {
        sharedPreferences.edit().remove(ACCESS_TOKEN_KEY).apply()

    }

}