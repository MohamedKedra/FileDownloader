package com.kedra.filedownloader.main.di

import com.kedra.filedownloader.main.view.ui.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, ViewModelModule::class])
interface AppComponent {

    fun injectFragment(mainFragment: MainFragment)
}