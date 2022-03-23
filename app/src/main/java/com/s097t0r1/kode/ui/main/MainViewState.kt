package com.s097t0r1.kode.ui.main

import com.s097t0r1.data.mock.mockUsers
import com.s097t0r1.domain.entities.User
import com.s097t0r1.kode.ui.main.managers.UsersManager

sealed class MainViewState {
    class InitialLoadingUsers(val users: List<User> = mockUsers) : MainViewState()
    object CriticalError : MainViewState()
    data class DisplayUsersByAlphabetically(val users: List<User>) : MainViewState()
    data class DisplayUsersByBirthday(val birthdayTuple: UsersManager.UsersBirthdayTuple) : MainViewState()
    object EmptySearchResult : MainViewState()
}

