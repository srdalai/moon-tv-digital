package com.moontvdigital.app.data

import com.google.gson.annotations.SerializedName

data class HomeContentResponse(

    @field:SerializedName("code")
    val code: String,

    @field:SerializedName("data")
    val homeContents: List<HomeContent>,

    @field:SerializedName("success")
    val success: String,

    @field:SerializedName("message")
    val message: String
)

data class HomeContent(

    @field:SerializedName("film_name")
    val filmName: String,

    @field:SerializedName("film_id")
    val filmId: Int,

    @field:SerializedName("film_path")
    val filmPath: String
)
