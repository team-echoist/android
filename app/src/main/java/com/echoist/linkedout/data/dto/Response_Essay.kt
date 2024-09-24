package com.echoist.linkedout.data.dto


import com.echoist.linkedout.data.api.EssayApi
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
data class NextEssayResponse(
    val data: EssaysWithRandom?,
    val path: String,
    val success: Boolean,
    val timestamp: String)

data class DetailEssayResponse(
    val data: EssaysWithRandom,
    val path: String,
    val success: Boolean,
    val timestamp: String)
data class EssaysWithRandom(
    val essay: EssayApi.EssayItem,
    val anotherEssays: AnotherEssays?,
)
data class AnotherEssays(val essays: List<EssayApi.EssayItem>)

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


