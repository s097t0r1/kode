package com.s097t0r1.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.s097t0r1.data.remote.api.BASE_URL
import com.s097t0r1.data.remote.api.UsersService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {

    single<UsersService> { get<Retrofit>().create(UsersService::class.java) }

    single {
        val contentType = "application/json".toMediaType()
        Retrofit.Builder().apply {
            baseUrl(BASE_URL)
            addConverterFactory(Json.asConverterFactory(contentType))
        }.build()
    }
}