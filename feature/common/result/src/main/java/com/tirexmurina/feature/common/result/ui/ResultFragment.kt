package com.tirexmurina.feature.common.result.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tirexmurina.feature.common.result.databinding.FragmentResultBinding
import com.tirexmurina.feature.common.result.presentation.ResultState
import com.tirexmurina.feature.common.result.presentation.ResultViewModel
import com.tirexmurina.util.features.fragments.BaseFragment
import com.tirexmurina.util.features.fragments.hide
import com.tirexmurina.util.features.fragments.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResultFragment : BaseFragment<FragmentResultBinding>() {

    companion object {
        private const val SUCCESS_RESULT = "success_result"
        fun newInstance(successResult : Boolean) = ResultFragment().apply {
            arguments = Bundle().apply {
                putBoolean(SUCCESS_RESULT, successResult)
            }
        }

    }

    private val viewModel: ResultViewModel by viewModel()
    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentResultBinding {
        return FragmentResultBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::handleState)
        showResult()
        binding.toBranchesButton.setOnClickListener {
            viewModel.openBranches()
        }
        binding.toHomeButton.setOnClickListener {
            viewModel.openHome()
        }
        binding.toolbarCrossButton.setOnClickListener {
            viewModel.openHome()
        }
    }

    private fun showResult() {
        viewModel.showResult(requireArguments().getBoolean(SUCCESS_RESULT))
    }

    private fun handleState(state: ResultState) {
        when(state){
            is ResultState.Content -> handleContent(state)
            is ResultState.Initial -> Unit
        }
    }

    private fun handleContent(state: ResultState.Content) {
        when(state){
            is ResultState.Content.Fault -> {
                binding.successContainer.hide()
                binding.faultContainer.show()
            }
            is ResultState.Content.Success -> {
                binding.successContainer.show()
                binding.faultContainer.hide()
            }
        }
    }

}