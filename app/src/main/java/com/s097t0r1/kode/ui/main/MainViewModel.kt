package com.s097t0r1.kode.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s097t0r1.domain.entities.Department
import com.s097t0r1.domain.repository.UsersRepository
import com.s097t0r1.kode.ui.main.managers.UsersFilterManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: UsersRepository
) : ViewModel() {

    private val usersManager = UsersFilterManager()

    private val _viewState = Channel<MainViewState>(Channel.BUFFERED)
    val viewState = _viewState.receiveAsFlow()

    init {
        getUsers()
        usersManager.flow
            .debounce(500L)
            .onEach {
                if (it.isEmpty())
                    _viewState.send(MainViewState.EmptySearchResult)
                else
                    _viewState.send(MainViewState.DisplayUsersByAlphabetically(it))
            }.launchIn(viewModelScope)
    }

    fun getUsers() {
        viewModelScope.launch {
            _viewState.send(MainViewState.InitialLoadingUsers())
            val usersResult = repository.getUsers()
            usersResult.fold(
                onSuccess = { users -> usersManager.setUsers(users) },
                onFailure = { throwable -> _viewState.send(MainViewState.CriticalError) }
            )
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