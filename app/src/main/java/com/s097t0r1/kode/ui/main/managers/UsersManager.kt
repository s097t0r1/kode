package com.s097t0r1.kode.ui.main.managers

import com.s097t0r1.domain.models.User
import com.s097t0r1.kode.utils.toDate
import com.s097t0r1.kode.utils.toLocalDate
import java.time.Clock
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters.lastDayOfYear

class UsersManager {

    companion object {
        private const val INDEX_OF_NAME_PREDICATE = 0
        private const val INDEX_OF_DEPARTMENT_PREDICATE = 1
        private const val SIZE_OF_PREDICATES_ARRAY = 2
    }

    private var users: List<User> = emptyList()
    private val predicates: Array<((User) -> Boolean)> =
        Array(SIZE_OF_PREDICATES_ARRAY) { { true } }

    fun setUsers(newUsers: List<User>): UsersManager {
        if (users == newUsers) return this

        users = newUsers
        return this
    }

    fun setNamePredicate(predicate: (User) -> Boolean): UsersManager {
        predicates[INDEX_OF_NAME_PREDICATE] = predicate
        return this
    }

    fun setDepartmentPredicate(predicate: (User) -> Boolean): UsersManager {
        predicates[INDEX_OF_DEPARTMENT_PREDICATE] = predicate
        return this
    }

    fun sortByAlphabet(): List<User> =
        filter(users).sortedBy { user -> user.firstName }

    fun sortByBirthday(clock: Clock = Clock.systemDefaultZone()): Pair<List<User>, List<User>> {
        filter(users)
            .map { user ->
                val currentYear = LocalDate.now(clock).year
                var newBirthday = user.birthday.toLocalDate().withYear(currentYear)

                if (newBirthday < LocalDate.now(clock)) {
                    newBirthday = newBirthday.plusYears(1)
                }

                user.copy(birthday = newBirthday.toDate())
            }
            .sortedBy { it.birthday }
            .groupBy { user ->
                val newYear = LocalDate.now(clock).with(lastDayOfYear())
                user.birthday < newYear.toDate()
            }
            // Map to users with normal birthdays
            .mapValues { entry ->
                entry.value.map { user ->
                    users.find { it.id == user.id } ?: throw IllegalStateException("User should be found")
                }
            }
            .also {
                return Pair(
                    first = it[true] ?: emptyList(),
                    second = it[false] ?: emptyList()
                )
            }
    }

    private fun filter(users: List<User>) =
        predicates.fold(users) { foldedUser, predicate ->  foldedUser.filter(predicate) }
}