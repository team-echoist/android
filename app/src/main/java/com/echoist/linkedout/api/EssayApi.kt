package com.echoist.linkedout.api

import WritingUserInfo
import com.echoist.linkedout.data.EssayInfo
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

interface EssayApi {

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
    data class EssayItem(
        val title: String,
        val content: String,
        val status: String? = null,
        val categoryId: Int? = null,
        val thumbnail: String? = null,
        val linkedOutGauge: Int? = null,
        val latitude: Double? = null,
        val longitude: Double? = null,
        val location: String? = null,
        val tags: List<String>? = null,
        val createdDate: String? = null,
        val updatedDate: String? = null,
        val id: Int? = null,
        val nickName: String? = null,
        val author: UserApi.UserInfo? = null,
    )

    @POST("api/essays")
    suspend fun writeEssay(
        @Header("Authorization") accessToken: String,
        @Body essayData: EssayItem
    ): Response<WritingUserInfo>


    @PUT("api/essays/:essayId") //바디로 바꿔야함
    suspend fun modifyEssay(
        @Header("Authorization") accessToken: String,
        @Body essayData: EssayItem
    ): Response<WritingUserInfo>


    @DELETE("api/essays/{essayId}")
    suspend fun deleteEssay(
        @Header("Authorization") accessToken: String,
        @Path("essayId") essayId: String = "0"
    ): Response<Unit>

    @GET("api/essays")
    suspend fun readMyEssay(
        @Header("Authorization") accessToken: String,
        @Query("published") published: Boolean? = false,
        @Query("categoryId") categoryId: String = "",
        @Query("page") page: String = "",
        @Query("limit") limit: String = "",
    ): Response<EssayInfo>

}
class ApiClient @Inject constructor()  {

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    val api: EssayApi = Retrofit.Builder()
        .baseUrl("https://www.linkedoutapp.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(EssayApi::class.java)
}