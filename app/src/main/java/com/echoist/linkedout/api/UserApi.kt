package com.echoist.linkedout.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

interface UserApi {

    //todo 수정 해야 할것.
    @JsonClass(generateAdapter = true)
    data class UserInfo(
        @Json(name = "id")val id: Int,
        @Json(name = "nickname")val nickname: String,
        @Json(name = "password")val password: String,
        @Json(name = "gender")val gender: String? ,
        @Json(name = "profileImage")val profileImage: String? ,
        @Json(name = "birthDate")val birthDate: String?
    )
}