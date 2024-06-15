package com.echoist.linkedout.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
    data class UserInfo(

        @Json(name = "id")val id: Int,
        @Json(name = "nickname")val nickname: String ? = null,
        @Json(name = "password")val password: String? = null,
        @Json(name = "gender")val gender: String? = null ,
        @Json(name = "profileImage")val profileImage: String? = null,
        @Json(name = "birthDate")val birthDate: String? = null,
        @Json(name = "writtenEssay")val writtenEssay: Int = 0,
        @Json(name = "publishedEssay")val publishedEssay: Int = 0,
        @Json(name = "linkedOutEssay")val linkedOutEssay: Int = 0,

    )

