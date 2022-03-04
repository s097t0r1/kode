package com.s097t0r1.domain.entities

import java.util.*

data class User(
    val id: String,

    val avatarUrl: String,

    val firstName: String,

    val lastName: String,

    val userTag: String,

    val department: Department,

    val position: String,

    val birthday: Date,

    val phone: String
)
