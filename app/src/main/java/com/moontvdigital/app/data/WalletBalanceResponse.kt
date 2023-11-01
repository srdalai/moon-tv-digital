package com.moontvdigital.app.data

import com.google.gson.annotations.SerializedName

data class WalletBalanceResponse(

    @field:SerializedName("Message")
    val message: String? = null,

    @field:SerializedName("Data")
    val walletData: List<WalletData?>? = null,

    @field:SerializedName("Code")
    val code: String? = null,

    @field:SerializedName("Success")
    val success: String? = null
)

data class WalletData(

    @field:SerializedName("wallet_balance")
    val walletBalance: String? = null
)
