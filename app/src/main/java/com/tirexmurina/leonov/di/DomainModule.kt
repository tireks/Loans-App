package com.tirexmurina.leonov.di

import com.tirexmurina.shared.loan.core.data.LoanRepositoryImpl
import com.tirexmurina.shared.loan.core.data.remote.LoanService
import com.tirexmurina.shared.loan.core.domain.repository.LoanRepository
import com.tirexmurina.shared.loan.core.domain.usecase.GetLoanConditionsUseCase
import com.tirexmurina.shared.loan.core.domain.usecase.GetLoansByIdUseCase
import com.tirexmurina.shared.loan.core.domain.usecase.GetLoansUseCase
import com.tirexmurina.shared.loan.core.domain.usecase.RequestLoanUseCase
import com.tirexmurina.shared.user.core.data.UserRepositoryImpl
import com.tirexmurina.shared.user.core.data.local.SharedPreferencesImpl
import com.tirexmurina.shared.user.core.data.remote.AuthService
import com.tirexmurina.shared.user.core.domain.repository.UserRepository
import com.tirexmurina.shared.user.core.domain.usecase.AskTokenAvailabilityUseCase
import com.tirexmurina.shared.user.core.domain.usecase.ClearTokenUseCase
import com.tirexmurina.shared.user.core.domain.usecase.LoginUserUseCase
import com.tirexmurina.shared.user.core.domain.usecase.RegisterUserUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

private fun provideUserRepository(authService: AuthService, sharedPreferencesImpl: SharedPreferencesImpl) : UserRepository =
    UserRepositoryImpl(authService, sharedPreferencesImpl)

private fun provideLoanRepository(loanService: LoanService) : LoanRepository =
    LoanRepositoryImpl(loanService)

fun provideDomainModule() : Module =
    module {
        single { provideUserRepository(authService = get(), sharedPreferencesImpl = get())}
        single { provideLoanRepository(loanService = get()) }


        factory { LoginUserUseCase(repository = get()) }
        factory { RegisterUserUseCase(repository = get()) }
        factory { GetLoansUseCase(repository = get()) }
        factory { GetLoansByIdUseCase(repository = get()) }
        factory { GetLoanConditionsUseCase(repository = get()) }
        factory { RequestLoanUseCase(repository = get()) }
        factory { AskTokenAvailabilityUseCase(repository = get()) }
        factory { ClearTokenUseCase(repository = get()) }
    }