package com.echoist.linkedout.api

import com.echoist.linkedout.data.BadgeDetailResponse
import com.echoist.linkedout.data.BadgeSimpleResponse
import com.echoist.linkedout.data.BasicResponse
import com.echoist.linkedout.data.UserEssayStatsResponse
import com.echoist.linkedout.data.UserGraphSummaryResponse
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.data.UserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface UserApi {

    @GET("api/users/profile/my") // 에세이통계 까지 조회
    suspend fun getMyInfo(
        @Header("Authorization") accessToken: String,
    ): UserEssayStatsResponse
    @GET("api/badges")
    suspend fun readBadgeList(
        @Header("Authorization") accessToken: String
    ): Response<BadgeSimpleResponse>

    @GET("api/badges/detail")
    suspend fun readBadgeWithTagsList(
        @Header("Authorization") accessToken: String
    ): Response<BadgeDetailResponse>

    @POST("api/badges/level")
    suspend fun requestBadgeLevelUp(
        @Header("Authorization") accessToken: String,
        @Query("badgeId") badgeId: Int,
    ): Response<BasicResponse>

    @PUT("api/users")
    suspend fun userUpdate(
        @Header("Authorization") accessToken: String,
        @Body userInfo: UserInfo
    ): Response<UserResponse>

    //이미지 파일 형식 으로 업로드 필요할 때
    @POST("api/users/images")
    @Multipart
    suspend fun userImageUpload(
        @Header("Authorization") accessToken: String,
        @Part image: MultipartBody.Part
    ): Response<ImageUrlResponse>


    data class ImageUrl(val imageUrl : String)
    data class ImageUrlResponse(
        val data: ImageUrl,
        val path: String,
        val success: Boolean,
        val timestamp: String,
        val statusCode : Int,

        )


    @POST("api/users/deactivate") //회원 탈퇴 요청 유예 기간 부여
    suspend fun requestDeactivate(
        @Header("Authorization") accessToken: String,
        @Body deactivate: RequestDeactivate,
    ): Response<Unit>

    data class RequestDeactivate(val reasons : List<String>)

    @POST("api/users/reactivate") //회원 탈퇴 요청 취소
    suspend fun requestReactivate(
        @Header("Authorization") accessToken: String
    ): Response<Unit>

    @DELETE("api/users") //회원 탈퇴 바로 진행
    suspend fun requestDeleteUser(
        @Header("Authorization") accessToken: String
    ): Response<Unit>



    //유저 주간 링크드아웃 지수 통계 (그래프용)
    @GET("api/users/summary")
    suspend fun requestUserGraphSummary(
        @Header("Authorization") accessToken: String,
    ): Response<UserGraphSummaryResponse>

}