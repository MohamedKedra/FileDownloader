package com.kedra.filedownloader.main.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {

    @Binds
    abstract fun getAppContext(application: Application)  : Context
}