package com.tirexmurina.feature.loan.home.homeTab.requestScreen.presentation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tirexmurina.feature.loan.home.homeTab.homeScreen.presentation.HomeRouter
import com.tirexmurina.shared.loan.core.data.models.LoanRequestModel
import com.tirexmurina.shared.loan.core.data.remote.ForbiddenException
import com.tirexmurina.shared.loan.core.data.remote.LoanConditionsCannotFind
import com.tirexmurina.shared.loan.core.data.remote.NetworkFault
import com.tirexmurina.shared.loan.core.data.remote.NotFoundException
import com.tirexmurina.shared.loan.core.data.remote.RequestFault
import com.tirexmurina.shared.loan.core.data.remote.ResponseFault
import com.tirexmurina.shared.loan.core.data.remote.UnauthorizedException
import com.tirexmurina.shared.loan.core.domain.usecase.GetLoanConditionsUseCase
import com.tirexmurina.shared.loan.core.domain.usecase.RequestLoanUseCase
import com.tirexmurina.util.core.exeptions.SuccessfulThrowable
import com.tirexmurina.util.core.exeptions.UnsuccessfulException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RequestViewModel(
    private val getLoanConditionsUseCase: GetLoanConditionsUseCase,
    private val requestLoanUseCase: RequestLoanUseCase,
    private val router: HomeRouter
) : ViewModel() {

    private val _state = MutableLiveData<RequestState>(RequestState.Initial)
    val state: LiveData<RequestState> = _state

    private val emptyFieldsList = mutableListOf(
        RequestField.Name,
        RequestField.Surname,
        RequestField.Phone
    )

    private val wrongFieldsList = mutableListOf<RequestField>()

    fun startScreen(){
        handleScreenState()
    }

    fun requestLoan(
        firstName : String,
        surName : String,
        phone : String,
        amount : Int
    ){

        viewModelScope.launch {
            _state.value = RequestState.Loading
            try {
                val loanConditions = withContext(Dispatchers.IO) {
                    getLoanConditionsUseCase()
                }
                val loanRequest = LoanRequestModel(
                    amount = amount.toLong(),
                    firstName = firstName,
                    lastName = surName,
                    percent = loanConditions.percent,
                    period = loanConditions.period,
                    phoneNumber = phone
                )
                withContext(Dispatchers.IO){
                    requestLoanUseCase(loanRequest)
                }
            } catch (successfulResult : SuccessfulThrowable){
                openSuccessScreen()
            } catch (unsuccessfulResult : UnsuccessfulException){
                openFaultScreen()
            } catch (exception : Exception){
                handleError(exception)
            }
        }
    }

    fun validateInput(name: String, surname: String, phone : String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                validateField(name, RequestField.Name)
                validateField(surname, RequestField.Surname)
                validateField(phone, RequestField.Phone)
            }
            handleScreenState()
        }
    }

    private fun validateField(content: String, field: RequestField) {
        if (content.isEmpty()) {
            if (!emptyFieldsList.contains(field)) {
                emptyFieldsList.add(field)
                wrongFieldsList.remove(field)
            }
        } else {
            emptyFieldsList.remove(field)
            val fieldValid = isFieldValid(field, content)
            if (!fieldValid) {
                if (!wrongFieldsList.contains(field)) {
                    wrongFieldsList.add(field)
                }
            } else {
                wrongFieldsList.remove(field)
            }

        }
    }



    private fun isFieldValid(field: RequestField, content: String): Boolean {
        return when (field) {

            RequestField.Name -> isNameValid(content)

            RequestField.Surname -> isNameValid(content)

            RequestField.Phone -> isPhoneValid(content)
        }
    }

    private fun isNameValid(validationData: String): Boolean {
        return Regex("^[А-ЯЁ][а-яё]*$").matches(validationData)
    }

    private fun isPhoneValid(validationData: String): Boolean {
        return Regex("^\\+7\\d{10}$").matches(validationData)
    }

    private fun handleScreenState() {
        if (emptyFieldsList.isEmpty() && wrongFieldsList.isEmpty()) {
            val emptyTroubleList = mutableListOf<RequestField>()
            _state.value = RequestState.Content.Locked(emptyTroubleList)
            _state.value = RequestState.Content.Unlocked
        } else {
            _state.value = RequestState.Content.Locked(wrongFieldsList)
        }
    }

    private fun openFaultScreen() {
        router.openResultScreen(false)
        _state.value = RequestState.LeavingScreen
    }

    private fun openSuccessScreen() {
        router.openResultScreen(true)
        _state.value = RequestState.LeavingScreen
    }
    private fun handleError(exception: Exception) {
        when(exception){
            is LoanConditionsCannotFind -> _state.value = RequestState.Error.LoanConditionsCannotFind
            is RequestFault -> _state.value = RequestState.Error.RequestFault
            is UnauthorizedException -> _state.value = RequestState.Error.Unauthorized
            is ForbiddenException -> _state.value = RequestState.Error.Forbidden
            is NotFoundException -> _state.value = RequestState.Error.NotFound
            is ResponseFault -> _state.value = RequestState.Error.ResponseFault
            is NetworkFault -> _state.value = RequestState.Error.NetworkFault
            else -> _state.value = RequestState.Error.UnknownError
        }
    }
    

}