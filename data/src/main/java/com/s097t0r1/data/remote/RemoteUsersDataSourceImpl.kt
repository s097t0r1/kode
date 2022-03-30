package com.s097t0r1.data.remote

import com.s097t0r1.data.remote.api.UsersService
import com.s097t0r1.data.remote.models.toDomainModel
import com.s097t0r1.data.utils.safeLaunch
import com.s097t0r1.domain.Result
import com.s097t0r1.domain.models.User
import com.s097t0r1.domain.sources.RemoteUsersDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteUsersDataSourceImpl(
    private val usersService: UsersService
) : RemoteUsersDataSource {

    override suspend fun getUsers(): Result<List<User>> = withContext(Dispatchers.IO) {
        safeLaunch {
            val remoteUsers = usersService.getUsers()
            withContext(Dispatchers.Default) {
                return@withContext remoteUsers.items.toDomainModel()
            }
        }
    }
}