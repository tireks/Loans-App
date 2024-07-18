package com.tirexmurina.shared.user.core.domain.usecase

import com.tirexmurina.shared.user.core.domain.repository.UserRepository

class AskTokenAvailabilityUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke() : Boolean = repository.tokenAvailable()
}