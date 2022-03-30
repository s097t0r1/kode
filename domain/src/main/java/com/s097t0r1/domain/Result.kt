package com.s097t0r1.domain

sealed class Result<out T>() {
    class Success<out T>(val data: T) : Result<T>()
    class Failure(val throwable: Throwable) : Result<Nothing>()
}
