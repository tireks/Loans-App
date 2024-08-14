package com.tirexmurina.shared.user.core.domain.repository

import com.tirexmurina.shared.user.core.data.models.AuthModel

interface UserRepository {

    suspend fun register(authModel: AuthModel)

    suspend fun login(authModel: AuthModel)

    suspend fun sessionAvailable(): Boolean

    suspend fun clearSession()

}