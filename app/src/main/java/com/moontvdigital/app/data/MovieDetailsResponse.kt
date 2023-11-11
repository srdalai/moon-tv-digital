package com.moontvdigital.app.data

import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("data")
    val data: List<MovieItem?>? = null,

    @field:SerializedName("success")
    val success: String? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class MovieItem(

    @field:SerializedName("film_starcast")
    val castItems: List<CastItem>? = null,

    @field:SerializedName("has_trailer")
    val hasTrailer: String? = null,

    @field:SerializedName("film_duration")
    val filmDuration: String? = null,

    @field:SerializedName("trailer_path")
    val trailerPath: String? = null,

    @field:SerializedName("film_name")
    val filmName: String? = null,

    @field:SerializedName("film_long_description")
    val filmLongDescription: String? = null,

    @field:SerializedName("film_language")
    val filmLanguage: String? = null,

    @field:SerializedName("film_genre")
    val filmGenre: String? = null,

    @field:SerializedName("film_id")
    val filmId: Int? = null,

    @field:SerializedName("film_path")
    val filmPath: String? = null,

    @field:SerializedName("film_banner")
    val bannerItems: List<MovieBannerItem>? = null,
)

data class CastItem(

    @field:SerializedName("actor_image_path")
    val actorImagePath: String? = null,

    @field:SerializedName("actor_name")
    val actorName: String? = null,

    @field:SerializedName("actor_type")
    val actorType: String? = null
)

data class MovieBannerItem(
    @field:SerializedName("film_banner_path")
    val bannerPath: String? = null,
)
