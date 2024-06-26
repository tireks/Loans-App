package com.tirexmurina.feature.common.help.presentation

sealed interface HelpState {

    data object Initial : HelpState

    data object Content : HelpState
}