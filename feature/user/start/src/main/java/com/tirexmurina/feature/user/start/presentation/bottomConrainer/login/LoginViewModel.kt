package com.tirexmurina.feature.user.start.presentation.bottomConrainer.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(

) : ViewModel() {

    private val _state = MutableLiveData<LoginState>(LoginState.Initial)
    val state: LiveData<LoginState> = _state

    private val emptyFieldsList = mutableListOf(
        LoginInputField.Login,
        LoginInputField.Password
    )

    private val wrongFieldsList = mutableListOf<LoginInputField>()

    fun startScreen(){
        handleScreenState()
    }

    fun validateInput(login: String, password: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                validateField(login, LoginInputField.Login)
                validateField(password, LoginInputField.Password)
            }
            handleScreenState()
        }
    }

    private fun validateField(content: String, field: LoginInputField) {
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

    private fun isFieldValid(field: LoginInputField, content: String): Boolean {
        return when (field) {

            LoginInputField.Login -> isLoginValid(content)

            LoginInputField.Password -> isPasswordValid(content)

        }
    }

    private fun isLoginValid(validationData: String): Boolean {
        return Regex("^[a-zA-Z0-9]*\$").matches(validationData)
    }

    private fun isPasswordValid(validationData: String): Boolean {
        return Regex("^[a-zA-Z0-9]*\$").matches(validationData)
    }

    private fun handleScreenState() {
        if (emptyFieldsList.isEmpty() && wrongFieldsList.isEmpty()) {
            val emptyTroubleList = mutableListOf<LoginInputField>()
            _state.value = LoginState.Content.Locked(emptyTroubleList)
            _state.value = LoginState.Content.Unlocked
        } else {
            _state.value = LoginState.Content.Locked(wrongFieldsList)
        }
    }


}