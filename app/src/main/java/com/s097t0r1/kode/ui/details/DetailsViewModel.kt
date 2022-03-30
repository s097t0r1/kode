package com.s097t0r1.kode.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s097t0r1.domain.Result
import com.s097t0r1.domain.repository.UsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repository: UsersRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow(DetailsViewState())
    val viewState: StateFlow<DetailsViewState> = _viewState

    fun getUser(id: String) {
        viewModelScope.launch {
            val userResult = repository.getUser(id)
            // TODO add events and reducer
            when (userResult) {
                is Result.Success -> _viewState.emit(
                    _viewState.value.copy(
                        isLoading = false,
                        user = userResult.data
                    )
                )
                is Result.Failure -> {

                }
            }
        }
    }
}