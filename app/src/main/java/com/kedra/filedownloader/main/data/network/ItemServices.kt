package com.kedra.filedownloader.main.data.network

import com.kedra.filedownloader.main.data.models.AllItemsResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ItemServices {

    @GET("/movies")
    fun getAllItems() : Single<AllItemsResponse>
}