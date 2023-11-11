package com.moontvdigital.app.data

import com.google.gson.annotations.SerializedName

data class StateListResponse(

	@field:SerializedName("code")
	val code: String,

	@field:SerializedName("data")
	val stateList: List<StateItem>,

	@field:SerializedName("success")
	val success: String,

	@field:SerializedName("message")
	val message: String
)

data class StateItem(

	@field:SerializedName("state_name")
	val stateName: String,

	@field:SerializedName("state_id")
	val stateId: String
)
