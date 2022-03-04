package com.s097t0r1.data.repository

import com.s097t0r1.domain.entities.User
import com.s097t0r1.domain.repository.UsersRepository
import com.s097t0r1.domain.sources.UsersDataSource

class UsersRepositoryImpl(
    private val remoteDataSource: UsersDataSource
) : UsersRepository {

    override suspend fun getUsers(): Result<List<User>> =
        remoteDataSource.getUsers()
}