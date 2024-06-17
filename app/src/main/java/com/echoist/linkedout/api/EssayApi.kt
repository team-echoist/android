package com.echoist.linkedout.api

import SingleEssayResponse
import com.echoist.linkedout.data.DetailEssayResponse
import com.echoist.linkedout.data.EssayListResponse
import com.echoist.linkedout.data.Story
import com.echoist.linkedout.data.UserInfo
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
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
    //보내는 용 string tag list

    data class Tag(val id : Int, val name : String)

    @POST("api/essays")
    suspend fun writeEssay(
        @Header("Authorization") accessToken: String,
        @Body essayData: WritingEssayItem
    ): Response<SingleEssayResponse>


    @PUT("api/essays/:essayId") //바디로 바꿔야함
    suspend fun modifyEssay(
        @Header("Authorization") accessToken: String,
        @Body essayData: WritingEssayItem
    ): Response<SingleEssayResponse>


    @DELETE("api/essays/{essayId}")
    suspend fun deleteEssay(
        @Header("Authorization") accessToken: String,
        @Path("storyId") storyId: Int = 0
    ): Response<Unit>

    @GET("api/essays")
    suspend fun readMyEssay(
        @Header("Authorization") accessToken: String,
        @Query("published") published: Boolean? = false,
        @Query("storyId") storyId: String = "",
        @Query("limit") limit: Int = 100, //이 값은 기본 10 수정가능
    ): Response<EssayListResponse>

    @GET("api/essays/recommend")
    suspend fun readRandomEssays(
        @Header("Authorization") accessToken: String,
        @Query("limit") limit: Int = 20, //이 값은 기본 10 수정가능
    ): Response<EssayListResponse>

    @GET("api/essays/followings")
    suspend fun readFollowingEssays(
        @Header("Authorization") accessToken: String,
        @Query("limit") limit: Int = 20, //이 값은 기본 10 수정가능
    ): Response<EssayListResponse>

    @GET("api/essays/sentence")
    suspend fun readOneSentences(
        @Header("Authorization") accessToken: String,
        @Query("type") type : String,
        @Query("limit") limit: Int = 20, //이 값은 기본 10 수정가능
    ): Response<EssayListResponse>

    @GET("api/essays/{essayId}")
    suspend fun readDetailEssay(
        @Header("Authorization") accessToken: String,
        @Path("essayId") essayId: Int = 0
    ): Response<DetailEssayResponse>

    data class ReportRequest(
        val reason: String
    )

    @POST("api/essays/reports/{essayId}")
    suspend fun reportEssay(
        @Header("Authorization") accessToken: String,
        @Path("essayId") essayId: Int,
        @Body reason : ReportRequest
    ): Response<Unit>

    @JsonClass(generateAdapter = true)
    data class WritingEssayItem(
        val title: String,
        val content: String,
        val status: String? = null,
        val categoryId: Int? = null,
        val thumbnail: String? = null,
        val linkedOutGauge: Int? = null,
        val latitude: Double? = null,
        val longitude: Double? = null,
        val location: String? = null,
        val tags: List<String>? = null,//보내는 용 string tag list
        val createdDate: String? = null,
        val updatedDate: String? = null,
        val id: Int? = null,
        val nickName: String? = null,
        val author: UserInfo? = null,
        val story: Story? = null
    )

    //받는용 Tag List id와 name으로 구별하여받음.
    @JsonClass(generateAdapter = true)
    data class EssayItem(
        val title: String? = null,
        val content: String? = null,
        val status: String? = null,
        val categoryId: Int? = null,
        val thumbnail: String? = null,
        val linkedOutGauge: Int? = null,
        val latitude: Double? = null,
        val longitude: Double? = null,
        val location: String? = null,
        val tags: List<Tag>? = null, //받는용 Tag List id와 name으로 구별하여받음.
        val createdDate: String? = null,
        val updatedDate: String? = null,
        val id: Int? = null,
        val author: UserInfo? = null,
        val story: Story? = null

    )



}