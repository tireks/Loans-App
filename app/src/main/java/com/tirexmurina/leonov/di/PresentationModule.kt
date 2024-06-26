package com.tirexmurina.leonov.di

import com.tirexmurina.feature.common.branches.presentation.BranchesRouter
import com.tirexmurina.feature.common.branches.presentation.BranchesViewModel
import com.tirexmurina.feature.common.help.presentation.HelpRouter
import com.tirexmurina.feature.common.help.presentation.HelpViewModel
import com.tirexmurina.feature.common.language.presentation.LanguageRouter
import com.tirexmurina.feature.common.language.presentation.LanguageViewModel
import com.tirexmurina.feature.common.offer.presentation.OfferRouter
import com.tirexmurina.feature.common.offer.presentation.OfferViewModel
import com.tirexmurina.feature.common.result.presentation.ResultRouter
import com.tirexmurina.feature.common.result.presentation.ResultViewModel
import com.tirexmurina.feature.loan.home.homeTab.homeScreen.presentation.HomeRouter
import com.tirexmurina.feature.loan.home.homeTab.homeScreen.presentation.HomeViewModel
import com.tirexmurina.feature.loan.home.homeTab.requestScreen.presentation.RequestViewModel
import com.tirexmurina.feature.loan.home.menuTab.detailsScreen.presentation.DetailsViewModel
import com.tirexmurina.feature.loan.home.menuTab.loansScreen.presentation.LoansViewModel
import com.tirexmurina.feature.loan.home.menuTab.menuScreen.presentation.MenuViewModel
import com.tirexmurina.feature.user.start.presentation.StartRouter
import com.tirexmurina.feature.user.start.presentation.StartViewModel
import com.tirexmurina.feature.user.start.presentation.bottomConrainer.login.LoginViewModel
import com.tirexmurina.feature.user.start.presentation.bottomConrainer.registration.RegistrationViewModel
import com.tirexmurina.leonov.navigation.BranchesRouterImpl
import com.tirexmurina.leonov.navigation.HelpRouterImpl
import com.tirexmurina.leonov.navigation.HomeRouterImpl
import com.tirexmurina.leonov.navigation.LanguageRouterImpl
import com.tirexmurina.leonov.navigation.OfferRouterImpl
import com.tirexmurina.leonov.navigation.OnboardingRouterImpl
import com.tirexmurina.leonov.navigation.ResultRouterImpl
import com.tirexmurina.leonov.navigation.StartRouterImpl
import com.tirexmurina.feature.common.onboarding.presentation.OnboardingRouter
import com.tirexmurina.feature.common.onboarding.presentation.OnboardingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

fun providePresentationModule(): Module =
    module {

        viewModel{
            StartViewModel(
                loginUserUseCase = get(),
                registerUserUseCase = get(),
                askTokenAvailability = get(),
                router = get()
            )
        }

        factory<StartRouter> { StartRouterImpl(get()) }


        viewModel{
            OnboardingViewModel(get())
        }

        factory <OnboardingRouter>{ OnboardingRouterImpl(get()) }



        viewModel{
            HomeViewModel(
                getLoanConditionsUseCase = get(),
                getLoansUseCase = get(),
                router = get()
            )
        }

        factory <HomeRouter>{ HomeRouterImpl(get()) }

        viewModel{
            RequestViewModel(
                getLoanConditionsUseCase = get(),
                requestLoanUseCase = get(),
                router = get()
            )
        }

        viewModel{
            LoansViewModel(
                getLoansUseCase = get()
            )
        }

        viewModel {
            DetailsViewModel(
                getLoansByIdUseCase = get()
            )
        }

        viewModel {
            MenuViewModel(
                clearTokenUseCase = get(),
                router = get()
            )
        }

        viewModel {
            ResultViewModel(
                router = get()
            )
        }

        factory <ResultRouter>{ ResultRouterImpl(get()) }

        viewModel {
            BranchesViewModel(
                router = get()
            )
        }

        factory<BranchesRouter>{BranchesRouterImpl(get())}

        viewModel {
            OfferViewModel(
                router = get()
            )
        }

        factory<OfferRouter>{ OfferRouterImpl(get()) }

        viewModel {
            HelpViewModel(
                router = get()
            )
        }

        factory<HelpRouter> { HelpRouterImpl(get()) }

        viewModel {
            LanguageViewModel(
                router = get()
            )
        }

        factory<LanguageRouter> { LanguageRouterImpl(get()) }

        viewModel {
            LoginViewModel()
        }

        viewModel {
            RegistrationViewModel()
        }

    }