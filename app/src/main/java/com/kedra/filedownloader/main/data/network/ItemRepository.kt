package com.kedra.filedownloader.main.data.network

import com.kedra.filedownloader.main.data.models.AllItemsResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class MainRepository @Inject constructor(private val itemServices: ItemServices) {

    fun getListItems(): Single<AllItemsResponse> {

        return itemServices.getAllItems()
    }
}