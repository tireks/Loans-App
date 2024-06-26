package com.tirexmurina.feature.common.result

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.tirexmurina.feature.common.result.ui.ResultFragment

fun getResultScreen(successResult : Boolean) = FragmentScreen { ResultFragment.newInstance(successResult) }