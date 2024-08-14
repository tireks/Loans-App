package com.tirexmurina.shared.user.source

import com.tirexmurina.shared.user.core.data.ForbiddenException
import com.tirexmurina.shared.user.core.data.NetworkFault
import com.tirexmurina.shared.user.core.data.NotFoundException
import com.tirexmurina.shared.user.core.data.RequestFault
import com.tirexmurina.shared.user.core.data.ResponseFault
import com.tirexmurina.shared.user.core.data.SharedPrefsCorruptedException
import com.tirexmurina.shared.user.core.data.UnauthorizedException
import com.tirexmurina.shared.user.core.data.models.AuthModel
import com.tirexmurina.shared.user.core.domain.repository.UserRepository
import com.tirexmurina.shared.user.source.data.AuthService
import com.tirexmurina.util.core.exeptions.UnsuccessfulException
import com.tirexmurina.util.source.data.AuthTokenDataStore
import retrofit2.Response
import java.io.IOException

class UserRepositoryImpl(
    private val authService: AuthService,
    private val authTokenDataStore: AuthTokenDataStore,
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
            if (response.body() == null) {
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
            if (body.isNullOrEmpty()) {
                throw UnsuccessfulException("Empty response body")
            }
        } else {
            handleErrorResponse(response)
        }
    }

    override suspend fun sessionAvailable(): Boolean {
        return try {
            authTokenDataStore.isAccessTokenSet()
        } catch (exception: Exception) {
            throw SharedPrefsCorruptedException("some problems with sharedprefs")
        }
    }

    override suspend fun clearSession() {
        try {
            authTokenDataStore.clearAccessToken()
        } catch (exception: Exception) {
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