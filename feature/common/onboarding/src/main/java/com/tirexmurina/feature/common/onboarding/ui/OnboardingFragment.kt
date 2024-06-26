package com.tirexmurina.feature.common.onboarding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.viewpager2.widget.ViewPager2
import com.tirexmurina.feature.common.onboarding.R
import com.tirexmurina.feature.common.onboarding.databinding.FragmentOnboardingBinding
import com.tirexmurina.util.features.fragments.BaseFragment
import com.tirexmurina.util.features.fragments.hide
import com.tirexmurina.util.features.fragments.show
import com.tirexmurina.feature.common.onboarding.presentation.OnboardingState
import com.tirexmurina.feature.common.onboarding.presentation.OnboardingViewModel
import com.tirexmurina.feature.common.onboarding.presentation.ViewPagerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>() {

    companion object {
        private const val USER_IS_NEWBIE = "user_is_newbie"
        fun newInstance(userIsNewbie : Boolean) = OnboardingFragment().apply {
            arguments = Bundle().apply {
                putBoolean(USER_IS_NEWBIE, userIsNewbie)
            }
        }

    }

    private val viewModel: OnboardingViewModel by viewModel()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOnboardingBinding {
        return FragmentOnboardingBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::handleState)
    }

    private fun handleState(state: OnboardingState) {
        when(state){
            is OnboardingState.Initial -> handleUserStatus()
            is OnboardingState.ShowOnboarding -> showOnboarding()
        }
    }

    private fun showOnboarding() {
        setupViewPager()
        setupButtons()
        binding.globalContainer.show()
    }

    private fun handleUserStatus() {
        binding.globalContainer.hide()
        val args = requireArguments()
        viewModel.handleUserStatus(args.getBoolean(USER_IS_NEWBIE))
    }

    private fun setupViewPager() {
        val slides = listOf(
            R.layout.slider_screen_1,
            R.layout.slider_screen_2,
            R.layout.slider_screen_3
        )
        val adapter = ViewPagerAdapter()
        binding.viewPager.adapter = adapter
        binding.springDotsIndicator.attachToPager(binding.viewPager)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateButtons(position)
            }
        })

        adapter.slides = slides
    }

    private fun setupButtons(){
        with(binding){

            toolbarCrossButton.setOnClickListener {
                viewModel.openHomeScreen()
            }
        }

    }

    private fun updateButtons(position: Int) {
        with(binding){
            when (position) {
                0 -> {
                    leftButton.visibility = View.GONE
                    rightButton.text = "Далее"
                    rightButton.setOnClickListener {
                        viewPager.setCurrentItem(1, true)
                    }
                    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                        viewModel.openHomeScreen()
                    }
                }
                1 -> {
                    leftButton.visibility = View.VISIBLE
                    leftButton.text = "Назад"
                    rightButton.text = "Далее"
                    leftButton.setOnClickListener {
                        viewPager.setCurrentItem(viewPager.currentItem - 1, true)
                    }
                    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                        viewPager.setCurrentItem(viewPager.currentItem - 1, true)
                    }
                    rightButton.setOnClickListener {
                        viewPager.setCurrentItem(2, true)
                    }
                }
                2 -> {
                    rightButton.text = "Закрыть"
                    rightButton.setOnClickListener {
                        viewModel.openHomeScreen()
                    }
                }

                else -> {}
            }
        }
    }

}