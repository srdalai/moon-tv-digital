package com.moontvdigital.app.data

import com.google.gson.annotations.SerializedName

data class GetHallsResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data")
	val hallDataList: List<HallData?>? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("success")
	val success: String? = null
)

data class HallData(

	@field:SerializedName("hall_id")
	val hallId: Int? = null,

	@field:SerializedName("hall_name")
	val hallName: String? = null,

	@field:SerializedName("hall_timing")
	val hallTiming: String? = null,

	@field:SerializedName("hall_film_thumbnail")
	val hallFilmThumbnail: String? = null
)
