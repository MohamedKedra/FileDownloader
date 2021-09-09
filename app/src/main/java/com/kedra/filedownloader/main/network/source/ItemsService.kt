package com.kedra.filedownloader.main.network.source

import com.kedra.filedownloader.main.network.models.ItemsResponse
import com.kedra.filedownloader.main.network.models.ItemsResponseItem
import io.reactivex.Single
import retrofit2.http.GET

interface ItemsService {

    @GET("movies")
    fun getAllItems() : Single<List<ItemsResponseItem>>
}