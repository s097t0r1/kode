package com.s097t0r1.kode.ui.details

import com.s097t0r1.data.mock.mockUser
import com.s097t0r1.domain.entities.User

data class DetailsViewState(

    val isLoading: Boolean = true,

    val user: User = mockUser
)