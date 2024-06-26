package com.tirexmurina.feature.loan.home.menuTab.detailsScreen.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.navArgs
import com.tirexmurina.feature.loan.home.R
import com.tirexmurina.feature.loan.home.databinding.FragmentDetailsBinding
import com.tirexmurina.feature.loan.home.menuTab.detailsScreen.presentation.DetailsState
import com.tirexmurina.feature.loan.home.menuTab.detailsScreen.presentation.DetailsViewModel
import com.tirexmurina.feature.loan.home.navContainer.MainContainerFragment
import com.tirexmurina.feature.loan.home.utils.loanStateHelper
import com.tirexmurina.shared.loan.core.domain.entity.Loan
import com.tirexmurina.util.features.fragments.BaseFragment
import com.tirexmurina.util.features.fragments.formatters.formatDateForDetails
import com.tirexmurina.util.features.fragments.hide
import com.tirexmurina.util.features.fragments.show
import com.tirexmurina.util.features.fragments.showDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : BaseFragment<FragmentDetailsBinding>() {

    private var navigationHost: MainContainerFragment? = null
    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel : DetailsViewModel by viewModel()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailsBinding {
        return FragmentDetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::handleState)
        val id = args.id.toLong()
        binding.toolbarText.text = getString(R.string.loan_item_id_template, id.toString())
        binding.toolbarBackButton.setOnClickListener {
            navigationHost?.fromDetailsToLoans()
        }
        getData(id)

    }

    private fun getData(id: Long) {
        viewModel.getData(id)
    }

    private fun handleState(state: DetailsState) {
        when(state){
            is DetailsState.Content -> handleContent(state.loan)
            is DetailsState.Error -> handleError(state)
            is DetailsState.Initial -> Unit
            is DetailsState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        binding.progressBar.show()
        binding.contentContainer.hide()
    }

    private fun handleError(state: DetailsState.Error) {
        binding.contentContainer.hide()
        when(state){
            DetailsState.Error.Forbidden ->
                createDialog("Кажется произошла ошибка авторизации. Попробуйте перзапустить приложение")
            DetailsState.Error.SingleLoanCannotFind ->
                createDialog("Какие-то проблемы на сервере. Не удается найти условия займа")
            DetailsState.Error.NetworkFault ->
                createDialog("Проблема с сетевым подключением. Проверьте, включен ли у вас интернет")
            DetailsState.Error.NotFound ->
                createDialog("Важный элемент не был найден в ответе сервера")
            DetailsState.Error.RequestFault ->
                createDialog("Проблема с запросом на сервер")
            DetailsState.Error.ResponseFault ->
                createDialog("Непредвиденная ошибка в ответе сервера")
            DetailsState.Error.Unauthorized ->
                createDialog("Кажется произошла ошибка авторизации. Попробуйте перзапустить приложение")
            DetailsState.Error.UnknownError ->
                createDialog("Непредвиденная ошибка")
        }
    }

    private fun createDialog(msg: String) {
        showDialog(
            { requireActivity().finish() },
            R.layout.dialog,
            R.id.dialogTitle,
            requireContext(),
            "OK",
            msg
        )
    }

    private fun handleContent(loan: Loan) {
        with(binding){
            progressBar.hide()
            contentContainer.show()
            firstNameLabel.text = loan.firstName
            lastNameLabel.text = loan.lastName
            phoneLabel.text = loan.phoneNumber
            idLabel.text = loan.id.toString()
            dateLabel.text = formatDateForDetails(loan.date)
            periodLabel.text = loan.period.toString()
            percentLabel.text = getString(R.string.percent_template, loan.percent.toString())
            amountLabel.text = getString(R.string.money_amount_template, loan.amount.toString())
            val (statusText, statusColor) = loanStateHelper(requireContext(), loan.state)
            stateLabel.text = statusText
            stateLabel.setTextColor(statusColor)
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navigationHost?.fromDetailsToLoans()
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

    override fun onDetach() {
        super.onDetach()
        navigationHost = null
    }

}