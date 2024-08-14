package com.echoist.linkedout.api

import com.echoist.linkedout.data.AlertsResponse
import com.echoist.linkedout.data.AllInquiriesResponse
import com.echoist.linkedout.data.GuleroquisResponse
import com.echoist.linkedout.data.Inquiry
import com.echoist.linkedout.data.InquiryResponse
import com.echoist.linkedout.data.IsFirstCheckResponse
import com.echoist.linkedout.data.LatestNoticeResponse
import com.echoist.linkedout.data.NoticeDetailResponse
import com.echoist.linkedout.data.NoticeResponse
import com.echoist.linkedout.data.NotificationResponse
import com.echoist.linkedout.data.NotificationSettings
import com.echoist.linkedout.data.UpdateHistoryResponse
import com.echoist.linkedout.data.VersionsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SupportApi {
    @GET("api/support/notices")
    suspend fun readNotices(
        @Header("Authorization") accessToken: String,
        @Query("page") page : Int = 1,
        @Query("limit") limit : Int = 20
    ): Response<NoticeResponse>

    @GET("api/support/notices/{noticeId}")
    suspend fun readNoticeDetail(
        @Header("Authorization") accessToken: String,
        @Path("noticeId") noticeId : Int
    ): Response<NoticeDetailResponse>

    @POST("api/support/inquiries")
    suspend fun writeInquiry(
        @Header("Authorization") accessToken: String,
        @Body inquiry : Inquiry
    ): Response<Unit>

    @GET("api/support/inquiries")
    suspend fun readInquiries(
        @Header("Authorization") accessToken: String
    ): Response<AllInquiriesResponse>

    @GET("api/support/inquiries/{inquiryId}")
    suspend fun readInquiryDetail(
        @Header("Authorization") accessToken: String,
        @Path("inquiryId") inquiryId : Int
    ): Response<InquiryResponse>

    @GET("api/support/release")
    suspend fun readUpdatedHistories(
        @Header("Authorization") accessToken: String,
        @Query("page") page : Int = 1,
        @Query("limit") limit : Int = 10
    ): Response<UpdateHistoryResponse>

    @POST("api/support/devices/register")
    suspend fun requestRegisterDevice(
        @Header("Authorization") accessToken: String,
        @Body registerDeviceRequest: SignUpApiImpl.RegisterDeviceRequest
    ): Response<Unit>
    @GET("api/support/settings")
    suspend fun readUserNotification(
        @Header("Authorization") accessToken: String,
    ): Response<NotificationResponse>
    @POST("api/support/settings")
    suspend fun updateUserNotification(
        @Header("Authorization") accessToken: String,
        @Body requestSettings: NotificationSettings
    ): Response<Unit>

    @GET("api/alerts")
    suspend fun readAlertsList(
        @Header("Authorization") accessToken: String,
        @Query("page") page : Int = 1,
        @Query("limit") limit : Int = 10
    ): Response<AlertsResponse>

    @PATCH("api/alerts/read/{alertId}")
    suspend fun readAlert(
        @Header("Authorization") accessToken: String,
        @Path("alertId") alertId : Int,
    ): Response<Unit>

    @GET("api/alerts/unread")
    suspend fun readUnreadAlerts(
        @Header("Authorization") accessToken: String,
    ): Response<IsFirstCheckResponse>

    //글로키 주소 가져오기
    @GET("api/home/geulroquis")
    suspend fun readGeulroquis(
        @Header("Authorization") accessToken: String
    ): Response<GuleroquisResponse>

    //최신 공지사항 여부
    @GET("api/support/notices/latest")
    suspend fun requestLatestNotice(
        @Header("Authorization") accessToken: String
    ): Response<LatestNoticeResponse>

    //앱 버전 조회
    @GET("api/support/versions")
    suspend fun requestAppVersion(): Response<VersionsResponse>


}