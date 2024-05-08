package com.echoist.linkedout.api.essay

import com.echoist.linkedout.data.WritingUserInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

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


    data class EssayData(
        val title: String,
        val content: String,
        val linkedOut: Boolean = false,
        val published: Boolean = false,
        val categoryId: Int = 0,
        val thumbnail: String = "",
        val linkedOutGauge: Int = 1,
        val id : String = ""
    )

    @POST("api/essay")
    suspend fun writeEssay(
        //todo 쿼리로 보내지말고 바디로보내기~
        @Header("Authorization") accessToken: String,
        @Body essayData: EssayData
    ): Response<WritingUserInfo>


    @FormUrlEncoded
    @PUT("api/essay/:essayId")
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

    @DELETE("api/essay/:essayId")
    suspend fun deleteEssay(
        @Header("Authorization") accessToken: String
    ): Response<WritingUserInfo>

}