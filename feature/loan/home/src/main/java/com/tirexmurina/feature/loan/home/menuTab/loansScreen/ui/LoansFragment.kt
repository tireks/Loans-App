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
        navigationHost?.fromLoansToDetails(loan.id.toInt())
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
                 createDialog(getString(R.string.loans_forbidden_error_text))
             LoansState.Error.NetworkFault ->
                 createDialog(getString(R.string.loans_network_error_text))
             LoansState.Error.NotFound ->
                 createDialog(getString(R.string.loans_not_found_error_text))
             LoansState.Error.RequestFault ->
                 createDialog(getString(R.string.loans_request_error_text))
             LoansState.Error.ResponseFault ->
                 createDialog(getString(R.string.loans_response_error_text))
             LoansState.Error.Unauthorized ->
                 createDialog(getString(R.string.loans_unauthorized_error_text))
             LoansState.Error.UnknownError ->
                 createDialog(getString(R.string.loans_unknown_error_text))
        }
    }

    private fun createDialog(msg: String) {
        showDialog(
            { requireActivity().finish() },
            R.layout.dialog,
            R.id.dialogTitle,
            requireContext(),
            getString(R.string.loans_positive_button),
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