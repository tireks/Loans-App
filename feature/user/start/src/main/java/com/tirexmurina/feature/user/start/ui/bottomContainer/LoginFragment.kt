package com.tirexmurina.feature.user.start.ui.bottomContainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tirexmurina.feature.user.start.R
import com.tirexmurina.feature.user.start.databinding.FragmentLoginBinding
import com.tirexmurina.feature.user.start.presentation.bottomConrainer.login.LoginInputField
import com.tirexmurina.feature.user.start.presentation.bottomConrainer.login.LoginState
import com.tirexmurina.feature.user.start.presentation.bottomConrainer.login.LoginViewModel
import com.tirexmurina.util.features.fragments.BaseFragment
import com.tirexmurina.util.features.fragments.showToast
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel : LoginViewModel by viewModel()
    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, ::handleState)
        startScreen()
    }

    private fun startScreen() {
        viewModel.startScreen()
    }

    private fun handleState(state: LoginState) {
        when(state){
            is LoginState.Content -> handleContent(state)
            is LoginState.Initial -> Unit
        }
    }

    private fun handleContent(state: LoginState.Content) {
        when(state){
            is LoginState.Content.Locked -> handleLockedScreen(state)
            is LoginState.Content.Unlocked -> performLogin()
        }
    }



    private fun handleLockedScreen(state: LoginState.Content.Locked) {
        binding.loginButton.setOnClickListener {
            viewModel.validateInput(
                binding.loginEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )

        }
        viewLifecycleOwner.lifecycleScope.launch {
            with(binding){
                val correctFieldsViews = mutableListOf(
                    loginEditText,
                    passwordEditText
                )
                for (editText in correctFieldsViews) {
                    if (editText.text.toString().isEmpty()) {
                        showToast(requireContext(), getString(R.string.empty_fields_warning))
                        break
                    }
                }
                state.troubleList.forEach { troubleField ->
                    when(troubleField){
                        LoginInputField.Login -> {
                            correctFieldsViews.remove(loginEditText)
                            showWrongField(loginEditText, getString(R.string.login_alert))
                        }
                        LoginInputField.Password -> {
                            correctFieldsViews.remove(passwordEditText)
                            showWrongField(passwordEditText, getString(R.string.password_alert))
                        }
                    }
                }
                tideUpForm(correctFieldsViews)
            }
        }
    }

    private fun performLogin() {
        val username = binding.loginEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        (parentFragment as? BottomContainerFragment)?.listener?.onLogin(username, password)
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