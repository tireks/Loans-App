package com.tirexmurina.feature.common.branches.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tirexmurina.feature.common.branches.databinding.FragmentBranchesBinding
import com.tirexmurina.feature.common.branches.presentation.BranchesState
import com.tirexmurina.feature.common.branches.presentation.BranchesViewModel
import com.tirexmurina.util.features.fragments.BaseFragment
import com.tirexmurina.util.features.fragments.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class BranchesFragment : BaseFragment<FragmentBranchesBinding>() {

    private val viewModel: BranchesViewModel by viewModel()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBranchesBinding {
        return FragmentBranchesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::handleState)
        binding.toHomeButton.setOnClickListener {
            viewModel.openHome()
        }
        binding.toolbarCrossButton.setOnClickListener {
            viewModel.openHome()
        }
        startScreen()
    }

    private fun startScreen() {
        viewModel.startScreen()
    }

    private fun handleState(state: BranchesState) {
        when(state){
            is BranchesState.Content -> handleContent()
            is BranchesState.Initial -> Unit
        }
    }

    private fun handleContent() {
        binding.globalContainer.show()
    }

}