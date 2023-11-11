package com.moontvdigital.app.data

import com.google.gson.annotations.SerializedName

data class ShowTimesResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data")
	val showTimes: List<ShowTime?>? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("success")
	val success: String? = null
)

data class ShowTime(

	@field:SerializedName("show_end_time")
	val showEndTime: String? = null,

	@field:SerializedName("hall_id")
	val hallId: Int? = null,

	@field:SerializedName("show_start_time")
	val showStartTime: String? = null,

	@field:SerializedName("show_date")
	val showDate: String? = null,

	@field:SerializedName("show_time_id")
	val showTimeId: Int? = null,

	@field:SerializedName("hall_name")
	val hallName: String? = null,

	@field:SerializedName("film_name")
	val filmName: String? = null,

	@field:SerializedName("film_id")
	val filmId: Int? = null,

	@field:SerializedName("ticket_rate")
	val ticketRate: Any? = null
)
