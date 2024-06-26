package com.tirexmurina.feature.loan.home.menuTab.loansScreen.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tirexmurina.shared.loan.core.data.remote.ForbiddenException
import com.tirexmurina.shared.loan.core.data.remote.NetworkFault
import com.tirexmurina.shared.loan.core.data.remote.NotFoundException
import com.tirexmurina.shared.loan.core.data.remote.RequestFault
import com.tirexmurina.shared.loan.core.data.remote.ResponseFault
import com.tirexmurina.shared.loan.core.data.remote.UnauthorizedException
import com.tirexmurina.shared.loan.core.domain.usecase.GetLoansUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoansViewModel(
    private val getLoansUseCase: GetLoansUseCase
) : ViewModel() {

    private val _state = MutableLiveData<LoansState>(LoansState.Initial)
    val state: LiveData<LoansState> = _state

    fun getData(){
        viewModelScope.launch {
            _state.value = LoansState.Loading
            try {
                val loans = withContext(Dispatchers.IO) {getLoansUseCase()}
                _state.value = LoansState.Content(loans)
            } catch (exception: Exception){
                handleError(exception)
            }
        }
    }

    private fun handleError(exception: Exception) {
        when(exception){
            is RequestFault -> _state.value =  LoansState.Error.RequestFault
            is UnauthorizedException -> _state.value =  LoansState.Error.Unauthorized
            is ForbiddenException -> _state.value =  LoansState.Error.Forbidden
            is NotFoundException -> _state.value =  LoansState.Error.NotFound
            is ResponseFault -> _state.value =  LoansState.Error.ResponseFault
            is NetworkFault -> _state.value =  LoansState.Error.NetworkFault
            else -> _state.value =  LoansState.Error.UnknownError
        }
    }

}