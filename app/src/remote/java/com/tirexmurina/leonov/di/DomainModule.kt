package com.tirexmurina.leonov.di

import com.tirexmurina.shared.loan.core.domain.repository.LoanRepository
import com.tirexmurina.shared.loan.core.domain.usecase.GetLoanConditionsUseCase
import com.tirexmurina.shared.loan.core.domain.usecase.GetLoansByIdUseCase
import com.tirexmurina.shared.loan.core.domain.usecase.GetLoansUseCase
import com.tirexmurina.shared.loan.core.domain.usecase.RequestLoanUseCase
import com.tirexmurina.shared.loan.source.LoanRepositoryImpl
import com.tirexmurina.shared.loan.source.data.LoanService
import com.tirexmurina.shared.user.core.domain.repository.UserRepository
import com.tirexmurina.shared.user.core.domain.usecase.AskSessionAvailabilityUseCase
import com.tirexmurina.shared.user.core.domain.usecase.ClearTokenUseCase
import com.tirexmurina.shared.user.core.domain.usecase.LoginUserUseCase
import com.tirexmurina.shared.user.core.domain.usecase.RegisterUserUseCase
import com.tirexmurina.shared.user.source.UserRepositoryImpl
import com.tirexmurina.shared.user.source.data.AuthService
import com.tirexmurina.util.source.data.AuthTokenDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

private fun provideUserRepository(
    authService: AuthService,
    authTokenDataStore: AuthTokenDataStore
): UserRepository =
    UserRepositoryImpl(
        authService,
        authTokenDataStore
    )

private fun provideLoanRepository(
    loanService: LoanService
): LoanRepository =
    LoanRepositoryImpl(
        loanService
    )

fun provideDomainModule(): Module =
    module {
        single { provideUserRepository(authService = get(), authTokenDataStore = get()) }
        single { provideLoanRepository(loanService = get()) }


        factory { LoginUserUseCase(repository = get()) }
        factory { RegisterUserUseCase(repository = get()) }
        factory { GetLoansUseCase(repository = get()) }
        factory { GetLoansByIdUseCase(repository = get()) }
        factory { GetLoanConditionsUseCase(repository = get()) }
        factory { RequestLoanUseCase(repository = get()) }
        factory { AskSessionAvailabilityUseCase(repository = get()) }
        factory { ClearTokenUseCase(repository = get()) }
    }