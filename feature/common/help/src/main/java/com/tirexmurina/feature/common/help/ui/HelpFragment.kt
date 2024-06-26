package com.tirexmurina.feature.common.help.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tirexmurina.feature.common.help.databinding.FragmentHelpBinding
import com.tirexmurina.feature.common.help.presentation.HelpState
import com.tirexmurina.feature.common.help.presentation.HelpViewModel
import com.tirexmurina.util.features.fragments.BaseFragment
import com.tirexmurina.util.features.fragments.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class HelpFragment : BaseFragment<FragmentHelpBinding>() {

    private val viewModel: HelpViewModel by viewModel()
    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHelpBinding {
        return FragmentHelpBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::handleState)
        binding.toolbarCrossButton.setOnClickListener {
            viewModel.openHome()
        }
        startScreen()
    }

    private fun startScreen() {
        viewModel.startScreen()
    }

    private fun handleState(state: HelpState) {
        when(state){
            is HelpState.Content -> handleContent()
            is HelpState.Initial -> Unit
        }
    }

    private fun handleContent() {
        binding.globalContainer.show()
    }

}