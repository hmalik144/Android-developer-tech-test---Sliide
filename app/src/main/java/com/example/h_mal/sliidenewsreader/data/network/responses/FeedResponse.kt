package com.example.h_mal.sliidenewsreader.data.network.responses

import com.example.h_mal.sliidenewsreader.data.models.Image


data class FeedResponse (
    val id: Int?,
    val datatype: String?,
    val type: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val images: List<Image>?
)