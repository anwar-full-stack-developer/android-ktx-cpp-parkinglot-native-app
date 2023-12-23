package com.example.parkinglot.data

import com.google.gson.annotations.SerializedName

data class ResponseListParkinglot(
    @SerializedName("data")
    var data: List<DataParkinglot>?,

    @SerializedName("status")
    var status: Int,

    @SerializedName("message")
    var message: String,
)