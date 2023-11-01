package com.moontvdigital.app.api

import com.moontvdigital.app.data.BannerImageResponse
import com.moontvdigital.app.data.ContentDetailsResponse
import com.moontvdigital.app.data.GetHallsResponse
import com.moontvdigital.app.data.SearchResponse
import com.moontvdigital.app.data.ShowTimesResponse
import com.moontvdigital.app.data.WalletBalanceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("GetBannerImages")
    fun getBannerItems(): Call<BannerImageResponse>

    @GET("searchData")
    fun searchData(
        @Query("authToken") authToken: String,
        @Query("q") query: String
    ): Call<SearchResponse>

    @GET("getContentDetails")
    fun getContentDetails(
        @Query("authToken") authToken: String,
        @Query("permalink") permalink: String
    ): Call<ContentDetailsResponse>

    @GET("GetHallList")
    fun getHallList(): Call<GetHallsResponse>

    @GET("GetShowTime")
    fun getShowTimes(
        @Query("hallId") hallId: String,
        @Query("showDate") showDate: String
    ): Call<ShowTimesResponse>

    @GET("GetWalletBalance")
    fun getWalletBalance(
        @Query("userId") userId: String
    ): Call<WalletBalanceResponse>
}