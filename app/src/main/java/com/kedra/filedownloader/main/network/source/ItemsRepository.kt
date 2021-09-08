package com.kedra.filedownloader.main.network.source

import com.kedra.filedownloader.main.network.models.ItemsResponse
import io.reactivex.Single
import javax.inject.Inject

class ItemsRepository @Inject constructor(private val itemsService: ItemsService) {

    fun getListItems(): Single<ItemsResponse> {

        return itemsService.getAllItems()
    }
}