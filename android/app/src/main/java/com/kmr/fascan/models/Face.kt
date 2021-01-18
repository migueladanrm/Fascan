package com.kmr.fascan.models

import com.google.gson.annotations.SerializedName

data class Face(
    @SerializedName("id")
    val id: String,
    @SerializedName("probability")
    val probability: Float,
    @SerializedName("resource")
    val resourceUrl: String
)
