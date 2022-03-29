package com.s097t0r1.data.di

import com.s097t0r1.data.local.LocalUsersDataSourceImpl
import com.s097t0r1.data.remote.RemoteUsersDataSourceImpl
import com.s097t0r1.data.repository.UsersRepositoryImpl
import com.s097t0r1.domain.repository.UsersRepository
import com.s097t0r1.domain.sources.LocalUsersDataSource
import com.s097t0r1.domain.sources.RemoteUsersDataSource
import org.koin.dsl.module

val userModule = module {
     
    single<RemoteUsersDataSource> { RemoteUsersDataSourceImpl(get()) }

    single<LocalUsersDataSource> { LocalUsersDataSourceImpl() }

    single<UsersRepository> { UsersRepositoryImpl(get(), get()) }

}