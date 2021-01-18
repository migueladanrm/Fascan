package com.kmr.fascan.models

import com.google.gson.annotations.SerializedName

data class Face(
    @SerializedName("id")
    val id: String,
    @SerializedName("distance")
    val distance: Float,
    @SerializedName("resource")
    val resourceUrl: String
)
