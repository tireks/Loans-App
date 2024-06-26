package com.tirexmurina.shared.user.core.domain.usecase

import com.tirexmurina.shared.user.core.data.models.AuthModel
import com.tirexmurina.shared.user.core.domain.repository.UserRepository

class LoginUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(authModel: AuthModel) = repository.login(authModel)
}