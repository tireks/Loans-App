package com.tirexmurina.feature.common.language.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LanguageViewModel(
    private val router: LanguageRouter
) : ViewModel() {

    private val _state = MutableLiveData<LanguageState>(LanguageState.Initial)
    val state: LiveData<LanguageState> = _state

    fun startScreen(){
        _state.value = LanguageState.Content
    }

    fun openHome(){
        router.openHome()
    }

}