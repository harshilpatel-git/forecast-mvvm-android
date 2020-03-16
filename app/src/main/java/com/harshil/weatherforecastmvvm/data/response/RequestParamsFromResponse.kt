package com.harshil.weatherforecastmvvm.data.response


import com.google.gson.annotations.SerializedName

data class RequestParamsFromResponse(
    @SerializedName("language")
    val language: String,
    @SerializedName("query")
    val query: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("unit")
    val unit: String
)