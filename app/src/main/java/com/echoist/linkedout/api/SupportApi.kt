package com.echoist.linkedout.api

import SignUpApiImpl
import com.echoist.linkedout.data.AllInquiriesResponse
import com.echoist.linkedout.data.Inquiry
import com.echoist.linkedout.data.InquiryResponse
import com.echoist.linkedout.data.NoticeDetailResponse
import com.echoist.linkedout.data.NoticeResponse
import com.echoist.linkedout.data.NotificationResponse
import com.echoist.linkedout.data.NotificationSettings
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SupportApi {
    @GET("api/support/notices")
    suspend fun requestNotices(
        @Header("Authorization") accessToken: String,
        @Query("page") page : Int,
        @Query("limit") limit : Int
    ): NoticeResponse

    @GET("api/support/notices/{noticeId}")
    suspend fun requestNoticeDetail(
        @Header("Authorization") accessToken: String,
        @Path("noticeId") noticeId : String
    ): NoticeDetailResponse

    @POST("api/support/inquiries")
    suspend fun writeInquiry(
        @Header("Authorization") accessToken: String,
        @Body inquiry : Inquiry
    ): Response<Unit>

    @GET("api/support/inquiries")
    suspend fun requestInquiries(
        @Header("Authorization") accessToken: String
    ): AllInquiriesResponse

    @GET("api/support/inquiries/{inquiryId}")
    suspend fun requestInquiryDetail(
        @Header("Authorization") accessToken: String,
        @Path("inquiryId") inquiryId : String
    ): InquiryResponse

    @GET("api/support/updated-histories")
    suspend fun requestUpdatedHistories(
        @Header("Authorization") accessToken: String,
        @Query("page") page : Int,
        @Query("limit") limit : Int
    ): InquiryResponse

    @POST("api/support/devices/register")
    suspend fun requestRegisterDevice(
        @Header("Authorization") accessToken: String,
        @Body registerDeviceRequest: SignUpApiImpl.RegisterDeviceRequest
    ): Response<Unit>
    @GET("api/support/settings/{deviceId}")
    suspend fun getUserNotification(
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