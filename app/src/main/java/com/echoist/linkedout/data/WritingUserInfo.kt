package com.echoist.linkedout.data

import com.google.gson.annotations.SerializedName

data class WritingUserInfo(

    @SerializedName("id") val id: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("published") val published: Boolean,
    @SerializedName("linkedOut") val linkedOut: Boolean,
    @SerializedName("categoryId") val categoryId: Int,
    @SerializedName("linkedOutGauge") val linkedOutGauge: Int,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("message") val message: String = ""

)

