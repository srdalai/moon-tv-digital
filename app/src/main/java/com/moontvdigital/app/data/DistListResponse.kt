package com.moontvdigital.app.data

import com.google.gson.annotations.SerializedName

data class DistListResponse(

	@field:SerializedName("code")
	val code: String,

	@field:SerializedName("data")
	val distList: List<DistItem>,

	@field:SerializedName("success")
	val success: String,

	@field:SerializedName("message")
	val message: String
)

data class DistItem(

	@field:SerializedName("district_name")
	val districtName: String,

	@field:SerializedName("district_id")
	val districtId: String
)
