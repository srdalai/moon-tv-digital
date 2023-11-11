package com.moontvdigital.app.data

import com.google.gson.annotations.SerializedName

data class AuthResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data")
	val data: List<UserData?>? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("success")
	val success: String? = null
)

data class UserData(

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("fullname")
	val fullName: String? = null,

	@field:SerializedName("mobileNo")
	val mobileNo: String? = null,

	@field:SerializedName("passcode")
	val passcode: String? = null,

	@field:SerializedName("user_type_id")
	val userTypeId: String? = null,

	@field:SerializedName("user_type")
	val userType: String? = null
)
