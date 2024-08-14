package com.tirexmurina.shared.user.core.domain.usecase

import com.tirexmurina.shared.user.core.domain.repository.UserRepository

class ClearTokenUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke() = repository.clearSession()
}