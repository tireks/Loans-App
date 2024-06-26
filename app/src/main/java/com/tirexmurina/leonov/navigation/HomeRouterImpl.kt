package com.tirexmurina.leonov.navigation

import com.github.terrakok.cicerone.Router
import com.tirexmurina.feature.common.branches.getBranchesScreen
import com.tirexmurina.feature.common.help.getHelpScreen
import com.tirexmurina.feature.common.language.getLanguageScreen
import com.tirexmurina.feature.common.offer.getOfferScreen
import com.tirexmurina.feature.common.result.getResultScreen
import com.tirexmurina.feature.loan.home.homeTab.homeScreen.presentation.HomeRouter
import com.tirexmurina.feature.common.onboarding.getOnboardingScreen


class HomeRouterImpl(private val router: Router) : HomeRouter {

    override fun openOnboarding(needOnboarding: Boolean) {
        router.navigateTo(getOnboardingScreen(needOnboarding))
    }

    override fun openResultScreen(resultSuccessful: Boolean) {
        router.navigateTo(getResultScreen(resultSuccessful))
    }

    override fun openSpecialOfferScreen() {
        router.navigateTo(getOfferScreen())
    }

    override fun openHelpScreen() {
        router.navigateTo(getHelpScreen())
    }

    override fun openLanguageScreen() {
        router.navigateTo(getLanguageScreen())
    }

    override fun openBranchesScreen() {
        router.navigateTo(getBranchesScreen())
    }
}