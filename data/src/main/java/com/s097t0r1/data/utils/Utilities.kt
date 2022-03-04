package com.s097t0r1.data.utils

import kotlinx.coroutines.CoroutineScope

suspend fun <T> CoroutineScope.safeLaunch(unsafeBlock: suspend () -> T): Result<T> {
    try {
        return Result.success(unsafeBlock.invoke())
    } catch (e: Exception) {
        return Result.failure(e)
    }
}