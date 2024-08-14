package com.tirexmurina.shared.user.core.domain.usecase

import com.tirexmurina.shared.user.core.domain.repository.UserRepository

class AskSessionAvailabilityUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Boolean = repository.sessionAvailable()
}