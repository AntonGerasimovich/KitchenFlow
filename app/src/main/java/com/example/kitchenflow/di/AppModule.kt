package com.example.kitchenflow.di

import com.example.kitchenflow.data.repository.KitchenRepository
import com.example.kitchenflow.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

}
val viewModelModule = module {
    fun provideRepository() = KitchenRepository()
    single { provideRepository() }
    viewModel { MainViewModel(get()) }
}
