package com.tirexmurina.feature.loan.home.homeTab.homeScreen.presentation

interface HomeRouter {

    fun openOnboarding(needOnboarding : Boolean)

    fun openResultScreen(resultSuccessful : Boolean)

    fun openSpecialOfferScreen()

    fun openHelpScreen()

    fun openLanguageScreen()

    fun openBranchesScreen()

}