package com.echoist.linkedout.api.essay

import com.echoist.linkedout.data.EssayInfo
import com.echoist.linkedout.data.WritingUserInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface EssayApi{
    /**
     *
     * 일반 유저: 자유롭게 발행
     * 블랙 유저: publish, linkedOut 요청시 에세이 테이블에 각 항목을 false로
     * 우선 저장 후 리뷰테이블에 등록. 관리자 승인 필요
     *
     * 블랙유저가 publish 또는 linkedOut 요청시
     * 응답에는 아래 메시지 필드 추가
     * "message": "review has been requested.."
     */


    @JsonClass(generateAdapter = true)
    data class EssayData(
        @Json(name = "title")val title: String,
        @Json(name = "content")val content: String,
        @Json(name = "linkedOut")val linkedOut: Boolean = false,
        @Json(name = "published")val published: Boolean = false,
        @Json(name = "categoryId")val categoryId: Int? = null,
        @Json(name = "thumbnail")val thumbnail: String? = null,
        @Json(name = "linkedOutGauge")val linkedOutGauge: Int = 1

    )

    @POST("api/essays")
    suspend fun writeEssay(
        @Header("Authorization") accessToken: String,
        @Body essayData: EssayData
    ): Response<WritingUserInfo>


    @PUT("api/essays/:essayId") //바디로 바꿔야함
    suspend fun modifyEssay(
        @Header("Authorization") accessToken: String,
        @Field("title") title: String = "",
        @Field("content") content: String = "",
        @Field("linkedOutGauge") linkedOutGauge: Int = 0,
        @Field("categoryId") categoryId: Int = 0,
        @Field("thumbnail") thumbnail: String = "",
        @Field("published") published: Boolean = false,
        @Field("linkedOut") linkedOut: Boolean = false
    ): Response<WritingUserInfo>

    @DELETE("api/essays/:essayId")
    suspend fun deleteEssay(
        @Header("Authorization") accessToken: String
    ): Response<WritingUserInfo>

    //todo null 값 안들어가는거 확인해야할듯
    @GET("api/essays")
    suspend fun readEssay(
        @Header("Authorization") accessToken: String,
        @Query("published") published: Boolean? = false,
        @Query("categoryId") categoryId: Int?= 1,
        @Query("page") page: Int?= 1,
        @Query("limit") limit: Int?= 10,
    ): EssayInfo

}