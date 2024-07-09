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
    var name: String = "",
    val createdDate: String = "",
    val essaysCount: Int? = null
)
//다른 스토리에 포함된 에세이 // 선택된 스토리에 이미있는 에세이