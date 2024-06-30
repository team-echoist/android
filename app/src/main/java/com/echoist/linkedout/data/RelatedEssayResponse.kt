package com.echoist.linkedout.data

import com.squareup.moshi.JsonClass

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

