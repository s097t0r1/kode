package com.s097t0r1.data.di

import com.s097t0r1.data.remote.RemoteUsersDataSource
import com.s097t0r1.data.repository.UsersRepositoryImpl
import com.s097t0r1.domain.repository.UsersRepository
import com.s097t0r1.domain.sources.UsersDataSource
import org.koin.dsl.module

val userModule = module {
     
    single<UsersDataSource> { RemoteUsersDataSource(get()) }

    single<UsersRepository> { UsersRepositoryImpl(get()) }

}