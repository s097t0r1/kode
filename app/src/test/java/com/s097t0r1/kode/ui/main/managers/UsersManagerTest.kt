package com.s097t0r1.kode.ui.main.managers

import com.google.common.truth.Truth.assertThat
import com.s097t0r1.data.mock.mockUser
import com.s097t0r1.domain.models.Department
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.*

class UsersManagerTest {

    @Test
    fun filterAndSort_InitialState_AlphaResultEmptyList() {
        val manager = UsersManager()

        assertThat(manager.sortByAlphabet()).isEmpty()
    }

    @Test
    fun setNamePredicate_HasUserMatchingPredicate_AlphaResultNotEmptyList() {
        val matchingUser = mockUser.copy(firstName = "Nikita")
        val notMatchingUser = mockUser.copy(firstName = "John")
        val users = listOf(matchingUser, notMatchingUser)

        val result = UsersManager()
            .setUsers(users)
            .setNamePredicate { it.firstName.startsWith('N') }
            .sortByAlphabet()

        assertThat(result).hasSize(1)

    }

    @Test
    fun setDepartmentPredicate_HasUserMatchingPredicate_AlphaResultNotEmptyList() {
        val matchingUser = mockUser.copy(department = Department.ANALYTICS)
        val notMatchingUser = mockUser.copy(firstName = "John")
        val users = listOf(matchingUser, notMatchingUser)

        val result = UsersManager()
            .setUsers(users)
            .setDepartmentPredicate { it.department == Department.ANALYTICS }
            .sortByAlphabet()

        assertThat(result).hasSize(1)
    }

    @Test
    fun sortByAlphabet_NotEmptyList_ListSortedByFirstName() {
        val user1 = mockUser.copy(firstName = "Alice")
        val user2 = mockUser.copy(firstName = "John")
        val user3 = mockUser.copy(firstName = "Zula")
        val users = listOf(user2, user1, user3)

        val result = UsersManager()
            .setUsers(users)
            .sortByAlphabet()

        assertThat(result).containsExactlyElementsIn(listOf(user1, user2, user3))
    }

    @Test
    fun sortByBirthday_NotEmptyList_TupleSortedByNearestBirthdayAndDivideByYear() {
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val user1 = mockUser.copy(birthday = simpleDateFormat.parse("14-06-2001")!!)
        val user2 = mockUser.copy(id = "1241498", birthday = simpleDateFormat.parse("14-03-1999")!!)

        val testClock = Clock.fixed(Instant.parse("2022-04-04T10:00:00.00Z"), ZoneId.systemDefault())

        val users = listOf(user2, user1)

        val result = UsersManager().setUsers(users).sortByBirthday(testClock)

        assertThat(result.first).containsExactly(user1)
        assertThat(result.second).containsExactly(user2)
    }

    @Test
    fun sortByBirthday_BirthsOnlyInTheCurrentYear_TupleSortedByNearestBirthWithEmptySecondElement() {
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val user1 = mockUser.copy(birthday = simpleDateFormat.parse("14-06-2001")!!)
        val user2 = mockUser.copy(id = "1241498", birthday = simpleDateFormat.parse("14-07-1999")!!)

        val testClock = Clock.fixed(Instant.parse("2022-04-04T10:00:00.00Z"), ZoneId.systemDefault())

        val users = listOf(user2, user1)

        val result = UsersManager().setUsers(users).sortByBirthday(testClock)

        assertThat(result.first).containsExactly(user1, user2)
        assertThat(result.second).isEmpty()
    }


}