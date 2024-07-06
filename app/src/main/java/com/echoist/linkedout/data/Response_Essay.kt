package com.echoist.linkedout.data


import com.echoist.linkedout.api.EssayApi
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EssayListResponse(
    val data: Essays,
    val path: String?,
    val success: Boolean,
    val timestamp: String,
    val statusCode: Int?
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

@JsonClass(generateAdapter = true)
data class BasicResponse(
    val path: String,
    val success: Boolean,
    val timestamp: String
)

@JsonClass(generateAdapter = true)
data class SingleEssayResponse(
    val data: EssayApi.EssayItem?,
    val path: String?,
    val success: Boolean,
    val timestamp: String?,
    val statusCode : Int?
)

@JsonClass(generateAdapter = true)
data class RelatedEssayResponse(
    val data: Essays2,
    val path: String,
    val success: Boolean,
    val timestamp: String
)
@JsonClass(generateAdapter = true)
data class Essays2(
    val essays: List<RelatedEssay>,
    val total: Int? = null,
    val totalPage : Int,
    val page : Int
)

data class RelatedEssay(val id : Int,val title : String, val createdDate : String, val story: Int?)


