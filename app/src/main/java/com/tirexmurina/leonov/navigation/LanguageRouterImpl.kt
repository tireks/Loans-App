package com.tirexmurina.leonov.navigation

import com.github.terrakok.cicerone.Router
import com.tirexmurina.feature.common.language.presentation.LanguageRouter

class LanguageRouterImpl(private val router: Router) : LanguageRouter {
    override fun openHome() {
        router.exit()
    }
}