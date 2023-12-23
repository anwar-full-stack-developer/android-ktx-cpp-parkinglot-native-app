package com.example.parkinglot.data

import com.google.gson.annotations.SerializedName


data class ResponseParkinglot(
    @SerializedName("data")
    var data: DataParkinglot?,

    @SerializedName("status")
    var status: Int,

    @SerializedName("message")
    var message: String?,
)