package com.echoist.linkedout.api

import com.echoist.linkedout.data.AlertsResponse
import com.echoist.linkedout.data.AllInquiriesResponse
import com.echoist.linkedout.data.GuleroquisResponse
import com.echoist.linkedout.data.Inquiry
import com.echoist.linkedout.data.InquiryResponse
import com.echoist.linkedout.data.IsFirstCheckResponse
import com.echoist.linkedout.data.NoticeDetailResponse
import com.echoist.linkedout.data.NoticeResponse
import com.echoist.linkedout.data.NotificationResponse
import com.echoist.linkedout.data.NotificationSettings
import com.echoist.linkedout.data.UpdateHistoryResponse
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
        @Query("page") page : Int,
        @Query("limit") limit : Int
    ): NoticeResponse

    @GET("api/support/notices/{noticeId}")
    suspend fun readNoticeDetail(
        @Header("Authorization") accessToken: String,
        @Path("noticeId") noticeId : String
    ): NoticeDetailResponse

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

    @GET("api/support/updated-histories")
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
    @GET("api/support/settings/{deviceId}")
    suspend fun readUserNotification(
        @Header("Authorization") accessToken: String,
        @Path("deviceId") deviceId: String,
    ): Response<NotificationResponse>
    @POST("api/support/settings/{deviceId}")
    suspend fun updateUserNotification(
        @Header("Authorization") accessToken: String,
        @Path("deviceId") deviceId: String,
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

    @GET("api/home/geulroquis") //todo 수정
    suspend fun readGeulroquis(
        @Header("Authorization") accessToken: String
    ): Response<GuleroquisResponse>



}