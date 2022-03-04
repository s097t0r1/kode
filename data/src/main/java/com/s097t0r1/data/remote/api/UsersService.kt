package com.s097t0r1.data.remote.api

import com.s097t0r1.data.remote.entity.RemoteUser
import retrofit2.http.GET

interface UsersService {

    @GET("/users")
    suspend fun getUsers(): List<RemoteUser>

}