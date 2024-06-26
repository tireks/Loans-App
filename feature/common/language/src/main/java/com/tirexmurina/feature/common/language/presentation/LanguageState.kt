package com.tirexmurina.feature.common.language.presentation

sealed interface LanguageState {
    data object Initial : LanguageState

    data object Content : LanguageState
}