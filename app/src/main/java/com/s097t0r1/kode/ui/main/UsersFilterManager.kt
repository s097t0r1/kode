package com.s097t0r1.kode.ui.main

import com.s097t0r1.domain.entities.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

class UsersFilterManager() {

    companion object {
        const val INDEX_OF_NAME_PREDICATE = 0
        const val INDEX_OF_DEPARTMENT_PREDICATE = 1
        const val SIZE_OF_PREDICATES_ARRAY = 2
    }

    private var users: List<User> = emptyList()

    private val _flow = MutableStateFlow<List<User>?>(null)
    val flow: Flow<List<User>> = _flow.filterNotNull()

    private val predicates: Array<((User) -> Boolean)?> = Array(SIZE_OF_PREDICATES_ARRAY) { null }

    fun setUsers(newUsers: List<User>) {
        if (users == newUsers) return

        users = newUsers
        filter()
    }

    fun setNamePredicate(predicate: (User) -> Boolean): UsersFilterManager {
        predicates[INDEX_OF_NAME_PREDICATE] = predicate
        filter()
        return this
    }

    fun setDepartmentPredicate(predicate: (User) -> Boolean): UsersFilterManager {
        predicates[INDEX_OF_DEPARTMENT_PREDICATE] = predicate
        filter()
        return this
    }

    private fun filter() {
        _flow.value = predicates.filterNotNull()
            .fold(users) { users, predicate -> users.filter(predicate) }
    }
}