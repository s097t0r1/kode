package com.s097t0r1.domain.repository

import com.s097t0r1.domain.models.User

interface UsersRepository {

    suspend fun getUsers(): Result<List<User>>

    suspend fun getUser(id: String): Result<User>


}