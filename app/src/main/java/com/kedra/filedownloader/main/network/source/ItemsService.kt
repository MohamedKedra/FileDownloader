package com.kedra.filedownloader.main.network.source

import com.kedra.filedownloader.main.network.models.ItemsResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ItemsService {

    @GET("/movies")
    fun getAllItems() : Single<ItemsResponse>
}