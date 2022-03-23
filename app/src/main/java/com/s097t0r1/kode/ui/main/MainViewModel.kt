package com.s097t0r1.kode.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s097t0r1.domain.entities.Department
import com.s097t0r1.domain.repository.UsersRepository
import com.s097t0r1.kode.ui.main.managers.UsersManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: UsersRepository
) : ViewModel() {

    private val usersManager = UsersManager()

    private val _viewState = Channel<MainViewState>(Channel.BUFFERED)
    val viewState = _viewState.receiveAsFlow()

    private val _viewEffect = Channel<MainViewEffect>(Channel.BUFFERED)
    val viewEffect = _viewEffect.receiveAsFlow()

    init {
        getUsers()
        usersManager.flow
            .debounce(500L)
            .onEach { result ->
                when (result) {
                    is UsersManager.Result.Alphabetically -> {
                        when {
                            result.value.isEmpty() -> _viewState.send(MainViewState.EmptySearchResult)
                            else -> _viewState.send(MainViewState.DisplayUsersByAlphabetically(result.value))
                        }
                    }
                    is UsersManager.Result.Birthday -> {
                        when {
                            result.value.isEmpty() -> _viewState.send(MainViewState.EmptySearchResult)
                            else -> _viewState.send(MainViewState.DisplayUsersByBirthday(result.value))
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun getUsers() {
        viewModelScope.launch {
            _viewState.send(MainViewState.InitialLoadingUsers)
            val usersResult = repository.getUsers()
            usersResult.fold(
                onSuccess = { users -> usersManager.setUsers(users) },
                onFailure = { throwable -> _viewState.send(MainViewState.CriticalError) }
            )
        }
    }

    fun refreshUsers() {
        viewModelScope.launch {
            _viewEffect.send(MainViewEffect.OnSwipeRefresh)
            val userResult = repository.getUsers()
            userResult.fold(
                onSuccess = { users -> usersManager.setUsers(users) },
                onFailure = {}
            )
            _viewEffect.send(MainViewEffect.Empty)
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

}