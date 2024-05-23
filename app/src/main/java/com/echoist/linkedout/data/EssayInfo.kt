package com.echoist.linkedout.data


import com.echoist.linkedout.api.EssayApi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EssayInfo(
    @Json(name = "data")
    val `data`: Essays,
    @Json(name = "path")
    val path: String,
    @Json(name = "success")
    val success: Boolean,
    @Json(name = "timestamp")
    val timestamp: String
)
@JsonClass(generateAdapter = true)
data class Essays(
    @Json(name = "essays")
    val essays: List<EssayApi.EssayItem>,
    @Json(name = "page")
    val page: Int,
    @Json(name = "total")
    val total: Int,
    @Json(name = "totalPage")
    val totalPage: Int
)
