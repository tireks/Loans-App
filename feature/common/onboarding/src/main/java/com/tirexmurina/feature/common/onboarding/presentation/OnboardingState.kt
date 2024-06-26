package com.tirexmurina.feature.common.onboarding.presentation

sealed interface OnboardingState {

    data object Initial : OnboardingState

    data object ShowOnboarding : OnboardingState

}