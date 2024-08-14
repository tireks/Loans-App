package com.tirexmurina.shared.loan.source.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tirexmurina.shared.loan.core.domain.entity.LoanStatus
import java.time.LocalDateTime

@Entity(tableName = "loans")
data class LoanLocalModel(
    @PrimaryKey(autoGenerate = true) val loanId: Long = 0,
    val userLinkedId: Int,
    val amount: Int,
    val date: LocalDateTime,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val percent: Double,
    val period: Int,
    val state: LoanStatus
)
