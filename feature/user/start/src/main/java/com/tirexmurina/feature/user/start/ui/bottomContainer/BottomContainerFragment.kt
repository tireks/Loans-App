package com.tirexmurina.feature.user.start.ui.bottomContainer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.tirexmurina.feature.user.start.R
import com.tirexmurina.feature.user.start.databinding.FragmentBottomContainerBinding
import com.tirexmurina.feature.user.start.presentation.bottomConrainer.BottomViewPagerAdapter
import com.tirexmurina.util.features.fragments.BaseFragment

class BottomContainerFragment : BaseFragment<FragmentBottomContainerBinding>() {

    interface BottomContainerFragmentListener {
        fun onLogin(login: String, password: String)
        fun onRegister(login: String, password: String)
    }

    var listener: BottomContainerFragmentListener? = null


    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBottomContainerBinding {
        return FragmentBottomContainerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabs()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is BottomContainerFragmentListener) {
            listener = parentFragment as BottomContainerFragmentListener
        } else {
            throw RuntimeException("$parentFragment must implement BottomContainerFragmentListener")
        }
    }


    private fun setupTabs() {
        val adapter = BottomViewPagerAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.bottom_tab_sign_in)
                1 -> getString(R.string.bottom_tab_registartion)
                else -> null
            }
        }.attach()

    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }

}