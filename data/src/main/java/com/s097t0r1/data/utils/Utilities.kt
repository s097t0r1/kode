package com.s097t0r1.data.utils

import com.s097t0r1.domain.Result
import kotlinx.coroutines.CoroutineScope

suspend fun <T> CoroutineScope.safeLaunch(unsafeBlock: suspend () -> T): Result<T> {
    return try {
        Result.Success(unsafeBlock.invoke())
    } catch (e: Exception) {
        Result.Failure(e)
    }
}