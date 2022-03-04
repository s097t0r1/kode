package com.s097t0r1.domain.repository

import com.s097t0r1.domain.entities.User

interface UsersRepository {

    suspend fun getUsers(): Result<List<User>>

}