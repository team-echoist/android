package com.echoist.linkedout.data


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
    val essays: List<EssayItem>,
    @Json(name = "page")
    val page: Int,
    @Json(name = "total")
    val total: Int,
    @Json(name = "totalPage")
    val totalPage: Int
)

@JsonClass(generateAdapter = true)
data class EssayItem(
    @Json(name = "content")
    val content: String,
    @Json(name = "createdDate")
    val createdDate: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "linkedOut")
    val linkedOut: Boolean,
    @Json(name = "linkedOutGauge")
    val linkedOutGauge: Int,
    @Json(name = "published")
    val published: Boolean,
    @Json(name = "thumbnail")
    val thumbnail: String? = null,
    @Json(name = "title")
    val title: String,
    @Json(name = "updatedDate")
    val updatedDate: String
)