package com.echoist.linkedout.data


import com.echoist.linkedout.api.EssayApi
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EssayInfo(
    val data: Essays,
    val path: String,
    val success: Boolean,
    val timestamp: String
)
@JsonClass(generateAdapter = true)
data class Essays(
    val essays: List<EssayApi.EssayItem>,
    val total: Int,
)
