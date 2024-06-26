package com.tirexmurina.leonov.navigation

import com.github.terrakok.cicerone.Router
import com.tirexmurina.feature.loan.home.getMainScreen
import com.tirexmurina.feature.common.onboarding.presentation.OnboardingRouter

class OnboardingRouterImpl(private val router: Router) : OnboardingRouter {
    override fun openHome() {
        router.navigateTo(getMainScreen())
    }
}