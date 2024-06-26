package com.tirexmurina.shared.loan.core.domain.repository

import com.tirexmurina.shared.loan.core.data.models.LoanConditionsModel
import com.tirexmurina.shared.loan.core.data.models.LoanRequestModel
import com.tirexmurina.shared.loan.core.domain.entity.Loan

interface LoanRepository {

    suspend fun getLoans() : List<Loan>

    suspend fun getLoanById(id : Long) : Loan

    suspend fun getLoanConditions() : LoanConditionsModel

    suspend fun requestLoan(loan : LoanRequestModel)

}