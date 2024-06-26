package com.tirexmurina.feature.common.help.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HelpViewModel(
    private val router: HelpRouter
) : ViewModel() {

    private val _state = MutableLiveData<HelpState>(HelpState.Initial)
    val state: LiveData<HelpState> = _state

    fun startScreen(){
        _state.value = HelpState.Content
    }

    fun openHome(){
        router.openHome()
    }


}