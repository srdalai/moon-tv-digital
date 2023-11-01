package com.moontvdigital.app.data

import com.google.gson.annotations.SerializedName

data class BannerImageResponse(

    @field:SerializedName("Message")
    val message: String? = null,

    @field:SerializedName("Data")
    val bannerItems: List<BannerItem?>? = null,

    @field:SerializedName("Code")
    val code: String? = null,

    @field:SerializedName("Success")
    val success: String? = null
)

data class BannerItem(

    @field:SerializedName("file_path")
    val filePath: String? = null
)
