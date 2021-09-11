package com.kedra.filedownloader.main.network.source

import com.kedra.filedownloader.main.network.models.ItemsResponseItem
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject

class ItemsRepository @Inject constructor(private val itemsService: ItemsService) {

    fun getListItems(): Single<List<ItemsResponseItem>> {

        return itemsService.getAllItems()
    }

    fun getFileDownloaded(url: String): Call<ResponseBody?>? {
        return itemsService.downloadFileWithDynamicUrl(url)
    }
}