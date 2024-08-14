package com.tirexmurina.feature.loan.home.homeTab.homeScreen.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tirexmurina.shared.loan.core.data.ForbiddenException
import com.tirexmurina.shared.loan.core.data.LoanConditionsCannotFind
import com.tirexmurina.shared.loan.core.data.NetworkFault
import com.tirexmurina.shared.loan.core.data.NotFoundException
import com.tirexmurina.shared.loan.core.data.RequestFault
import com.tirexmurina.shared.loan.core.data.ResponseFault
import com.tirexmurina.shared.loan.core.data.UnauthorizedException
import com.tirexmurina.shared.loan.core.domain.usecase.GetLoanConditionsUseCase
import com.tirexmurina.shared.loan.core.domain.usecase.GetLoansUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val getLoanConditionsUseCase: GetLoanConditionsUseCase,
    private val getLoansUseCase: GetLoansUseCase,
    private val router: HomeRouter
) : ViewModel() {

    private val _state = MutableLiveData<HomeState>(HomeState.Initial)
    val state: LiveData<HomeState> = _state

    fun initScreen(){
        viewModelScope.launch {
            _state.value = HomeState.Loading
            try {
                val loanConditions = withContext(Dispatchers.IO) {
                    getLoanConditionsUseCase()
                }
                val loans = withContext(Dispatchers.IO) {
                    getLoansUseCase()
                }
                if (loans.isEmpty()){
                    _state.value = HomeState.Content.NoLoans(loanConditions)
                } else {
                    val truncatedList = loans.take(3)
                    _state.value = HomeState.Content.GotLoans(truncatedList, loanConditions)
                }

            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun handleError(exception: Exception){
        when(exception){
            is LoanConditionsCannotFind -> _state.value = HomeState.Error.LoanConditionsCannotFind
            is RequestFault -> _state.value = HomeState.Error.RequestFault
            is UnauthorizedException -> _state.value = HomeState.Error.Unauthorized
            is ForbiddenException -> _state.value = HomeState.Error.Forbidden
            is NotFoundException -> _state.value = HomeState.Error.NotFound
            is ResponseFault -> _state.value = HomeState.Error.ResponseFault
            is NetworkFault -> _state.value = HomeState.Error.NetworkFault
            else -> _state.value = HomeState.Error.UnknownError
        }
    }

    fun openOnboarding(){
        router.openOnboarding(true)
    }


}