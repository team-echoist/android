package com.echoist.linkedout.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StoryResponse(
    val data: Stories,
    val path: String,
    val success: Boolean,
    val timestamp: String
)

@JsonClass(generateAdapter = true)
data class Stories(
    val stories: List<Story>
)

@JsonClass(generateAdapter = true)
data class Story(
    val id: Int? =null,
    val name: String,
    val createdDate: String,
    val essaysCount: Int
)