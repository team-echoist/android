package com.echoist.linkedout.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
    data class UserInfo(
    val id: Int,
    val nickname: String ? = null,
    val password: String? = null,
    val createdDate : String? = null,
    val updatedDate : String? = null,
    val gender: String? = null,
    val profileImage: String? = null,
    val birthDate: String? = null,
    var essayStats: EssayStats? = null, //이 스탯은 원래 바로 받아지지않는 값임.
    val oauthInfo: OauthInfo? = null, //유저의 소셜로그인정보
    val subscriptionEnd : Boolean? = null

    )

data class EssayStats(
    val totalEssays: Int = 0,
    val publishedEssays: Int = 0,
    val linkedOutEssays: Int = 0
)

data class OauthInfo(
    val googleId : String? = null
)
@JsonClass(generateAdapter = true)
data class UserResponse(
    val data: UserInfo,
    val path: String,
    val success: Boolean,
    val timestamp: String
)

/**
 * essay stats를 포함한 Response
 */
@JsonClass(generateAdapter = true)
data class UserEssayStatsResponse(
    val data: UserWithEssay,
    val path: String,
    val success: Boolean,
    val timestamp: String
)

data class UserWithEssay(
    val user: UserInfo,
    val essayStats: EssayStats
)

