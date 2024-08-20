package com.echoist.linkedout.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokensResponse(
    val data: Tokens,
    val path: String,
    val success: Boolean,
    val timestamp: String
)

@JsonClass(generateAdapter = true)
data class Tokens(
    val accessToken : String,
    val refreshToken : String
)

data class RegisterCode(
    val code : String
)