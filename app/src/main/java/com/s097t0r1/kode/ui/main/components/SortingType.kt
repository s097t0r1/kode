package com.s097t0r1.kode.ui.main.components

import androidx.annotation.StringRes
import com.s097t0r1.kode.R

enum class SortingType(@StringRes val value: Int) {
    ALPHABETICALLY(R.string.sorting_by_alphabet),
    BIRTHDAY(R.string.sorting_by_birthday)
}
