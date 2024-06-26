package com.tirexmurina.feature.user.start.presentation.bottomConrainer.login

sealed interface LoginState {

    data object Initial : LoginState

    sealed interface Content : LoginState {

        data class Locked(val troubleList: List<LoginInputField>) : Content

        data object Unlocked : Content
    }



}
