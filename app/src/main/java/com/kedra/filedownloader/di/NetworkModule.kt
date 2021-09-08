package com.kedra.filedownloader.di

import com.kedra.filedownloader.main.data.network.ItemServices
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideAppService(): ItemServices {
        return Retrofit.Builder()
            .baseUrl("https://nagwa.free.beeceptor.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(ItemServices::class.java)
    }
}