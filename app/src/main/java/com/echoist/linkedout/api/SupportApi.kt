package com.echoist.linkedout.api

import SignUpApiImpl
import com.echoist.linkedout.data.AllInquiriesResponse
import com.echoist.linkedout.data.Inquiry
import com.echoist.linkedout.data.InquiryResponse
import com.echoist.linkedout.data.NoticeDetailResponse
import com.echoist.linkedout.data.NoticeResponse
import com.echoist.linkedout.data.NotificationResponse
import com.echoist.linkedout.data.NotificationSettings
import com.echoist.linkedout.data.UpdateHistoryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
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
    ): AllInquiriesResponse

    @GET("api/support/inquiries/{inquiryId}")
    suspend fun readInquiryDetail(
        @Header("Authorization") accessToken: String,
        @Path("inquiryId") inquiryId : String
    ): InquiryResponse

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



}