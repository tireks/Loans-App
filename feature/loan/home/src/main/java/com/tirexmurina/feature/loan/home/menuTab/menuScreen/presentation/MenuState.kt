package com.tirexmurina.feature.loan.home.menuTab.menuScreen.presentation


sealed interface MenuState {
    data object Initial : MenuState

    data object Content : MenuState

    sealed interface Exit : MenuState{

        data object Request : Exit

        data object CloseApp : Exit

    }

    data object LeavingScreen : MenuState

    data object ExitError : MenuState
}