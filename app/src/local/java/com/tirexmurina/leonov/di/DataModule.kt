package com.tirexmurina.leonov.di

import android.content.Context
import com.tirexmurina.shared.database.core.storage.AppDatabase
import com.tirexmurina.shared.loan.source.data.LoanConditionsGenerator
import com.tirexmurina.shared.loan.source.data.LoanDao
import com.tirexmurina.shared.loan.source.data.models.LoanModelAdapter
import com.tirexmurina.shared.user.source.data.UserDao
import com.tirexmurina.shared.user.source.data.models.UserModelAdapter
import com.tirexmurina.util.source.data.IdDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

private fun provideIdDataStore(context: Context): IdDataStore =
    IdDataStore(context)

private fun provideAppDatabase(app: Context): AppDatabase =
    AppDatabase.getDatabase(app)

private fun provideUserDao(database: AppDatabase): UserDao =
    database.userDao()

private fun provideLoanDao(database: AppDatabase): LoanDao =
    database.loanDao()

private fun provideLoanModelAdapter(): LoanModelAdapter =
    LoanModelAdapter()

private fun provideUserModelAdapter(): UserModelAdapter =
    UserModelAdapter()

private fun provideLoanConditionsGenerator(): LoanConditionsGenerator = LoanConditionsGenerator()

fun provideDataModule(): Module =
    module {
        single { provideIdDataStore(androidContext()) }
        single { provideAppDatabase(androidContext()) }
        single { provideUserDao(database = get()) }
        single { provideLoanDao(database = get()) }
        single { provideLoanModelAdapter() }
        single { provideUserModelAdapter() }
        single { provideLoanConditionsGenerator() }
    }