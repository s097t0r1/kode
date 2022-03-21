package com.s097t0r1.data.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class Users(
    val items: List<RemoteUser>
)
