package com.s097t0r1.data.repository

import com.s097t0r1.domain.models.User
import com.s097t0r1.domain.repository.UsersRepository
import com.s097t0r1.domain.sources.LocalUsersDataSource
import com.s097t0r1.domain.sources.RemoteUsersDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsersRepositoryImpl(
    private val remoteUsersDataSource: RemoteUsersDataSource,
    private val localUsersDataSource: LocalUsersDataSource,
) : UsersRepository {

    override suspend fun getUsers(): Result<List<User>> = withContext(Dispatchers.Default) {
        val remoteResult = remoteUsersDataSource.getUsers()
        remoteResult.onSuccess {
            localUsersDataSource.insertUsers(it)
        }
        return@withContext remoteResult
    }

    override suspend fun getUser(id: String): Result<User> = withContext(Dispatchers.Default) {
        return@withContext localUsersDataSource.getUser(id)
    }
}