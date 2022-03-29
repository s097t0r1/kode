package com.s097t0r1.kode.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            repository.getUser(id).fold(
                onSuccess = {
                    _viewState.value = _viewState.value.copy(isLoading = false, user = it)
                },
                onFailure = {}
            )
        }
    }
}