package com.moontvdigital.app.api

import com.moontvdigital.app.data.AuthResponse
import com.moontvdigital.app.data.BannerImageResponse
import com.moontvdigital.app.data.DistListResponse
import com.moontvdigital.app.data.CountryListResponse
import com.moontvdigital.app.data.GetHallsResponse
import com.moontvdigital.app.data.MovieDetailsResponse
import com.moontvdigital.app.data.ShowTimesResponse
import com.moontvdigital.app.data.StateListResponse
import com.moontvdigital.app.data.WalletBalanceResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("GetCountryList")
    fun getCountryList(): Call<CountryListResponse>

    @GET("GetStateList")
    fun getStateList(@Query("countryId") countryId: String): Call<StateListResponse>

    @GET("GetDistrictList")
    fun getDistrictList(@Query("stateId") stateId: String): Call<DistListResponse>

    @GET("GetBannerImages")
    fun getBannerItems(): Call<BannerImageResponse>

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

    @GET("GetFilmDetails")
    fun getFilmDetails(
        @Query("filmId") filmId: String
    ): Call<MovieDetailsResponse>

    @FormUrlEncoded
    @POST("RegisterAccount")
    fun userRegister(
        @Field("_countryId") countryId: String,
        @Field("_stateId") stateId: String,
        @Field("_districtId") districtId: String,
        @Field("_fullName") fullName: String,
        @Field("_mobileNo") mobileNo: String,
        @Field("_passcode") passcode: String
    ): Call<AuthResponse>

    @FormUrlEncoded
    @POST("LoginAccount")
    fun userLogin(
        @Field("_mobileNo") mobileNo: String,
        @Field("_passcode") passcode: String
    ): Call<AuthResponse>
}