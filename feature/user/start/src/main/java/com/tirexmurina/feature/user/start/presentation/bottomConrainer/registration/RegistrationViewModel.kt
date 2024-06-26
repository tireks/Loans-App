package com.tirexmurina.feature.user.start.presentation.bottomConrainer.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationViewModel(

) : ViewModel() {

    private val _state = MutableLiveData<RegistrationState>(RegistrationState.Initial)
    val state: LiveData<RegistrationState> = _state

    private val emptyFieldsList = mutableListOf(
        RegisterInputField.Login,
        RegisterInputField.Password
    )

    private val wrongFieldsList = mutableListOf<RegisterInputField>()

    private var currentPassword = ""

    fun startScreen(){
        handleScreenState()
    }

    fun validateInput(login: String,
                      password: String,
                      passwordConf : String
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                validateField(login, RegisterInputField.Login)
                validateField(password, RegisterInputField.Password)
                validateField(passwordConf, RegisterInputField.PasswordConf)
            }
            handleScreenState()
        }
    }

    private fun validateField(content: String, field: RegisterInputField) {
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

    private fun isFieldValid(field: RegisterInputField, content: String): Boolean {
        return when (field) {

            RegisterInputField.Login -> isLoginValid(content)

            RegisterInputField.Password -> isPasswordValid(content)

            RegisterInputField.PasswordConf -> isPasswordsConfValid(content)
        }
    }

    private fun isLoginValid(validationData: String): Boolean {
        return Regex("^[a-zA-Z0-9]*\$").matches(validationData)
    }

    private fun isPasswordValid(validationData: String): Boolean {
        currentPassword = validationData
        return Regex("^[a-zA-Z0-9]*\$").matches(validationData)
    }

    private fun isPasswordsConfValid(validationData: String): Boolean {
        return validationData == currentPassword
    }

    private fun handleScreenState() {
        if (emptyFieldsList.isEmpty() && wrongFieldsList.isEmpty()) {
            val emptyTroubleList = mutableListOf<RegisterInputField>()
            _state.value = RegistrationState.Content.Locked(emptyTroubleList)
            _state.value = RegistrationState.Content.Unlocked
        } else {
            _state.value = RegistrationState.Content.Locked(wrongFieldsList)
        }
    }

}