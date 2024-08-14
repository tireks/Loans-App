package com.tirexmurina.leonov.di

import com.tirexmurina.shared.loan.core.domain.repository.LoanRepository
import com.tirexmurina.shared.loan.core.domain.usecase.GetLoanConditionsUseCase
import com.tirexmurina.shared.loan.core.domain.usecase.GetLoansByIdUseCase
import com.tirexmurina.shared.loan.core.domain.usecase.GetLoansUseCase
import com.tirexmurina.shared.loan.core.domain.usecase.RequestLoanUseCase
import com.tirexmurina.shared.loan.source.LoanRepositoryImpl
import com.tirexmurina.shared.loan.source.data.LoanConditionsGenerator
import com.tirexmurina.shared.loan.source.data.LoanDao
import com.tirexmurina.shared.loan.source.data.models.LoanModelAdapter
import com.tirexmurina.shared.user.core.domain.repository.UserRepository
import com.tirexmurina.shared.user.core.domain.usecase.AskSessionAvailabilityUseCase
import com.tirexmurina.shared.user.core.domain.usecase.ClearTokenUseCase
import com.tirexmurina.shared.user.core.domain.usecase.LoginUserUseCase
import com.tirexmurina.shared.user.core.domain.usecase.RegisterUserUseCase
import com.tirexmurina.shared.user.source.UserRepositoryImpl
import com.tirexmurina.shared.user.source.data.UserDao
import com.tirexmurina.shared.user.source.data.models.UserModelAdapter
import com.tirexmurina.util.source.data.IdDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

private fun provideDispatcher(): CoroutineDispatcher {
    return Dispatchers.IO
}

private fun provideUserRepository(
    userDao: UserDao,
    userModelAdapter: UserModelAdapter,
    idDataStore: IdDataStore,
    dispatcherIO: CoroutineDispatcher
): UserRepository =
    UserRepositoryImpl(
        userDao,
        userModelAdapter,
        idDataStore,
        dispatcherIO
    )

private fun provideLoanRepository(
    loanDao: LoanDao,
    loanModelAdapter: LoanModelAdapter,
    idDataStore: IdDataStore,
    loanConditionsGenerator: LoanConditionsGenerator,
    dispatcherIO: CoroutineDispatcher
): LoanRepository =
    LoanRepositoryImpl(
        loanDao,
        idDataStore,
        loanModelAdapter,
        loanConditionsGenerator,
        dispatcherIO
    )

fun provideDomainModule(): Module =
    module {
        single { provideDispatcher() }
        single {
            provideUserRepository(
                userDao = get(),
                userModelAdapter = get(),
                idDataStore = get(),
                dispatcherIO = get()
            )
        }
        single {
            provideLoanRepository(
                loanDao = get(),
                idDataStore = get(),
                loanModelAdapter = get(),
                loanConditionsGenerator = get(),
                dispatcherIO = get()
            )
        }


        factory { LoginUserUseCase(repository = get()) }
        factory { RegisterUserUseCase(repository = get()) }
        factory { GetLoansUseCase(repository = get()) }
        factory { GetLoansByIdUseCase(repository = get()) }
        factory { GetLoanConditionsUseCase(repository = get()) }
        factory { RequestLoanUseCase(repository = get()) }
        factory { AskSessionAvailabilityUseCase(repository = get()) }
        factory { ClearTokenUseCase(repository = get()) }
    }