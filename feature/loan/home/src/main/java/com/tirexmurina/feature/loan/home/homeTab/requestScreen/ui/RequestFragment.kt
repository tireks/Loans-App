package com.tirexmurina.feature.loan.home.homeTab.requestScreen.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tirexmurina.feature.loan.home.R
import com.tirexmurina.feature.loan.home.databinding.FragmentRequestBinding
import com.tirexmurina.feature.loan.home.homeTab.requestScreen.presentation.RequestField
import com.tirexmurina.feature.loan.home.homeTab.requestScreen.presentation.RequestState
import com.tirexmurina.feature.loan.home.homeTab.requestScreen.presentation.RequestViewModel
import com.tirexmurina.feature.loan.home.navContainer.MainContainerFragment
import com.tirexmurina.util.features.fragments.BaseFragment
import com.tirexmurina.util.features.fragments.hide
import com.tirexmurina.util.features.fragments.show
import com.tirexmurina.util.features.fragments.showDialog
import com.tirexmurina.util.features.fragments.showToast
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RequestFragment : BaseFragment<FragmentRequestBinding>() {

    private val viewModel : RequestViewModel by viewModel()
    private var navigationHost: MainContainerFragment? = null
    private val args: RequestFragmentArgs by navArgs()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRequestBinding {
        return FragmentRequestBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::handleState)
        startScreen()
        binding.toolbarBackButton.setOnClickListener {
            navigationHost?.fromRequestToHome()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        if (parent != null) {
            val mainContainer = parent.parentFragment
            if (mainContainer is MainContainerFragment) {
                navigationHost = mainContainer
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigationHost?.fromRequestToHome()
        }
    }

    private fun startScreen() {
        viewModel.startScreen()
    }

    private fun handleState(state: RequestState) {
        when(state){
            is RequestState.Content -> handleContent(state)
            is RequestState.Error -> handleError(state)
            is RequestState.Initial -> Unit
            is RequestState.Loading -> showLoading()
            is RequestState.LeavingScreen -> findNavController().popBackStack(R.id.homeFragment, false)
        }
    }

    private fun showLoading() {
        binding.contentContainer.hide()
        binding.progressBar.show()
    }

    private fun handleError(state: RequestState.Error) {
        binding.contentContainer.hide()
        when(state){
            RequestState.Error.Forbidden ->
                createDialog(getString(R.string.request_forbidden_error_text))
            RequestState.Error.LoanConditionsCannotFind ->
                createDialog(getString(R.string.request_loans_conditions_error_text))
            RequestState.Error.NetworkFault ->
                createDialog(getString(R.string.request_network_error_text))
            RequestState.Error.NotFound ->
                createDialog(getString(R.string.request_not_found_error_text))
            RequestState.Error.RequestFault ->
                createDialog(getString(R.string.request_request_error_text))
            RequestState.Error.ResponseFault ->
                createDialog(getString(R.string.request_response_error_text))
            RequestState.Error.Unauthorized ->
                createDialog(getString(R.string.request_unauthorized_error_text))
            RequestState.Error.UnknownError ->
                createDialog(getString(R.string.request_unknown_error_text))
        }
    }

    private fun createDialog(msg: String) {
        showDialog(
            { requireActivity().finish() },
            R.layout.dialog,
            R.id.dialogTitle,
            requireContext(),
            getString(R.string.request_positive_button),
            msg
        )
    }

    private fun handleContent(state: RequestState.Content) {
        binding.contentContainer.show()
        binding.progressBar.hide()
        when(state){
            is RequestState.Content.Locked -> handleLockedScreen(state)

            is RequestState.Content.Unlocked -> performRequest()
        }
    }

    private fun handleLockedScreen(state: RequestState.Content.Locked) {
        binding.requestButton.setOnClickListener {
            viewModel.validateInput(
                binding.nameEditText.text.toString(),
                binding.surnameEditText.text.toString(),
                binding.phoneEditText.text.toString()
            )
        }
        viewLifecycleOwner.lifecycleScope.launch {
            with(binding){
                val correctFieldsViews = mutableListOf(
                    nameEditText,
                    surnameEditText,
                    phoneEditText
                )
                for (editText in correctFieldsViews) {
                    if (editText.text.toString().isEmpty()) {
                        showToast(requireContext(), getString(R.string.empty_fields_warning))
                        break
                    }
                }
                state.troubleList.forEach { troubleField ->
                    when(troubleField){
                        RequestField.Name-> {
                            correctFieldsViews.remove(nameEditText)
                            showWrongField(nameEditText, getString(R.string.name_alert))
                        }
                        RequestField.Surname -> {
                            correctFieldsViews.remove(surnameEditText)
                            showWrongField(surnameEditText, getString(R.string.surname_alert))
                        }

                        RequestField.Phone -> {
                            correctFieldsViews.remove(phoneEditText)
                            showWrongField(phoneEditText, getString(R.string.phone_alert))
                        }
                    }
                }
                tideUpForm(correctFieldsViews)
            }
        }
    }

    private fun performRequest() {
        viewModel.requestLoan(
            binding.nameEditText.text.toString(),
            binding.surnameEditText.text.toString(),
            binding.phoneEditText.text.toString(),
            args.amount
        )
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

    override fun onDetach() {
        super.onDetach()
        navigationHost = null
    }



}