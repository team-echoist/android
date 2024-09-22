package com.echoist.linkedout.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
    data class UserInfo(
    val id: Int? = null,
    var nickname: String ? = null,
    val password: String? = null,
    val createdDate : String? = null,
    val updatedDate : String? = null,
    val gender: String? = null,
    var profileImage: String? = null,
    val birthDate: String? = null,
    var essayStats: EssayStats? = null, //이 스탯은 원래 바로 받아지지않는 값임.
    //val oauthInfo: String? = null, //유저의 소셜로그인정보
    val subscriptionEnd : Boolean? = null,
    val email : String? = null,
    var isFirst : Boolean? = null,
    var platform : String? = null,
    var platformId : String? = null,
    var deactivationDate : String? = null,
    var weeklyEssayCounts: List<WeeklyEssayCount>? = null,
    var locationConsent: Boolean? = null

    )

data class EssayStats(
    var totalEssays: Int = 0,
    var publishedEssays: Int = 0,
    var linkedOutEssays: Int = 0
) {
}

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
    val statusCode: Int,
    val success: Boolean,
    val timestamp: String
)

data class UserWithEssay(
    val user: UserInfo,
    val essayStats: EssayStats
)

//첫 유저인지 체크
@JsonClass(generateAdapter = true)
data class IsFirstCheckResponse(
    val data: Boolean,
    val path: String,
    val success: Boolean,
    val timestamp: String
)

//유저 주간 링크드아웃 지수 통계 (그래프용)

@JsonClass(generateAdapter = true)
data class UserGraphSummaryResponse(
    val data: UserInfo,
    val path: String,
    val success: Boolean,
    val timestamp: String
)

@JsonClass(generateAdapter = true)
data class WeeklyEssayCount(
    val weekStart: String,
    val weekEnd: String,
    val count: Int
)

//중복 닉 검사
@JsonClass(generateAdapter = true)
data class NicknameCheckResponse(
    val timestamp: String,
    val path: String,
    val success: Boolean,
    val statusCode : Int
)
