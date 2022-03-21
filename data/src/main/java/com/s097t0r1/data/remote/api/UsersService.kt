package com.s097t0r1.data.remote.api

import com.s097t0r1.data.remote.entity.Users
import retrofit2.http.GET

const val BASE_URL = "https://stoplight.io/mocks/kode-education/trainee-test/25143926/"

interface UsersService {

    @GET("users")
    suspend fun getUsers(): Users

}