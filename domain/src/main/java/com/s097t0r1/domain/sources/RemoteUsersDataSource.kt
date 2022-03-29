package com.s097t0r1.domain.sources

import com.s097t0r1.domain.models.User

interface RemoteUsersDataSource {

    suspend fun getUsers(): Result<List<User>>

}