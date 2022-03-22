package com.s097t0r1.kode.utils

import java.time.LocalDate
import java.time.ZoneId
import java.util.*

fun LocalDate.toDate() =
    Date.from(this.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())

fun Date.toLocalDate() =
    this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()