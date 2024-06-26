package com.tirexmurina.leonov.navigation

import com.github.terrakok.cicerone.Router
import com.tirexmurina.feature.user.start.presentation.StartRouter
import com.tirexmurina.feature.common.onboarding.getOnboardingScreen

class StartRouterImpl(private val router: Router) : StartRouter {
    override fun openOnboarding(userIsNewbie: Boolean) {
        router.navigateTo(getOnboardingScreen(userIsNewbie))
    }
}