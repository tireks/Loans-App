package com.tirexmurina.feature.user.start.ui.bottomContainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tirexmurina.feature.user.start.R
import com.tirexmurina.feature.user.start.databinding.FragmentRegistrationBinding
import com.tirexmurina.feature.user.start.presentation.bottomConrainer.registration.RegisterInputField
import com.tirexmurina.feature.user.start.presentation.bottomConrainer.registration.RegistrationState
import com.tirexmurina.feature.user.start.presentation.bottomConrainer.registration.RegistrationViewModel
import com.tirexmurina.util.features.fragments.BaseFragment
import com.tirexmurina.util.features.fragments.showToast
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>() {

    private val viewModel : RegistrationViewModel by viewModel()
    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegistrationBinding {
        return FragmentRegistrationBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, ::handleState)
        startScreen()
    }

    private fun startScreen() {
        viewModel.startScreen()
    }

    private fun handleState(state: RegistrationState) {
        when(state){
            is RegistrationState.Content -> handleContent(state)
            is RegistrationState.Initial -> Unit
        }
    }

    private fun handleContent(state: RegistrationState.Content) {
        when(state){
            is RegistrationState.Content.Locked -> handleLockedScreen(state)
            is RegistrationState.Content.Unlocked -> performRegistration()
        }
    }



    private fun handleLockedScreen(state: RegistrationState.Content.Locked) {
        binding.registrationButton.setOnClickListener {
            viewModel.validateInput(
                binding.loginEditText.text.toString(),
                binding.passwordEditText.text.toString(),
                binding.passwordConfirmEditText.text.toString()
            )
        }
        viewLifecycleOwner.lifecycleScope.launch {
            with(binding){
                val correctFieldsViews = mutableListOf(
                    loginEditText,
                    passwordEditText,
                    passwordConfirmEditText
                )
                for (editText in correctFieldsViews) {
                    if (editText.text.toString().isEmpty()) {
                        showToast(requireContext(), getString(R.string.empty_fields_warning))
                        break
                    }
                }
                state.troubleList.forEach { troubleField ->
                    when(troubleField){
                        RegisterInputField.Login -> {
                            correctFieldsViews.remove(loginEditText)
                            showWrongField(loginEditText, getString(R.string.login_alert))
                        }
                        RegisterInputField.Password -> {
                            correctFieldsViews.remove(passwordEditText)
                            showWrongField(passwordEditText, getString(R.string.password_alert))
                        }

                        RegisterInputField.PasswordConf -> {
                            correctFieldsViews.remove(passwordConfirmEditText)
                            showWrongField(passwordEditText, getString(R.string.password_conf_alert))
                        }
                    }
                }
                tideUpForm(correctFieldsViews)
            }
        }
    }

    private fun performRegistration() {
        val username = binding.loginEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        (parentFragment as? BottomContainerFragment)?.listener?.onRegister(username, password)
    }

    private fun showWrongField(editText: TextInputEditText, alertText : String) {
        (editText.parent.parent as TextInputLayout).error = alertText
    }

    private fun showCorrectField(editText: TextInputEditText) {
        (editText.parent.parent as TextInputLayout).isErrorEnabled = false
    }

    private fun tideUpForm(correctFieldsList: MutableList<TextInputEditText>) {
        //метод для "подчистки" - убирания эрроров у полей с правильными данными и пустых полей
        correctFieldsList.forEach {
            showCorrectField(it)
        }
    }


}