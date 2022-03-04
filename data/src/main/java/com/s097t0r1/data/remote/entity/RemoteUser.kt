package com.s097t0r1.data.remote.entity

import com.s097t0r1.domain.entities.Department
import com.s097t0r1.domain.entities.User
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Serializable
data class RemoteUser(
    val id: String,

    val avatarUrl: String,

    val firstName: String,

    val lastName: String,

    val userTag: String,

    val department: String,

    val position: String,

    val birthDay: String,

    val phone: String
)

fun List<RemoteUser>.toDomainModel(): List<User> {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
    return this.map {
        User(
            id = it.id,
            avatarUrl = it.avatarUrl,
            firstName = it.firstName,
            lastName = it.lastName,
            userTag = it.userTag,
            department = Department.valueOf(it.department),
            position = it.position,
            birthday = dateFormatter.parse(it.birthDay) ?: throw RuntimeException("Cannot parse date"),
            phone = it.phone
        )
    }
}
