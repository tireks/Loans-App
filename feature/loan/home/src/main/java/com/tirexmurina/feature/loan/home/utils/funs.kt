package com.tirexmurina.feature.loan.home.utils

import android.content.Context
import com.tirexmurina.feature.loan.home.R
import com.tirexmurina.shared.loan.core.domain.entity.LoanStatus
import com.tirexmurina.util.features.fragments.getThemeColor

fun loanStateHelper(context: Context, state: LoanStatus): Pair<String, Int> {
    return when(state){
        LoanStatus.APPROVED -> "Одобрен" to getThemeColor(context, android.R.attr.colorPressedHighlight)
        LoanStatus.REGISTERED -> "На рассмотрении" to getThemeColor(context, android.R.attr.colorMultiSelectHighlight)
        LoanStatus.REJECTED -> "Отклонен" to getThemeColor(context, android.R.attr.colorFocusedHighlight)
    }
}