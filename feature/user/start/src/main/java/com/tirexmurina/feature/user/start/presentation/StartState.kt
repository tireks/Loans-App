package com.tirexmurina.feature.user.start.presentation

sealed interface StartState {

    data object Initial : StartState

    data object Loading : StartState

    data object Content : StartState

    sealed interface Error : StartState {

        data object SharedPrefsCorrupted : Error

        data object RequestFault : Error

        data object Unauthorized : Error

        data object Forbidden : Error

        data object NotFound : Error

        data object ResponseFault : Error

        data object NetworkFault : Error

        data object UnknownError : Error

    }

}