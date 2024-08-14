package com.tirexmurina.shared.loan.core.data.models

data class LoanRequestModel (
    val amount: Int,
    val firstName: String,
    val lastName: String,
    val percent: Double,
    val period: Int,
    val phoneNumber: String
)
