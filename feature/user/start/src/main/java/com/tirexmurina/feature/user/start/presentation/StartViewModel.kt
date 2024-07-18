package com.tirexmurina.feature.user.start.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tirexmurina.shared.user.core.data.local.SharedPrefsCorruptedException
import com.tirexmurina.shared.user.core.data.models.AuthModel
import com.tirexmurina.shared.user.core.data.remote.ForbiddenException
import com.tirexmurina.shared.user.core.data.remote.NetworkFault
import com.tirexmurina.shared.user.core.data.remote.NotFoundException
import com.tirexmurina.shared.user.core.data.remote.RequestFault
import com.tirexmurina.shared.user.core.data.remote.ResponseFault
import com.tirexmurina.shared.user.core.data.remote.UnauthorizedException
import com.tirexmurina.shared.user.core.domain.usecase.AskTokenAvailabilityUseCase
import com.tirexmurina.shared.user.core.domain.usecase.LoginUserUseCase
import com.tirexmurina.shared.user.core.domain.usecase.RegisterUserUseCase
import com.tirexmurina.util.core.exeptions.SuccessfulThrowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartViewModel (
    private val loginUserUseCase: LoginUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val askTokenAvailability: AskTokenAvailabilityUseCase,
    private val router: StartRouter
) : ViewModel() {
    private val _state = MutableLiveData<StartState>(StartState.Initial)
    val state: LiveData<StartState> = _state

    fun startScreen(){
        viewModelScope.launch {
            _state.value = StartState.Loading
            try {
                delay(500) //это исключительно в демонстративных целях
                if (askTokenAvailability()){
                    openOnboarding(false)
                } else {
                    _state.value = StartState.Content
                }
            } catch ( sharedPrefsException : SharedPrefsCorruptedException){
                handleError(sharedPrefsException)
            }


        }
    }

    fun loginUser(login : String, password: String){
        performLogin(onRegistration = false, login, password)
    }

    fun registerUser(login : String, password: String){
        viewModelScope.launch {
            try {
                val authCredentials = AuthModel(login, password)
                withContext(Dispatchers.IO){ registerUserUseCase(authCredentials) }

            } catch (success: SuccessfulThrowable) {
                performLogin(onRegistration = true, login, password)

            }catch (e : Exception){
                handleError(e)
            }
        }
    }

    private fun openOnboarding(userIsNewbie: Boolean){
        router.openOnboarding(userIsNewbie)
    }

    private fun performLogin(onRegistration : Boolean, login : String, password: String){
        viewModelScope.launch {
            try {
                val authCredentials = AuthModel(login, password)
                withContext(Dispatchers.IO){ loginUserUseCase(authCredentials) }
            } catch (success: SuccessfulThrowable) {
                openOnboarding(userIsNewbie = onRegistration)
            } catch (e : Exception){
                handleError(e)
            }
        }
    }

    private fun handleError(exception: Exception) {
        when(exception){
            is SharedPrefsCorruptedException -> _state.value = StartState.Error.SharedPrefsCorrupted
            is RequestFault -> _state.value = StartState.Error.RequestFault
            is UnauthorizedException -> _state.value = StartState.Error.Unauthorized
            is ForbiddenException -> _state.value = StartState.Error.Forbidden
            is NotFoundException -> _state.value = StartState.Error.NotFound
            is ResponseFault -> _state.value = StartState.Error.ResponseFault
            is NetworkFault -> _state.value = StartState.Error.NetworkFault
            else -> _state.value = StartState.Error.UnknownError
        }
    }


}