package com.tirexmurina.feature.loan.home.menuTab.detailsScreen.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tirexmurina.shared.loan.core.data.remote.ForbiddenException
import com.tirexmurina.shared.loan.core.data.remote.LoanConditionsCannotFind
import com.tirexmurina.shared.loan.core.data.remote.NetworkFault
import com.tirexmurina.shared.loan.core.data.remote.NotFoundException
import com.tirexmurina.shared.loan.core.data.remote.RequestFault
import com.tirexmurina.shared.loan.core.data.remote.ResponseFault
import com.tirexmurina.shared.loan.core.data.remote.UnauthorizedException
import com.tirexmurina.shared.loan.core.domain.usecase.GetLoansByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsViewModel(
    private val getLoansByIdUseCase: GetLoansByIdUseCase
) : ViewModel() {

    private val _state = MutableLiveData<DetailsState>(DetailsState.Initial)
    val state: LiveData<DetailsState> = _state

    fun getData(id : Long){
        viewModelScope.launch {
            _state.value = DetailsState.Loading
            try {
                val loan = withContext(Dispatchers.IO) {getLoansByIdUseCase(id)}
                _state.value = DetailsState.Content(loan)
            } catch (exception : Exception){
                handleError(exception)
            }
        }
    }

    private fun handleError(exception: Exception) {
        when(exception){
            is LoanConditionsCannotFind -> _state.value = DetailsState.Error.SingleLoanCannotFind
            is RequestFault -> _state.value = DetailsState.Error.RequestFault
            is UnauthorizedException -> _state.value = DetailsState.Error.Unauthorized
            is ForbiddenException -> _state.value = DetailsState.Error.Forbidden
            is NotFoundException -> _state.value = DetailsState.Error.NotFound
            is ResponseFault -> _state.value = DetailsState.Error.ResponseFault
            is NetworkFault -> _state.value = DetailsState.Error.NetworkFault
            else -> _state.value = DetailsState.Error.UnknownError
        }
    }

}