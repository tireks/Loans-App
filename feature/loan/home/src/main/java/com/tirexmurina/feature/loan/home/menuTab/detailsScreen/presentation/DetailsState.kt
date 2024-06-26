package com.tirexmurina.feature.loan.home.menuTab.detailsScreen.presentation

import com.tirexmurina.shared.loan.core.domain.entity.Loan

sealed interface DetailsState {

    data object Initial : DetailsState

    data object Loading : DetailsState

    data class Content(val loan : Loan) : DetailsState

    sealed interface Error : DetailsState {

        data object SingleLoanCannotFind : Error

        data object RequestFault : Error

        data object Unauthorized : Error

        data object Forbidden : Error

        data object NotFound : Error

        data object ResponseFault : Error

        data object NetworkFault : Error

        data object UnknownError : Error

    }

}