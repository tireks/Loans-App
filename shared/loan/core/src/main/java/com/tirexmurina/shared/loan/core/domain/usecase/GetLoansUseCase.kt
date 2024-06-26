package com.tirexmurina.shared.loan.core.domain.usecase

import com.tirexmurina.shared.loan.core.domain.repository.LoanRepository

class GetLoansUseCase(
    private val repository: LoanRepository
) {
    suspend operator fun invoke() = repository.getLoans()
}