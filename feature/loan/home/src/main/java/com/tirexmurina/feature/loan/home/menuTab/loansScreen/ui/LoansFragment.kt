package com.tirexmurina.feature.loan.home.menuTab.loansScreen.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.tirexmurina.feature.loan.home.R
import com.tirexmurina.feature.loan.home.databinding.FragmentLoansBinding
import com.tirexmurina.feature.loan.home.menuTab.loansScreen.presentation.LoansState
import com.tirexmurina.feature.loan.home.menuTab.loansScreen.presentation.LoansViewModel
import com.tirexmurina.feature.loan.home.menuTab.loansScreen.presentation.recycler.LoanListAdapter
import com.tirexmurina.feature.loan.home.navContainer.MainContainerFragment
import com.tirexmurina.shared.loan.core.domain.entity.Loan
import com.tirexmurina.util.features.fragments.BaseFragment
import com.tirexmurina.util.features.fragments.hide
import com.tirexmurina.util.features.fragments.show
import com.tirexmurina.util.features.fragments.showDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoansFragment : BaseFragment<FragmentLoansBinding>() {

    private val viewModel : LoansViewModel by viewModel()

    private var navigationHost: MainContainerFragment? = null
    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoansBinding {
        return FragmentLoansBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::handleState)
        binding.toolbarBackButton.setOnClickListener {
            navigationHost?.fromLoansToHome()
        }
        setupRecycler()
        getData()
    }

    private fun getData() {
        viewModel.getData()
    }

    private fun setupRecycler() {
        binding.contentRecycler.adapter = LoanListAdapter(
            ::handleLoanClick
        )
        binding.contentRecycler.layoutManager = LinearLayoutManager(context)
    }

    private fun handleLoanClick(loan: Loan) {
        navigationHost?.fromLoansToDetails(loan.id)
    }

    private fun handleState(state: LoansState) {
        when(state){
            is LoansState.Content -> handleContent(state)
            is LoansState.Error -> handleError(state)
            is LoansState.Initial -> Unit
            is LoansState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        binding.progressBar.show()
        binding.contentContainer.hide()
    }

    private fun handleContent(state: LoansState.Content) {
        binding.progressBar.hide()
        binding.contentContainer.show()
        (binding.contentRecycler.adapter as? LoanListAdapter)?.loans = state.contentList
    }

    private fun handleError(state: LoansState.Error) {
        binding.contentContainer.hide()
        when(state){
             LoansState.Error.Forbidden ->
                createDialog("Кажется произошла ошибка авторизации. Попробуйте перзапустить приложение")
             LoansState.Error.NetworkFault ->
                createDialog("Проблема с сетевым подключением. Проверьте, включен ли у вас интернет")
             LoansState.Error.NotFound ->
                createDialog("Важный элемент не был найден в ответе сервера")
             LoansState.Error.RequestFault ->
                createDialog("Проблема с запросом на сервер")
             LoansState.Error.ResponseFault ->
                createDialog("Непредвиденная ошибка в ответе сервера")
             LoansState.Error.Unauthorized ->
                createDialog("Кажется произошла ошибка авторизации. Попробуйте перзапустить приложение")
             LoansState.Error.UnknownError ->
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
            navigationHost?.fromLoansToHome()
        }
    }

    override fun onDetach() {
        super.onDetach()
        navigationHost = null
    }

}