package com.tirexmurina.shared.loan.core.domain.entity

import java.time.LocalDateTime

//вообще конечно по-хорошему, надо бы получать ретрофитом модельку, а потом уже конвертить в entity, но не плодим сущности
data class Loan(
    val id : Int,
    val amount : Int,
    val date : LocalDateTime,
    val firstName : String,
    val lastName : String,
    val phoneNumber : String,
    val percent : Double,
    val period : Int,
    val state: LoanStatus
)
