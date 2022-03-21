package com.s097t0r1.kode.ui.main

import com.s097t0r1.domain.entities.Department
import com.s097t0r1.domain.entities.User
import java.util.*

sealed class MainViewState {
    class InitialLoadingUsers(val users: List<User> = mockUsers) : MainViewState()
    object CriticalError : MainViewState()
    data class DisplayUsersByAlphabetically(val users: List<User>) : MainViewState()
    data class DisplayUsersByBirthday(val users: List<User>) : MainViewState()
    object EmptySearchResult : MainViewState()
}

val mockUser = User(
    id = "1234124123",
    avatarUrl = "http://example.com/",
    firstName = "Nikita",
    lastName = "Zmitrovich",
    userTag = "zmitr",
    department = Department.ANDROID,
    position = "Android",
    birthday = Date(System.currentTimeMillis()),
    phone = "+7978243423423"
)

val mockUsers: List<User> = listOf(
    mockUser,
    mockUser,
    mockUser,
    mockUser,
    mockUser
)