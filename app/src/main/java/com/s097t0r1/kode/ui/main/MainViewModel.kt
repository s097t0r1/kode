package com.s097t0r1.kode.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.s097t0r1.data.remote.exceptions.NoInternetConnectionException
import com.s097t0r1.domain.Result
import com.s097t0r1.domain.models.Department
import com.s097t0r1.domain.repository.UsersRepository
import com.s097t0r1.kode.R
import com.s097t0r1.kode.ui.main.components.DepartmentTabs
import com.s097t0r1.kode.ui.main.components.SortingType
import com.s097t0r1.kode.ui.main.managers.UsersManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val context: Application,
    private val repository: UsersRepository
) : AndroidViewModel(context) {

    private val usersManager = UsersManager()

    private val _viewState = MutableStateFlow<MainViewState>(MainViewState())
    val viewState: StateFlow<MainViewState> = _viewState

    private val _viewEffect = MutableSharedFlow<MainViewEffect>()
    val viewEffect: SharedFlow<MainViewEffect> = _viewEffect

    private val events = Channel<MainEvents>()

    init {
        events.receiveAsFlow().onEach {
            reduce(_viewState.value, it)
        }.launchIn(viewModelScope)

        getUsers()
    }

    private fun reduce(oldState: MainViewState, event: MainEvents) {
        _viewState.value = when (event) {
            is MainEvents.InitialLoading -> oldState.copy(
                criticalError = false,
                isInitialLoading = true
            )
            is MainEvents.CriticalError -> oldState.copy(
                isInitialLoading = false,
                criticalError = true
            )
            is MainEvents.ChangeDepartmentTab -> oldState.copy(currentDepartmentTab = event.newDepartment)
            is MainEvents.ChangeSortingType -> oldState.copy(currentSortingType = event.sortingType)
            is MainEvents.ChangeSearchQuery -> oldState.copy(currentSearchQuery = event.searchQuery)
            is MainEvents.EmptySearch -> oldState.copy(isEmptySearchResult = true)
            is MainEvents.DisplayUsers -> oldState.copy(
                isInitialLoading = false,
                criticalError = false,
                isEmptySearchResult = false,
                alphabetUsers = event.users,
                birthdayTuple = event.tuple
            )
        }
    }

    fun getUsers() {
        viewModelScope.launch {
            events.send(MainEvents.InitialLoading)
            val usersResult = repository.getUsers()
            when (usersResult) {
                is Result.Success -> {
                    usersManager.setUsers(usersResult.data)
                    filterAndSortUsers()
                }
                is Result.Failure -> {
                    events.send(MainEvents.CriticalError)
                }
            }
        }
    }

    fun refreshUsers() {
        viewModelScope.launch {
            _viewEffect.emit(MainViewEffect.Refreshing)
            val userResult = repository.getUsers()
            when (userResult) {
                is Result.Success -> usersManager.setUsers(userResult.data)
                is Result.Failure -> {
                    val errorMessage = when (userResult.throwable) {
                        is NoInternetConnectionException -> context.getString(R.string.no_internet_connection_snackbar_text)
                        else -> context.getString(R.string.unknown_error_snackbar_text)
                    }
                    _viewEffect.emit(MainViewEffect.ErrorSnackBar(errorMessage))
                    delay(3000)
                    _viewEffect.emit(MainViewEffect.Empty)
                }
            }
            _viewEffect.emit(MainViewEffect.Empty)
        }
    }

    fun setDepartment(department: Department?) {
        viewModelScope.launch {
            val departmentTab = DepartmentTabs.values().find {
                it.department == department
            } ?: throw RuntimeException("Department tab not found")
            events.send(MainEvents.ChangeDepartmentTab(departmentTab))
        }
        usersManager.setDepartmentPredicate { user ->
            if (department != null) user.department == department else true
        }
        filterAndSortUsers()
    }

    fun setSearchQuery(searchQuery: String) {
        viewModelScope.launch {
            events.send(MainEvents.ChangeSearchQuery(searchQuery))
        }
        usersManager.setNamePredicate { user ->
            if (searchQuery.length < 2) return@setNamePredicate true

            return@setNamePredicate user.firstName.contains(searchQuery) ||
                    user.lastName.contains(searchQuery) ||
                    user.userTag.contains(searchQuery)
        }
        filterAndSortUsers()
    }

    fun setSortingType(sortingType: SortingType) {
        viewModelScope.launch {
            events.send(MainEvents.ChangeSortingType(sortingType))
            filterAndSortUsers()
        }
    }

    private fun filterAndSortUsers() {
        viewModelScope.launch {
            when (_viewState.value.currentSortingType) {
                SortingType.ALPHABETICALLY -> {
                    val sortedByAlphabetUsers = usersManager.sortByAlphabet()
                    if (sortedByAlphabetUsers.isEmpty()) {
                        events.send(MainEvents.EmptySearch)
                    } else {
                        events.send(
                            MainEvents.DisplayUsers(users = sortedByAlphabetUsers)
                        )
                    }
                }
                SortingType.BIRTHDAY -> {
                    val sortedByBirthdayUsers = usersManager.sortByBirthday()
                    if (sortedByBirthdayUsers.first.isEmpty() && sortedByBirthdayUsers.second.isEmpty()) {
                        events.send(MainEvents.EmptySearch)
                    } else {
                        events.send(MainEvents.DisplayUsers(tuple = sortedByBirthdayUsers))
                    }
                }
            }
        }
    }

}