package com.tirexmurina.feature.loan.home.homeTab.homeScreen.presentation

import com.tirexmurina.shared.loan.core.data.models.LoanConditionsModel
import com.tirexmurina.shared.loan.core.domain.entity.Loan

sealed interface HomeState {

    data object Initial : HomeState

    data object Loading : HomeState

    sealed interface Content : HomeState {
        data class NoLoans(val conditions: LoanConditionsModel) : Content

        data class GotLoans(val list: List<Loan>, val conditions: LoanConditionsModel) : Content
    }

    sealed interface Error : HomeState {

        data object LoanConditionsCannotFind : Error

        data object RequestFault : Error

        data object Unauthorized : Error

        data object Forbidden : Error

        data object NotFound : Error

        data object ResponseFault : Error

        data object NetworkFault : Error

        data object UnknownError : Error

    }

}