package com.s097t0r1.domain.usecases

import com.s097t0r1.domain.repository.UsersRepository

class GetUsersUseCase(private val usersRepository: UsersRepository) {

    suspend operator fun invoke() =
        usersRepository.getUsers()

}