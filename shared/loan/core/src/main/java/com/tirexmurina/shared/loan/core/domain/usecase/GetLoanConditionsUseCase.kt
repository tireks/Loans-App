package com.tirexmurina.shared.loan.core.domain.usecase

import com.tirexmurina.shared.loan.core.data.models.LoanConditionsModel
import com.tirexmurina.shared.loan.core.domain.repository.LoanRepository

class GetLoanConditionsUseCase(
    private val repository: LoanRepository
) {
    suspend operator fun invoke() : LoanConditionsModel = repository.getLoanConditions()
}