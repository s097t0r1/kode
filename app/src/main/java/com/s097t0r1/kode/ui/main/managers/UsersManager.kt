package com.s097t0r1.kode.ui.main.managers

import com.s097t0r1.domain.entities.User
import com.s097t0r1.kode.ui.main.components.SortingType
import com.s097t0r1.kode.utils.toDate
import com.s097t0r1.kode.utils.toLocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters.lastDayOfYear

class UsersManager {

    companion object {
        const val INDEX_OF_NAME_PREDICATE = 0
        const val INDEX_OF_DEPARTMENT_PREDICATE = 1
        const val SIZE_OF_PREDICATES_ARRAY = 2
    }

    private var users: List<User> = emptyList()

    private var sortingType: SortingType = SortingType.BIRTHDAY

    private val _flow = MutableStateFlow<Result>(Result.Initial)
    val flow: StateFlow<Result> = _flow

    private val predicates: Array<((User) -> Boolean)?> = Array(SIZE_OF_PREDICATES_ARRAY) { null }

    fun setUsers(newUsers: List<User>) {
        if (users == newUsers) return

        users = newUsers
        _flow.value = filterAndSort()
    }

    fun setNamePredicate(predicate: (User) -> Boolean) {
        predicates[INDEX_OF_NAME_PREDICATE] = predicate
        _flow.value = filterAndSort()
    }

    fun setDepartmentPredicate(predicate: (User) -> Boolean) {
        predicates[INDEX_OF_DEPARTMENT_PREDICATE] = predicate
        _flow.value = filterAndSort()
    }

    fun setSortingType(type: SortingType) {
        sortingType = type
        _flow.value = filterAndSort()
    }

    private fun filterAndSort(): Result =
        sort(filter(users))

    private fun sort(users: List<User>): Result {
        return when (sortingType) {
            SortingType.ALPHABETICALLY -> {
                Result.Alphabetically(sortByAlphabet(users))
            }
            SortingType.BIRTHDAY -> {
                Result.Birthday(sortByBirthday(users))
            }
        }
    }

    private fun sortByAlphabet(users: List<User>): List<User> =
        users.sortedBy { user -> user.firstName }

    private fun sortByBirthday(users: List<User>): UsersBirthdayTuple {
        val groupedUsers = users
            .map { user ->
                val currentYear = LocalDate.now(ZoneId.systemDefault()).year
                var newBirthday = user.birthday.toLocalDate().withYear(currentYear)

                if (newBirthday < LocalDate.now(ZoneId.systemDefault())) {
                    newBirthday = newBirthday.plusYears(1)
                }

                user.copy(birthday = newBirthday.toDate())
            }
            .sortedBy { it.birthday }
            .groupBy { user ->
                val newYear = LocalDate.now().with(lastDayOfYear())
                user.birthday < newYear.toDate()
            }
        return UsersBirthdayTuple(
            currentYear = groupedUsers[true] ?: emptyList(),
            nextYear = groupedUsers[false] ?: emptyList()
        )
    }

    private fun filter(users: List<User>) =
        predicates.filterNotNull()
            .fold(users) { users, predicate -> users.filter(predicate) }


    sealed class Result {
        object Initial : Result()
        class Alphabetically(val value: List<User>) : Result()
        class Birthday(val value: UsersBirthdayTuple) : Result()
    }

    data class UsersBirthdayTuple(val currentYear: List<User>, val nextYear: List<User>) {

        fun isEmpty() =
            currentYear.isEmpty() && nextYear.isEmpty()
    }
}