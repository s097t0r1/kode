package com.s097t0r1.domain.sources

import com.s097t0r1.domain.models.User

interface LocalUsersDataSource {

    suspend fun getUser(id: String): Result<User>

    suspend fun insertUsers(users: List<User>)

}