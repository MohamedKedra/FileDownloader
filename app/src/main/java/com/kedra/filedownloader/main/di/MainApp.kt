package com.kedra.filedownloader.main.di

import android.app.Application

class MainApp : Application() {

    companion object{ lateinit var appComponent: AppComponent }

    override fun onCreate() {
        super.onCreate()
    }

}