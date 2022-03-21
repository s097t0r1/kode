package com.s097t0r1.kode.ui

sealed class Result<out T>() {
    class Success<out T : Any>(val data: T) : Result<T>()
    class Loading() : Result<Nothing>()
    class Failure(val throwable: Throwable) : Result<Nothing>()
}
