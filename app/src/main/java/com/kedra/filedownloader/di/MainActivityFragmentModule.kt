package com.kedra.filedownloader.di

import com.kedra.filedownloader.main.view.ui.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityFragmentModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): MainFragment
}