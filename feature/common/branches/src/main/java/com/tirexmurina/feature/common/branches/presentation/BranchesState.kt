package com.tirexmurina.feature.common.branches.presentation

sealed interface BranchesState {

    data object Initial : BranchesState

    data object Content : BranchesState

}