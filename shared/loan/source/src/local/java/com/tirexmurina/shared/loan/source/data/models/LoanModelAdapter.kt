package com.tirexmurina.shared.loan.source.data.models

import com.tirexmurina.shared.loan.core.data.models.LoanRequestModel
import com.tirexmurina.shared.loan.core.domain.entity.Loan
import com.tirexmurina.shared.loan.core.domain.entity.LoanStatus
import java.time.LocalDateTime
import kotlin.random.Random

class LoanModelAdapter {

    fun convert(from: LoanLocalModel): Loan =
        with(from) {
            Loan(
                id = loanId,
                amount = amount,
                date = date,
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber,
                percent = percent,
                period = period,
                state = state
            )
        }

    fun generate(from: LoanRequestModel, userId: Int): LoanLocalModel =
        with(from) {
            LoanLocalModel(
                userLinkedId = userId,
                amount = amount,
                date = LocalDateTime.now(),
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber,
                percent = percent,
                period = period,
                state = getRandomLoanStatus()
            )
        }

    private fun getRandomLoanStatus(): LoanStatus {
        val statuses = LoanStatus.entries.toTypedArray()
        return statuses[Random.nextInt(statuses.size)]
    }
}