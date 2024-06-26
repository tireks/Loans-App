package com.tirexmurina.feature.common.onboarding

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.tirexmurina.feature.common.onboarding.ui.OnboardingFragment

fun getOnboardingScreen(userIsNewbie : Boolean) = FragmentScreen { OnboardingFragment.newInstance(userIsNewbie) }