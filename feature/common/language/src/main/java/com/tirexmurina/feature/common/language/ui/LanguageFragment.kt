package com.tirexmurina.feature.common.language.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tirexmurina.feature.common.language.databinding.FragmentLanguageBinding
import com.tirexmurina.feature.common.language.presentation.LanguageState
import com.tirexmurina.feature.common.language.presentation.LanguageViewModel
import com.tirexmurina.util.features.fragments.BaseFragment
import com.tirexmurina.util.features.fragments.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class LanguageFragment : BaseFragment<FragmentLanguageBinding>() {

    private val viewModel: LanguageViewModel by viewModel()
    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLanguageBinding {
        return FragmentLanguageBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::handleState)
        binding.toolbarBackButton.setOnClickListener {
            viewModel.openHome()
        }
        binding.confirmButton.setOnClickListener {
            viewModel.openHome()
        }
        startScreen()
    }

    private fun startScreen() {
        viewModel.startScreen()
    }

    private fun handleState(state: LanguageState) {
        when(state){
            is LanguageState.Content -> handleContent()
            is LanguageState.Initial -> Unit
        }
    }

    private fun handleContent() {
        binding.globalContainer.show()
    }


}