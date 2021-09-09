package com.kedra.filedownloader.main.network.models

data class ItemsResponseItem(
    val id: Int,
    val name: String,
    val type: String,
    val url: String
)