package com.tirexmurina.shared.user.source.data

import com.tirexmurina.shared.user.core.data.TokenEmptyException
import com.tirexmurina.util.source.data.AuthTokenDataStore
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject

class TokenGetInterceptor(private val authTokenDataStore: AuthTokenDataStore) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val url = request.url
        val pathSegments = url.pathSegments
        if (url.toString().endsWith("/login")) {
            return handleLoginRequest(chain, request)
        }
        if (pathSegments.lastOrNull() in listOf(
                "loans",
                "all",
                "conditions"
            ) || pathSegments.getOrNull(pathSegments.size - 2) == "loans"
        ) {
            return handleAuthenticatedRequest(chain, request)
        }

        return chain.proceed(request)

    }

    private fun handleLoginRequest(chain: Interceptor.Chain, request: Request): Response {
        val response = chain.proceed(request)
        if (response.isSuccessful) {
            val responseBody = response.body
            val contentType = responseBody?.contentType()
            val content = responseBody?.string()?.trim()

            if (!content.isNullOrEmpty()) {
                authTokenDataStore.setAccessToken(content)
                val jsonObject = JSONObject()
                jsonObject.put("token", content)
                val jsonContent = jsonObject.toString()
                val newResponseBody = jsonContent.toResponseBody(contentType)
                return response.newBuilder().body(newResponseBody).build()
            }
        }
        return response
    }

    private fun handleAuthenticatedRequest(chain: Interceptor.Chain, request: Request): Response {
        val token =
            authTokenDataStore.getAccessToken() ?: throw TokenEmptyException("No token found")
        val newRequest = request.newBuilder()
            .addHeader("Authorization", token)
            .build()
        val response = chain.proceed(newRequest)
        return response
    }
}