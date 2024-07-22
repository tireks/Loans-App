package com.tirexmurina.leonov.di

import android.content.Context
import android.content.SharedPreferences
import com.tirexmurina.shared.user.core.data.local.AuthTokenDataStore
import com.tirexmurina.util.core.sharedPrefs.SHARED_PREFS_NAME
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

private fun provideSharedPrefs(context: Context): SharedPreferences =
    context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

private fun provideSharedPrefsImpl(sharedPreferences: SharedPreferences) =
    AuthTokenDataStore(sharedPreferences)

fun provideDataModule(): Module =
    module {
        single { provideSharedPrefs(androidContext()) }
        single{ provideSharedPrefsImpl(sharedPreferences = get()) }
    }