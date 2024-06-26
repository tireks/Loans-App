package com.tirexmurina.shared.loan.core.domain.usecase

import com.tirexmurina.shared.loan.core.domain.repository.LoanRepository

class GetLoansByIdUseCase(
    private val repository: LoanRepository
) {
    suspend operator fun invoke(id : Long) = repository.getLoanById(id)
}