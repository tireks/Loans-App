package com.tirexmurina.shared.loan.core.domain.usecase

import com.tirexmurina.shared.loan.core.data.models.LoanRequestModel
import com.tirexmurina.shared.loan.core.domain.repository.LoanRepository

class RequestLoanUseCase(
    private val repository: LoanRepository
) {
    suspend operator fun invoke(loanRequest : LoanRequestModel) = repository.requestLoan(loanRequest)
}