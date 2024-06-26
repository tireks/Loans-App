package com.tirexmurina.leonov.navigation

import com.github.terrakok.cicerone.Router
import com.tirexmurina.feature.common.branches.getBranchesScreen
import com.tirexmurina.feature.common.result.presentation.ResultRouter

class ResultRouterImpl(private val router: Router) : ResultRouter {
    override fun openBranches() {
        router.navigateTo(getBranchesScreen())
    }

    override fun openHome() {
        router.exit()
    }
}