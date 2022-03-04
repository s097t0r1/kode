package com.s097t0r1.domain.sources

import com.s097t0r1.domain.entities.User

interface UsersDataSource {

    suspend fun getUsers(): Result<List<User>>

}