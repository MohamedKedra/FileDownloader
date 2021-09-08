package com.kedra.filedownloader.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        AndroidInjectionModule::class,
        ActivityModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent