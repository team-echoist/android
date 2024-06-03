package com.echoist.linkedout.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BasicResponse(
    val path: String,
    val success: Boolean,
    val timestamp: String
)