package com.kedra.filedownloader.main.di

import com.kedra.filedownloader.main.network.source.ItemsService
import com.kedra.filedownloader.utils.Endpoints
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class NetworkModule {

    @Singleton
    @Provides
    fun getInstance() : ItemsService {

        return Retrofit.Builder()
            .baseUrl(Endpoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(ItemsService::class.java)
    }
}