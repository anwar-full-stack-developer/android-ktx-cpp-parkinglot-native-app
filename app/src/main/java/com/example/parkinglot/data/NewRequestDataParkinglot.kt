package com.example.parkinglot.data

import com.google.gson.annotations.SerializedName

data class NewRequestDataParkinglot(

    @SerializedName("token_number")
    var token_number: String,

    @SerializedName("type")
    var type: String,

    @SerializedName("size")
    var size: String,

    @SerializedName("weight")
    var weight: String,

    @SerializedName("booking_time")
    var booking_time: String,

    @SerializedName("booking_time_to")
    var booking_time_to: String,

    @SerializedName("booking_status")
    var booking_status: String,

    @SerializedName("price")
    var price: String,

    @SerializedName("status")
    var status: String,

    )