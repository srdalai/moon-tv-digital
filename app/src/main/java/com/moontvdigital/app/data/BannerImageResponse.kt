package com.moontvdigital.app.data

import com.google.gson.annotations.SerializedName

data class BannerImageResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val bannerItems: List<BannerItem?>? = null,

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("success")
    val success: String? = null
)

data class BannerItem(

    @field:SerializedName("file_path")
    val filePath: String? = null
)
