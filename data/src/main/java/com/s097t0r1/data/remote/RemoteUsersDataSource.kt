package com.s097t0r1.data.remote

import com.s097t0r1.data.remote.api.UsersService
import com.s097t0r1.data.remote.entity.toDomainModel
import com.s097t0r1.data.utils.safeLaunch
import com.s097t0r1.domain.entities.User
import com.s097t0r1.domain.sources.UsersDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteUsersDataSource(
    private val usersService: UsersService
) : UsersDataSource {

    override suspend fun getUsers(): Result<List<User>> = withContext(Dispatchers.IO) {
        safeLaunch {
            val remoteUsers = usersService.getUsers()
            withContext(Dispatchers.Default) {
                return@withContext remoteUsers.items.toDomainModel()
            }
        }
    }
}