package com.tirexmurina.feature.loan.home.homeTab.requestScreen.presentation

sealed interface RequestState {

    data object Initial : RequestState

    data object Loading : RequestState

    sealed interface Content : RequestState {

        data class Locked(val troubleList: List<RequestField>) : Content

        data object Unlocked : Content

    }

    sealed interface Error : RequestState {

        data object LoanConditionsCannotFind : Error

        data object RequestFault : Error

        data object Unauthorized : Error

        data object Forbidden : Error

        data object NotFound : Error

        data object ResponseFault : Error

        data object NetworkFault : Error

        data object UnknownError : Error

    }

    data object LeavingScreen : RequestState



}