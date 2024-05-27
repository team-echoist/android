package com.echoist.linkedout.data


import com.echoist.linkedout.api.EssayApi
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EssayListResponse(
    val data: Essays,
    val path: String,
    val success: Boolean,
    val timestamp: String
)
@JsonClass(generateAdapter = true)
data class Essays(
    val essays: List<EssayApi.EssayItem>,
    val total: Int? = null,
)

data class DetailEssayResponse(
    val data: EssaysWithPrevious,
    val path: String,
    val success: Boolean,
    val timestamp: String)

data class EssaysWithPrevious(
    val essay: EssayApi.EssayItem,
    val previous: List<EssayApi.EssayItem>? = null,
)

