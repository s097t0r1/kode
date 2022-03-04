package com.s097t0r1.data.utils

suspend fun <T> safeLaunch(unsafeBlock: suspend () -> T): Result<T> {
    try {
        return Result.success(unsafeBlock.invoke())
    } catch (e: Exception) {
        return Result.failure(e)
    }
}