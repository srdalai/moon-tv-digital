package com.moontvdigital.app.data

import com.google.gson.annotations.SerializedName

data class ContentItem(
    @field:SerializedName("posterForTv")
    val posterForTv: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("permalink")
    val permalink: String? = null,

    @field:SerializedName("story")
    val story: String? = null
)