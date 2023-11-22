package com.moontvdigital.app.data

import com.google.gson.annotations.SerializedName

data class WalletBalanceResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val walletData: List<WalletData?>? = null,

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("success")
    val success: String? = null
)

data class WalletData(

    @field:SerializedName("wallet_balance")
    val walletBalance: String? = null
)
