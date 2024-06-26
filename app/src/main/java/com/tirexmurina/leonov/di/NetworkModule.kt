package com.tirexmurina.leonov.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tirexmurina.leonov.gson.LocalDateTimeGsonAdapter
import com.tirexmurina.shared.loan.core.data.remote.LoanService
import com.tirexmurina.shared.user.core.data.local.SharedPreferencesImpl
import com.tirexmurina.shared.user.core.data.remote.TokenGetInterceptor
import com.tirexmurina.shared.user.core.data.remote.AuthService
import com.tirexmurina.util.core.service.BASE_URL
import okhttp3.OkHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

private const val LOANS_BASE_URL = BASE_URL
private const val CONNECT_TIMEOUT = 10L
private const val WRITE_TIMEOUT = 10L
private const val READ_TIMEOUT = 10L


private fun provideAuthInterceptor(sharedPreferencesImpl: SharedPreferencesImpl): TokenGetInterceptor =
    TokenGetInterceptor(sharedPreferencesImpl)


private fun provideOkHttpClient(authInterceptor: TokenGetInterceptor): OkHttpClient =
    OkHttpClient().newBuilder()
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(authInterceptor)
        .cache(null)
        .build()

private fun provideGson(): Gson =
    GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeGsonAdapter)
        .create()

private fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit =
    Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(LOANS_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

private fun provideAuthService(retrofit: Retrofit): AuthService = retrofit.create()

private fun provideLoanService(retrofit: Retrofit): LoanService = retrofit.create()

fun provideNetworkModule(): Module =
    module {
        single { provideAuthInterceptor(sharedPreferencesImpl = get()) }
        single { provideOkHttpClient(authInterceptor = get()) }
        single { provideGson() }
        single { provideRetrofit(okHttpClient = get(), gson = get()) }
        single { provideAuthService(retrofit = get()) }
        single { provideLoanService(retrofit = get()) }
    }