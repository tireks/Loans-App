package com.tirexmurina.leonov.di

import android.content.Context
import android.content.SharedPreferences
import com.tirexmurina.util.core.sharedPrefs.TOKEN_SHARED_PREFS_NAME
import com.tirexmurina.util.source.data.AuthTokenDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

private fun provideSharedPrefs(context: Context): SharedPreferences =
    context.getSharedPreferences(TOKEN_SHARED_PREFS_NAME, Context.MODE_PRIVATE)

private fun provideSharedPrefsImpl(sharedPreferences: SharedPreferences) =
    AuthTokenDataStore(sharedPreferences)


fun provideDataModule(): Module =
    module {
        single { provideSharedPrefs(androidContext()) }
        single { provideSharedPrefsImpl(sharedPreferences = get()) }
    }