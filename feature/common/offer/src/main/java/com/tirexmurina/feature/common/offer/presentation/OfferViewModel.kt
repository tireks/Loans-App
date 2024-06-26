package com.tirexmurina.feature.common.offer.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OfferViewModel(
    private val router: OfferRouter
) : ViewModel() {

    private val _state = MutableLiveData<OfferState>(OfferState.Initial)
    val state: LiveData<OfferState> = _state

    fun startScreen(){
        _state.value = OfferState.Content
    }

    fun openHome(){
        router.openHome()
    }

    fun openBranches(){
        router.openBranches()
    }

}