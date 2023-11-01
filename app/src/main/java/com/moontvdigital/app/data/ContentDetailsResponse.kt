package com.moontvdigital.app.data

import com.google.gson.annotations.SerializedName

data class ContentDetailsResponse(

    @field:SerializedName("msg")
    val msg: String? = null,


    @field:SerializedName("code")
    val code: Int? = null,


    @field:SerializedName("movie")
    val movie: Movie? = null,

    @field:SerializedName("searchTags")
    val searchTags: List<SearchTagsItem?>? = null,

    @field:SerializedName("rating")
    val rating: String? = null
)

data class CastDetailItem(

    @field:SerializedName("celeb_name")
    val celebName: String? = null,

    @field:SerializedName("celeb_id")
    val celebId: String? = null,

    @field:SerializedName("cast_type")
    val castType: String? = null,

    @field:SerializedName("celeb_image")
    val celebImage: String? = null,

    @field:SerializedName("permalink")
    val permalink: String? = null
)

data class Movie(

    @field:SerializedName("trailer_status")
    val trailerStatus: Int? = null,

    @field:SerializedName("tv_banner")
    val tvBanner: String? = null,

    @field:SerializedName("content_publish_date")
    val contentPublishDate: String? = null,


    @field:SerializedName("movieUrl")
    val movieUrl: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("content_type_id")
    val contentTypeId: String? = null,

    @field:SerializedName("trailerUrl")
    val trailerUrl: String? = null,

    @field:SerializedName("gmt_timeUTC")
    val gmtTimeUTC: String? = null,

    @field:SerializedName("start_time")
    val startTime: String? = null,

    @field:SerializedName("movieUrlForTv")
    val movieUrlForTv: String? = null,

    @field:SerializedName("release_date")
    val releaseDate: String? = null,

    @field:SerializedName("is_downloadable")
    val isDownloadable: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("is_livestream_enabled")
    val isLivestreamEnabled: Int? = null,

    @field:SerializedName("is_episode")
    val isEpisode: String? = null,


    @field:SerializedName("movie_stream_uniq_id")
    val movieStreamUniqId: String? = null,

    @field:SerializedName("muvi_uniq_id")
    val muviUniqId: String? = null,


    @field:SerializedName("permalink")
    val permalink: String? = null,

    @field:SerializedName("video_duration")
    val videoDuration: String? = null,

    @field:SerializedName("posterForTv")
    val posterForTv: String? = null,

    @field:SerializedName("content_publish_end_date")
    val contentPublishEndDate: String? = null,


    @field:SerializedName("censor_rating")
    val censorRating: String? = null,

    @field:SerializedName("content_language")
    val contentLanguage: String? = null,

    @field:SerializedName("duration")
    val duration: String? = null,


    @field:SerializedName("genre")
    val genre: String? = null,

    @field:SerializedName("custom3")
    val custom3: String? = null,


    @field:SerializedName("content_types_id")
    val contentTypesId: String? = null,

    @field:SerializedName("video_resolution")
    val videoResolution: String? = null,

    @field:SerializedName("movie_stream_id")
    val movieStreamId: String? = null,


    @field:SerializedName("story")
    val story: String? = null
)

data class SearchTagsItem(

    @field:SerializedName("tag_name")
    val tagName: String? = null,

    @field:SerializedName("tag_id")
    val tagId: String? = null
)
