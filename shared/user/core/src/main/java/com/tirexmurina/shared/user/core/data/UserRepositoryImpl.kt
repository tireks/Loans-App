package com.tirexmurina.shared.user.core.data

import com.tirexmurina.shared.user.core.data.local.SharedPreferencesImpl
import com.tirexmurina.shared.user.core.data.local.SharedPrefsCorruptedException
import com.tirexmurina.shared.user.core.data.models.AuthModel
import com.tirexmurina.shared.user.core.data.remote.AuthService
import com.tirexmurina.shared.user.core.data.remote.ForbiddenException
import com.tirexmurina.shared.user.core.data.remote.NetworkFault
import com.tirexmurina.shared.user.core.data.remote.NotFoundException
import com.tirexmurina.shared.user.core.data.remote.RequestFault
import com.tirexmurina.shared.user.core.data.remote.ResponseFault
import com.tirexmurina.shared.user.core.data.remote.UnauthorizedException
import com.tirexmurina.shared.user.core.domain.repository.UserRepository
import com.tirexmurina.util.core.exeptions.SuccessfulThrowable
import com.tirexmurina.util.core.exeptions.UnsuccessfulException
import retrofit2.Response
import java.io.IOException

class UserRepositoryImpl(
    private val authService: AuthService,
    private val sharedPreferencesImpl: SharedPreferencesImpl
) : UserRepository {
    override suspend fun register(authModel: AuthModel) {
        val response = try {
            authService.register(authModel)
        } catch (exception: IOException) {
            throw NetworkFault("Network error")
        } catch (exception: Exception) {
            throw RequestFault("Request went wrong")
        }

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                throw SuccessfulThrowable("Register Successful")
            } else {
                throw UnsuccessfulException("Response body is null")
            }
        } else {
            handleErrorResponse(response)
        }
    }

    override suspend fun login(authModel: AuthModel) {
        val response = try {
            authService.login(authModel)
        } catch (exception: IOException) {
            throw NetworkFault("Network error")
        } catch (exception: Exception) {
            throw RequestFault("Request went wrong")
        }

        if (response.isSuccessful) {
            val body = response.body()?.string()
            if (!body.isNullOrEmpty()) {
                throw SuccessfulThrowable("Login successful")
            } else {
                throw UnsuccessfulException("Empty response body")
            }
        } else {
            handleErrorResponse(response)
        }
    }

    override suspend fun tokenAvailable(): Boolean {
        return try{
            sharedPreferencesImpl.isAccessTokenSet()
        } catch ( exception : Exception ){
            throw SharedPrefsCorruptedException("some problems with sharedprefs")
        }
    }

    override suspend fun clearToken() {
        try {
            sharedPreferencesImpl.clearAccessToken()
        } catch ( exception : Exception  ) {
            throw SharedPrefsCorruptedException("some problems with sharedprefs")
        }
    }

    private fun <T> handleErrorResponse(response: Response<T>): Nothing {
        val errorMessage = response.errorBody()?.string()
        when (response.code()) {
            401 -> throw UnauthorizedException("Unauthorized: ${response.code()} $errorMessage")
            403 -> throw ForbiddenException("Forbidden: ${response.code()} $errorMessage")
            404 -> throw NotFoundException("Not Found: ${response.code()} $errorMessage")
            else -> throw ResponseFault("Something went wrong with response: ${response.code()} $errorMessage")
        }
    }

}