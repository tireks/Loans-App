package com.tirexmurina.leonov.navigation

import com.github.terrakok.cicerone.Router
import com.tirexmurina.feature.common.branches.presentation.BranchesRouter
import com.tirexmurina.feature.loan.home.getMainScreen

class BranchesRouterImpl(private val router: Router) : BranchesRouter {
    override fun openHome() {
        router.navigateTo(getMainScreen())
    }
}