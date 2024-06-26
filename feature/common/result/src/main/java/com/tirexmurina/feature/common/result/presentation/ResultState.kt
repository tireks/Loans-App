package com.tirexmurina.feature.common.result.presentation

sealed interface ResultState {
    data object Initial : ResultState

    sealed interface Content : ResultState {

        data object Success : Content

        data object Fault : Content

    }
}