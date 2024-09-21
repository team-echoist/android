package com.echoist.linkedout.api

import com.echoist.linkedout.data.AlertsResponse
import com.echoist.linkedout.data.AllInquiriesResponse
import com.echoist.linkedout.data.GuleroquisResponse
import com.echoist.linkedout.data.Inquiry
import com.echoist.linkedout.data.InquiryResponse
import com.echoist.linkedout.data.IsFirstCheckResponse
import com.echoist.linkedout.data.LatestNoticeResponse
import com.echoist.linkedout.data.LatestUpdateResponse
import com.echoist.linkedout.data.NoticeDetailResponse
import com.echoist.linkedout.data.NoticeResponse
import com.echoist.linkedout.data.NotificationResponse
import com.echoist.linkedout.data.NotificationSettings
import com.echoist.linkedout.data.UpdateHistoryResponse
import com.echoist.linkedout.data.VersionsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SupportApi {
    @GET("api/support/notices")
    suspend fun readNotices(
        @Query("page") page : Int = 1,
        @Query("limit") limit : Int = 20
    ): Response<NoticeResponse>

    @GET("api/support/notices/{noticeId}")
    suspend fun readNoticeDetail(
        @Path("noticeId") noticeId : Int
    ): Response<NoticeDetailResponse>

    @POST("api/support/inquiries")
    suspend fun writeInquiry(
        @Body inquiry : Inquiry
    ): Response<Unit>

    @GET("api/support/inquiries")
    suspend fun readInquiries(
    ): Response<AllInquiriesResponse>

    @GET("api/support/inquiries/{inquiryId}")
    suspend fun readInquiryDetail(
        @Path("inquiryId") inquiryId : Int
    ): Response<InquiryResponse>

    //최신 업데이트 공지 조회
    @GET("api/support/release/latest")
    suspend fun requestLatestUpdate(
    ): Response<LatestUpdateResponse>

    @GET("api/support/release")
    suspend fun readUpdatedHistories(
        @Query("page") page : Int = 1,
        @Query("limit") limit : Int = 10
    ): Response<UpdateHistoryResponse>

    @POST("api/support/devices/register")
    suspend fun requestRegisterDevice(
        @Body registerDeviceRequest: SignUpApiImpl.RegisterDeviceRequest
    ): Response<Unit>
    @GET("api/support/settings")
    suspend fun readUserNotification(
    ): Response<NotificationResponse>
    @POST("api/support/settings")
    suspend fun updateUserNotification(
        @Body requestSettings: NotificationSettings
    ): Response<Unit>

    @GET("api/alerts")
    suspend fun readAlertsList(
        @Query("page") page : Int = 1,
        @Query("limit") limit : Int = 10
    ): Response<AlertsResponse>

    @PATCH("api/alerts/read/{alertId}")
    suspend fun readAlert(
        @Path("alertId") alertId : Int,
    ): Response<Unit>

    @GET("api/alerts/unread")
    suspend fun readUnreadAlerts(
    ): Response<IsFirstCheckResponse>

    //글로키 주소 가져오기
    @GET("api/home/geulroquis")
    suspend fun readGeulroquis(
    ): Response<GuleroquisResponse>

    //최신 공지사항 여부
    @GET("api/support/notices/latest")
    suspend fun requestLatestNotice(
    ): Response<LatestNoticeResponse>

    //앱 버전 조회
    @GET("api/support/versions")
    suspend fun requestAppVersion(): Response<VersionsResponse>
}