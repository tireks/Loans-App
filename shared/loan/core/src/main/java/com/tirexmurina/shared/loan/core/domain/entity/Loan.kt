package com.tirexmurina.shared.loan.core.domain.entity

import java.time.LocalDateTime

data class Loan(
    val id: Long,
    val amount: Int,
    val date: LocalDateTime,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val percent: Double,
    val period: Int,
    val state: LoanStatus
)
