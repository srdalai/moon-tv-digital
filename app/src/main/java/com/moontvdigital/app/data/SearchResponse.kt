package com.moontvdigital.app.data

import com.google.gson.annotations.SerializedName

data class SearchResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("item_count")
	val itemCount: String? = null,

	@field:SerializedName("search")
	val search: List<ContentItem>? = null,

	@field:SerializedName("code")
	val code: Int? = null
)
