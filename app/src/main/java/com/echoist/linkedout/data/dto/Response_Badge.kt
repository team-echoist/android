package com.echoist.linkedout.data.dto


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BadgeSimpleResponse(
    val data: Data,
    val path: String,
    val success: Boolean,
    val timestamp: String
)

@JsonClass(generateAdapter = true)
data class Data(
    val badges: List<Badge>?
)

@JsonClass(generateAdapter = true)
data class Badge(
    val exp: Int,
    val id: Int? = null,
    val level: Int,
    val name: String
)

@JsonClass(generateAdapter = true)
data class BadgeDetailResponse(
    val data: DataWithTag,
    val path: String,
    val success: Boolean,
    val timestamp: String
)

@JsonClass(generateAdapter = true)
data class DataWithTag(
    val badges: List<BadgeWithTag>
)

@JsonClass(generateAdapter = true)
data class BadgeWithTag(
    val exp: Int,
    val id: Int? =null,
    val level: Int,
    val name: String,
    val tags : List<String>
)