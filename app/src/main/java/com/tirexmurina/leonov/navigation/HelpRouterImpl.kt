package com.tirexmurina.leonov.navigation

import com.github.terrakok.cicerone.Router
import com.tirexmurina.feature.common.help.presentation.HelpRouter

class HelpRouterImpl(private val router: Router) : HelpRouter {
    override fun openHome() {
        router.exit()
    }


}