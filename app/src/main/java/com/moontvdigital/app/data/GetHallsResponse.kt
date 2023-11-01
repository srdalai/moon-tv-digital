package com.moontvdigital.app.data

import com.google.gson.annotations.SerializedName

data class GetHallsResponse(

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("Data")
	val hallDataList: List<HallData?>? = null,

	@field:SerializedName("Code")
	val code: String? = null,

	@field:SerializedName("Success")
	val success: String? = null
)

data class HallData(

	@field:SerializedName("hall_id")
	val hallId: Int? = null,

	@field:SerializedName("hall_name")
	val hallName: String? = null
)
