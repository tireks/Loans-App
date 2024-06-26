package com.tirexmurina.feature.loan.home.menuTab.loansScreen.presentation

import com.tirexmurina.shared.loan.core.domain.entity.Loan

sealed interface LoansState {

    data object Initial : LoansState

    data object Loading : LoansState

    data class Content(val contentList: List<Loan>) : LoansState

    sealed interface Error : LoansState {
        data object RequestFault : Error

        data object Unauthorized : Error

        data object Forbidden : Error

        data object NotFound : Error

        data object ResponseFault :  Error

        data object NetworkFault :  Error

        data object UnknownError :  Error
    }
}