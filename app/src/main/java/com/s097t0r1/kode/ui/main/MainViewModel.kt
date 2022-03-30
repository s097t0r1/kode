package com.s097t0r1.kode.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s097t0r1.domain.Result
import com.s097t0r1.domain.models.Department
import com.s097t0r1.domain.repository.UsersRepository
import com.s097t0r1.kode.ui.main.components.DepartmentTabs
import com.s097t0r1.kode.ui.main.components.SortingType
import com.s097t0r1.kode.ui.main.managers.UsersManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: UsersRepository
) : ViewModel() {

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

        usersManager.flow
            .debounce(500L)
            .onEach { result ->
                when (result) {
                    is UsersManager.Result.Alphabetically -> {
                        when {
                            result.value.isEmpty() -> events.send(MainEvents.EmptySearch)
                            else -> events.send(MainEvents.DisplayUsers(users = result.value))
                        }
                    }
                    is UsersManager.Result.Birthday -> {
                        when {
                            result.value.isEmpty() -> events.send(MainEvents.EmptySearch)
                            else -> events.send(
                                MainEvents.DisplayUsers(
                                    tuple = Pair(
                                        result.value.currentYear,
                                        result.value.nextYear
                                    )
                                )
                            )
                        }
                    }
                }
            }.launchIn(viewModelScope)
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
    }

    fun setSortingType(sortingType: SortingType) {
        viewModelScope.launch {
            events.send(MainEvents.ChangeSortingType(sortingType))
        }
        usersManager.setSortingType(sortingType)
    }

}