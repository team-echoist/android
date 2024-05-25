package com.echoist.linkedout.data

data class NaverUserResponse(
    val message: String,
    val response: Response,
    val resultcode: String
)
data class Response(
    val age: String?,
    val birthday: String?,
    val birthyear: String?,
    val email: String?,
    val gender: String?,
    val id: String?,
    val mobile: String?,
    val name: String?,
    val nickname: String?,
    val profile_image: String?
)