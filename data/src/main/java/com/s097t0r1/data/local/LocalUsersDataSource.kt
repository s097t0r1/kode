package com.s097t0r1.data.local

import com.s097t0r1.domain.models.User
import com.s097t0r1.domain.sources.LocalUsersDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalUsersDataSourceImpl : LocalUsersDataSource {

    var usersList: List<User> = emptyList()

    override suspend fun getUser(id: String): Result<User> = withContext(Dispatchers.Main) {
        val user = usersList.find { it.id == id }
        if (user == null) {
            return@withContext Result.failure(RuntimeException("Users with id not found"))
        } else {
            return@withContext Result.success(user)
        }
    }

    override suspend fun insertUsers(users: List<User>) = withContext(Dispatchers.Main) {
        usersList = users
    }

}