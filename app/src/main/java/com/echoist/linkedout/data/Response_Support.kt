package com.echoist.linkedout.data

import com.echoist.linkedout.api.EssayApi
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NoticeResponse(
    val data: NoticeList,
    val path: String,
    val success: Boolean,
    val timestamp: String,
    val statusCode : Int,
)
data class NoticeList(val Notices : List<Notice>,val total: Int,val page: Int,val totalPage: Int)

@JsonClass(generateAdapter = true)
data class NoticeDetailResponse(
    val data: Notice,
    val path: String,
    val success: Boolean,
    val timestamp: String,
    val statusCode : Int,

)

data class InquiryResponse(
    val data: Inquiry,
    val path: String,
    val success: Boolean,
    val timestamp: String,
    val statusCode : Int,
)
data class AllInquiriesResponse(
    val data: List<Inquiry>,
    val path: String,
    val success: Boolean,
    val timestamp: String,
    val statusCode : Int,
)

//공지사항
@JsonClass(generateAdapter = true)
data class Notice(
    val id: Int,
    val title: String,
    val content: String? = null,
    val createdDate: String
)
//공지사항

// 고객문의
data class Inquiry(
    val id: Int? = null,
    var title: String,
    var content: String? = null,
    val createdDate: String? = null,
    val processed: Boolean? = null,
    val user: UserInfo? = null, //문의자 정보
    var type : String? = null,
    var answer : String? = null //문의답변
)

data class NotificationSettings(
    val viewed: Boolean,
    val report : Boolean,
    val marketing : Boolean
)

@JsonClass(generateAdapter = true)
data class NotificationResponse(
    val data: NotificationSettings,
    val path: String?,
    val success: Boolean,
    val timestamp: String?,
    val statusCode : Int?
)
// 고객문의

//업데이트 api
@JsonClass(generateAdapter = true)
data class UpdateHistoryResponse(
    val data: Releases,
    val path: String?,
    val success: Boolean,
    val timestamp: String?,
    val statusCode : Int?
)

data class Releases(
    val releases : List<Release>,
    val total : Int,
    val page : Int,
    val totalPage : Int
    )

data class Release(
    val id: Int,
    val history : String,
    val createdDate: String,
    val updatedDate: String,
    val processor: Processor? = null
)

data class Processor(
    val id: Int,
    val name: String,
    val email: String,
    val profileImage: String,
    val activated : Boolean,
    val info : String,
    val createdDate: String,
)
//업데이트 api

//alert api
data class AlertsResponse(
    val data: Alerts,
    val path: String?,
    val success: Boolean,
    val timestamp: String?,
    val statusCode : Int?
)

data class Alerts(
    val alerts: List<Alert>,
    val total: Int,
    val page: Int,
    val totalPage: Int
)
data class Alert(
    val id: Int,
    val title: String,
    val content: String,
    val body: String? = null,
    val type: String,
    val read: Boolean,
    val createdDate: String,
    val essay : EssayApi.EssayItem
)
data class Url(val url : String)
data class GuleroquisResponse(
    val data: Url,
    val path: String,
    val success: Boolean,
    val timestamp: String,
    val statusCode : Int,
    )

//최신 공지사항
data class LatestNoticeResponse(
    val data: LatestNotice,
    val path: String,
    val success: Boolean,
    val timestamp: String,
    val statusCode : Int,
)

data class LatestNotice(val newNotice : Int?)

//버전체크
data class VersionsResponse(
    val data: Versions,
    val path: String,
    val success: Boolean,
    val timestamp: String,
    val statusCode : Int,
)
data class Versions(
    val versions : DevicesVersions
)
data class DevicesVersions(
    val android_mobile : String,
    val android_tablet : String,
    val ios_mobile : String,
    val ios_tablet : String,
    val desktop_mac : String,
    val desktop_windows : String,
)