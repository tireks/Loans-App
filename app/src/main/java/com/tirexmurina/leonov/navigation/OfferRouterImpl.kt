package com.tirexmurina.leonov.navigation

import com.github.terrakok.cicerone.Router
import com.tirexmurina.feature.common.branches.getBranchesScreen
import com.tirexmurina.feature.common.offer.presentation.OfferRouter

class OfferRouterImpl(private val router: Router) : OfferRouter {
    override fun openHome() {
        router.exit()
    }

    override fun openBranches() {
        router.navigateTo(getBranchesScreen())
    }
}