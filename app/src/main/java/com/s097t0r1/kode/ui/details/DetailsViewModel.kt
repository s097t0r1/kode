package com.s097t0r1.kode.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s097t0r1.domain.Result
import com.s097t0r1.domain.repository.UsersRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repository: UsersRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow(DetailsViewState())
    val viewState: StateFlow<DetailsViewState> = _viewState

    private val _events = Channel<DetailsEvents>()

    init {
        viewModelScope.launch {
            _events.receiveAsFlow().collect {
                reduce(_viewState.value, it)
            }
        }
    }

    private fun reduce(oldState: DetailsViewState, event: DetailsEvents) {
        _viewState.value = when (event) {
            is DetailsEvents.GettingUser -> oldState.copy(isLoading = false, user = event.user)
        }
    }

    fun getUser(id: String) {
        viewModelScope.launch {
            val userResult = repository.getUser(id)
            when (userResult) {
                is Result.Success -> _events.send(DetailsEvents.GettingUser(userResult.data))
                is Result.Failure -> {

                }
            }
        }
    }
}