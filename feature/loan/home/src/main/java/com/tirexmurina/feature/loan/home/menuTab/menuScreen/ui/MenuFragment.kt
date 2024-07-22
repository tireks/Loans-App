package com.tirexmurina.feature.loan.home.menuTab.menuScreen.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tirexmurina.feature.loan.home.R
import com.tirexmurina.feature.loan.home.databinding.FragmentMenuBinding
import com.tirexmurina.feature.loan.home.menuTab.menuScreen.presentation.MenuState
import com.tirexmurina.feature.loan.home.menuTab.menuScreen.presentation.MenuViewModel
import com.tirexmurina.feature.loan.home.navContainer.MainContainerFragment
import com.tirexmurina.util.features.fragments.BaseFragment
import com.tirexmurina.util.features.fragments.show
import com.tirexmurina.util.features.fragments.showDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class MenuFragment : BaseFragment<FragmentMenuBinding>() {

    private var navigationHost: MainContainerFragment? = null
    private val viewModel : MenuViewModel by viewModel()
    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMenuBinding {
        return FragmentMenuBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::handleState)
        setupButtons()
        initScreen()
    }

    private fun setupButtons() {
        binding.toolbarQuestionButton.setOnClickListener {
            viewModel.openOnboarding()
        }
        binding.loansButtonView.setOnClickListener {
            navigationHost?.fromMenuToLoans()
        }
        binding.offerButtonView.setOnClickListener {
            viewModel.openOffers()
        }
        binding.branchesButtonView.setOnClickListener {
            viewModel.openBranches()
        }
        binding.helpButtonView.setOnClickListener {
            viewModel.openHelp()
        }
        binding.languageButtonView.setOnClickListener {
            viewModel.openLanguages()
        }
        binding.exitButtonView.setOnClickListener {
            viewModel.exitRequest()
        }
    }

    private fun initScreen() {
        viewModel.showScreen()
    }

    private fun handleState(state: MenuState) {
        when(state){
            is MenuState.Content -> handleContent()
            is MenuState.Exit -> handleExit(state)
            is MenuState.Initial -> Unit
            is MenuState.LeavingScreen -> findNavController().popBackStack(R.id.homeFragment, false)
            is MenuState.ExitError -> handleError()
        }
    }

    private fun handleError() {
        createDialog()
    }

    private fun createDialog() {
        showDialog(
            { requireActivity().finish() },
            R.layout.dialog,
            R.id.dialogTitle,
            requireContext(),
            getString(R.string.menu_positive_button),
            getString(R.string.menu_error_placeholder)
        )
    }

    private fun handleExit(state: MenuState.Exit) {
        when(state){
            MenuState.Exit.CloseApp -> requireActivity().finish()
            MenuState.Exit.Request -> handleExitRequest()
        }
    }

    private fun handleExitRequest() {
        showDialog(
            { viewModel.clearToken() },
            { viewModel.showScreen() },
            R.layout.dialog,
            R.id.dialogTitle,
            requireContext(),
            getString(R.string.exit_positive_button),
            getString(R.string.exit_negative_button),
            getString(R.string.exit_dialog_message)
        )
    }

    private fun handleContent() {
        binding.globalContainer.show()
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