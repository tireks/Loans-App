package com.tirexmurina.shared.loan.source.data

import com.tirexmurina.shared.loan.core.data.models.LoanConditionsModel
import kotlin.random.Random

class LoanConditionsGenerator {

    fun generate(): LoanConditionsModel {
        val maxAmountRandom =
            1000.0 + Random.nextInt(((50000.0 - 1000.0) / 500.0).toInt() + 1) * 500.0
        val percentRandom = 10.0 + Random.nextInt(((35.0 - 10.0) / 0.01).toInt() + 1) * 0.01
        val periodRandom = Random.nextInt(14, 60 + 1)
        return LoanConditionsModel(
            maxAmount = maxAmountRandom,
            percent = percentRandom,
            period = periodRandom
        )
    }

}