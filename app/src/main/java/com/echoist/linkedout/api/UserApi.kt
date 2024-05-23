package com.echoist.linkedout.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

interface UserApi {

    //todo 수정 해야 할것. 작성한 글 리스트
    @JsonClass(generateAdapter = true)
    data class UserInfo(

        @Json(name = "id")val id: Int,
        @Json(name = "nickname")val nickname: String,
        @Json(name = "password")val password: String,
        @Json(name = "gender")val gender: String? = null ,
        @Json(name = "profileImage")val profileImage: String? = null,
        @Json(name = "birthDate")val birthDate: String?,
        @Json(name = "writtenEssay")val writtenEssay: Int = 0,
        @Json(name = "publishedEssay")val publishedEssay: Int = 0,
        @Json(name = "linkedOutEssay")val linkedOutEssay: Int = 0,

    )
}