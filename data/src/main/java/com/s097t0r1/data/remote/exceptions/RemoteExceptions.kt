package com.s097t0r1.data.remote.exceptions

import java.net.UnknownHostException

class NoInternetConnectionException() : Exception()

fun Throwable.convertToRemoteException(): Exception {
    return when (this) {
        is UnknownHostException -> NoInternetConnectionException()
        else -> Exception()
    }
}