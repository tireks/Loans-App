package com.tirexmurina.feature.common.onboarding.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OnboardingViewModel(
    private val router: OnboardingRouter
) : ViewModel() {

    private val _state = MutableLiveData<OnboardingState>(OnboardingState.Initial)
    val state: LiveData<OnboardingState> = _state

    fun handleUserStatus(userIsNewbie : Boolean){
        if (userIsNewbie){
            _state.value = OnboardingState.ShowOnboarding
        } else {
            openHomeScreen()
        }
    }

    fun openHomeScreen() {
        router.openHome()
    }
}