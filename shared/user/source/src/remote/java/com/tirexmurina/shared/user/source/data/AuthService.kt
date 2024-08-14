package com.tirexmurina.shared.user.source.data

import com.tirexmurina.shared.user.core.data.models.AuthModel
import com.tirexmurina.shared.user.core.domain.entity.User
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/registration")
    suspend fun register(@Body auth: AuthModel): Response<User>

    @POST("/login")
    suspend fun login(@Body auth: AuthModel): Response<ResponseBody>

}