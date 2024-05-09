package com.echoist.linkedout.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WritingUserInfo(
    @Json(name = "data")
    val data: Data,
    @Json(name = "path")
    val path: String,
    @Json(name = "success")
    val success: Boolean,
    @Json(name = "timestamp")
    val timestamp: String
)
@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "content")
    val content: String?,
    @Json(name = "createdDate")
    val createdDate: String?,
    @Json(name = "id")
    val id: Int?, // todo essay id값 받아서 내가 쓴 글들은 room에 저장해놔야할듯?
    @Json(name = "linkedOut")
    val linkedOut: Boolean?,
    @Json(name = "linkedOutGauge")
    val linkedOutGauge: Int?,
    @Json(name = "published")
    val published: Boolean,
    @Json(name = "thumbnail")
    val thumbnail: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "updatedDate")
    val updatedDate: String?
)

