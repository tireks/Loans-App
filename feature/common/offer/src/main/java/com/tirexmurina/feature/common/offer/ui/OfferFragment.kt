package com.tirexmurina.feature.common.offer.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tirexmurina.feature.common.offer.R
import com.tirexmurina.feature.common.offer.databinding.FragmentOfferBinding
import com.tirexmurina.feature.common.offer.presentation.OfferState
import com.tirexmurina.feature.common.offer.presentation.OfferViewModel
import com.tirexmurina.util.features.fragments.BaseFragment
import com.tirexmurina.util.features.fragments.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class OfferFragment : BaseFragment<FragmentOfferBinding>() {

    private val viewModel: OfferViewModel by viewModel()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOfferBinding {
        return FragmentOfferBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::handleState)
        binding.toBranchesButton.setOnClickListener {
            viewModel.openBranches()
        }
        binding.toolbarCrossButton.setOnClickListener {
            viewModel.openHome()
        }
        startScreen()
    }

    private fun startScreen() {
        viewModel.startScreen()
    }

    private fun handleState(state: OfferState) {
        when(state){
            is OfferState.Content -> handleContent()
            is OfferState.Initial -> Unit
        }
    }

    private fun handleContent() {
        binding.globalContainer.show()
    }

}