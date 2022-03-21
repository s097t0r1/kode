package com.s097t0r1.kode.di

import com.s097t0r1.kode.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {

    viewModel { MainViewModel(get()) }

}