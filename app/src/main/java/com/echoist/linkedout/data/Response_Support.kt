package com.echoist.linkedout.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NoticeResponse(
    val data: List<Notice>,
    val path: String,
    val success: Boolean,
    val timestamp: String,
    val statusCode : Int,
    val total : Int,
    val totalPages : Int,
    val page : Int

)

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


@JsonClass(generateAdapter = true)
data class Notice( //공지사항
    val id: Int,
    val title: String,
    val content: String? = null,
    val createdDate: String
)

data class Inquiry( // 고객문의
    val id: String,
    val title: String,
    val content: String? = null,
    val createdDate: String,
    val processed: Boolean? = null,
    val user: UserInfo, //문의자 정보
    val type : String? = null,
    val answer : String? = null //문의답변
)

data class NotificationSettings(
    val viewed: Boolean,
    val report : Boolean,
    val timeAllowed : Boolean,
    val remindTime : String? = null
)

@JsonClass(generateAdapter = true)
data class NotificationResponse(
    val data: NotificationSettings,
    val path: String?,
    val success: Boolean,
    val timestamp: String?,
    val statusCode : Int?
)