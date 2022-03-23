package com.s097t0r1.kode.ui.main

import com.s097t0r1.data.mock.mockUsers
import com.s097t0r1.domain.entities.User
import com.s097t0r1.kode.ui.main.managers.UsersManager

sealed class MainViewState {
    object InitialLoadingUsers : MainViewState() { val users: List<User> = mockUsers }
    object CriticalError : MainViewState()
    class DisplayUsersByAlphabetically(val users: List<User>) : MainViewState()
    class DisplayUsersByBirthday(val birthdayTuple: UsersManager.UsersBirthdayTuple) : MainViewState()
    object EmptySearchResult : MainViewState()
}

