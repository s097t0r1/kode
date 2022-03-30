package com.s097t0r1.kode.ui.main

import com.s097t0r1.domain.models.User
import com.s097t0r1.kode.ui.main.components.DepartmentTabs
import com.s097t0r1.kode.ui.main.components.SortingType

data class MainViewState(

    val isInitialLoading: Boolean = true,

    val currentSearchQuery: String = "",

    val currentDepartmentTab: DepartmentTabs = DepartmentTabs.ALL,

    val currentSortingType: SortingType = SortingType.ALPHABETICALLY,

    val criticalError: Boolean = false,

    val alphabetUsers: List<User> = emptyList(),

    val birthdayTuple: Pair<List<User>, List<User>> = Pair(emptyList(), emptyList()),

    val isEmptySearchResult: Boolean = false,
)

sealed class MainEvents {
    object InitialLoading : MainEvents()
    class ChangeDepartmentTab(val newDepartment: DepartmentTabs) : MainEvents()
    class ChangeSortingType(val sortingType: SortingType) : MainEvents()
    class ChangeSearchQuery(val searchQuery: String) : MainEvents()
    class DisplayUsers(
        val users: List<User> = emptyList(),
        val tuple: Pair<List<User>, List<User>> = Pair(emptyList(), emptyList())
    ) : MainEvents()
    object CriticalError : MainEvents()
    object EmptySearch : MainEvents()
}

sealed class MainViewEffect() {
    object Empty : MainViewEffect()
    object Refreshing : MainViewEffect()
}