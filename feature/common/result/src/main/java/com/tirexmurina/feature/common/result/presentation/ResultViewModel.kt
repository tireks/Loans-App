package com.tirexmurina.feature.common.result.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResultViewModel(
    private val router: ResultRouter
) : ViewModel() {

    private val _state = MutableLiveData<ResultState>(ResultState.Initial)
    val state: LiveData<ResultState> = _state

    fun showResult(successResult : Boolean){
        if (successResult){
            _state.value = ResultState.Content.Success
        } else {
            _state.value = ResultState.Content.Fault
        }
    }

    fun openBranches(){
        router.openBranches()
    }

    fun openHome(){
        router.openHome()
    }
}