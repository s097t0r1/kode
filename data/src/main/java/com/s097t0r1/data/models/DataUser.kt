package com.s097t0r1.data.models

import android.os.Parcelable
import com.s097t0r1.domain.models.Department
import com.s097t0r1.domain.models.User
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class DataUser(
    val id: String,

    val avatarUrl: String,

    val firstName: String,

    val lastName: String,

    val userTag: String,

    val department: String,

    val position: String,

    val birthday: Long,

    val phone: String

) : Parcelable

fun User.toDataModel() =
    DataUser(
        id = this.id,
        avatarUrl = this.avatarUrl,
        firstName = this.firstName,
        lastName = this.lastName,
        userTag = this.userTag,
        department = this.department.name,
        position = this.position,
        birthday = this.birthday.time,
        phone = this.phone
    )

fun DataUser.toDomainModel() =
    User(
        id = this.id,
        avatarUrl = this.avatarUrl,
        firstName = this.firstName,
        lastName = this.lastName,
        userTag = this.userTag,
        department = Department.valueOf(this.department),
        position = this.position,
        birthday = Date(this.birthday),
        phone = this.phone
    )