package com.tirexmurina.feature.user.start.presentation.bottomConrainer.registration

sealed interface RegistrationState {

    data object Initial : RegistrationState

    sealed interface Content : RegistrationState {

        data class Locked(val troubleList: List<RegisterInputField>) : Content

        data object Unlocked : Content
    }
}

