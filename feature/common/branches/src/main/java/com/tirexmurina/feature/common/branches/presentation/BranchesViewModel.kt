package com.tirexmurina.feature.common.branches.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BranchesViewModel(
    private val router: BranchesRouter
) : ViewModel() {

    private val _state = MutableLiveData<BranchesState>(BranchesState.Initial)
    val state: LiveData<BranchesState> = _state

    fun startScreen(){
        _state.value = BranchesState.Content
    }

    fun openHome(){
        router.openHome()
    }

}