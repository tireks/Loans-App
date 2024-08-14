package com.tirexmurina.feature.loan.home.menuTab.menuScreen.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tirexmurina.feature.loan.home.homeTab.homeScreen.presentation.HomeRouter
import com.tirexmurina.shared.user.core.data.IdSharedPrefsCorruptedException
import com.tirexmurina.shared.user.core.domain.usecase.ClearTokenUseCase
import kotlinx.coroutines.launch

class MenuViewModel(
    private val clearTokenUseCase: ClearTokenUseCase,
    private val router: HomeRouter
) : ViewModel() {

    private val _state = MutableLiveData<MenuState>(MenuState.Initial)
    val state: LiveData<MenuState> = _state

    fun showScreen(){
        _state.value = MenuState.Content
    }

    fun exitRequest(){
        _state.value = MenuState.Exit.Request
    }

    fun openOnboarding(){
        router.openOnboarding(true)
        _state.value = MenuState.LeavingScreen
    }

    fun openOffers(){
        router.openSpecialOfferScreen()
        _state.value = MenuState.LeavingScreen
    }

    fun openBranches(){
        router.openBranchesScreen()
        _state.value = MenuState.LeavingScreen
    }

    fun openHelp(){
        router.openHelpScreen()
        _state.value = MenuState.LeavingScreen
    }

    fun openLanguages(){
        router.openLanguageScreen()
        _state.value = MenuState.LeavingScreen
    }

    fun clearToken(){
        viewModelScope.launch {
            try {
                clearTokenUseCase()
                _state.value = MenuState.Exit.CloseApp
            } catch (sharedPrefsException: IdSharedPrefsCorruptedException) {
                _state.value = MenuState.ExitError
            }
        }

    }

}