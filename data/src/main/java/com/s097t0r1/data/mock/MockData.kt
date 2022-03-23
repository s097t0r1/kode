package com.s097t0r1.data.mock

import com.s097t0r1.domain.entities.Department
import com.s097t0r1.domain.entities.User
import java.util.*

val mockUser = User(
    id = "1234124123",
    avatarUrl = "http://example.com/",
    firstName = "Nikita",
    lastName = "Zmitrovich",
    userTag = "ZM",
    department = Department.ANDROID,
    position = "Android",
    birthday = Date(System.currentTimeMillis()),
    phone = "+7978243423423"
)

val mockUsers: List<User> = MutableList(10) { mockUser }