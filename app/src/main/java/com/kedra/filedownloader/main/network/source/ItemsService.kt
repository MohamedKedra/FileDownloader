package com.kedra.filedownloader.main.network.source

import com.kedra.filedownloader.main.network.models.ItemsResponseItem
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url


interface ItemsService {

    @GET("movies")
    fun getAllItems() : Single<List<ItemsResponseItem>>

    @GET
    fun downloadFileWithDynamicUrl(@Url fileUrl: String?): Call<ResponseBody?>?
}