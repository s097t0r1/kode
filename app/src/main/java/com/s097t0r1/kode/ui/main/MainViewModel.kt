package com.s097t0r1.kode.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s097t0r1.domain.models.Department
import com.s097t0r1.domain.repository.UsersRepository
import com.s097t0r1.kode.ui.main.components.SortingType
import com.s097t0r1.kode.ui.main.managers.UsersManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel (
    private val repository: UsersRepository
) : ViewModel() {

    private val usersManager = UsersManager()

    private val _viewState = MutableStateFlow<MainViewState>(MainViewState.InitialLoadingUsers)
    val viewState: StateFlow<MainViewState> = _viewState

    private val _viewEffect = MutableStateFlow<MainViewEffect>(MainViewEffect.Empty)
    val viewEffect: StateFlow<MainViewEffect> = _viewEffect

    init {
        getUsers()
        usersManager.flow
            .debounce(500L)
            .onEach { result ->
                when (result) {
                    is UsersManager.Result.Alphabetically -> {
                        when {
                            result.value.isEmpty() -> _viewState.emit(MainViewState.EmptySearchResult)
                            else -> _viewState.emit(MainViewState.DisplayUsersByAlphabetically(result.value))
                        }
                    }
                    is UsersManager.Result.Birthday -> {
                        when {
                            result.value.isEmpty() -> _viewState.emit(MainViewState.EmptySearchResult)
                            else -> _viewState.emit(MainViewState.DisplayUsersByBirthday(result.value))
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun getUsers() {
        viewModelScope.launch {
            _viewState.emit(MainViewState.InitialLoadingUsers)
            val usersResult = repository.getUsers()
            usersResult.fold(
                onSuccess = { users -> usersManager.setUsers(users) },
                onFailure = { throwable -> _viewState.emit(MainViewState.CriticalError) }
            )
        }
    }

    fun refreshUsers() {
        viewModelScope.launch {
            _viewEffect.emit(MainViewEffect.OnSwipeRefresh)
            val userResult = repository.getUsers()
            userResult.fold(
                onSuccess = { users -> usersManager.setUsers(users) },
                onFailure = {}
            )
            _viewEffect.emit(MainViewEffect.Empty)
        }
    }

    fun setDepartment(department: Department?) {
        usersManager.setDepartmentPredicate { user ->
            if (department != null) user.department == department else true
        }
    }

    fun setSearchQuery(searchQuery: String) {
        usersManager.setNamePredicate { user ->
            if (searchQuery.length < 2) return@setNamePredicate true

            return@setNamePredicate user.firstName.contains(searchQuery) ||
                    user.lastName.contains(searchQuery) ||
                    user.userTag.contains(searchQuery)
        }
    }

    fun setSortingType(sortingType: SortingType) {
        usersManager.setSortingType(sortingType)
    }

}