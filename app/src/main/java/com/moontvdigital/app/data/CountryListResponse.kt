package com.moontvdigital.app.data

import com.google.gson.annotations.SerializedName

data class CountryListResponse(

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("data")
    val countryList: List<CountryItem>? = null,

    @field:SerializedName("success")
    val success: String? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class CountryItem(

    @field:SerializedName("country_name")
    val countryName: String,

    @field:SerializedName("country_id")
    val countryId: String
)
