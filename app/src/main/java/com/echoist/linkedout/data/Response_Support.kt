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
    val report : Boolean
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
    val data: HistoryResponse,
    val path: String?,
    val success: Boolean,
    val timestamp: String?,
    val statusCode : Int?
)

data class HistoryResponse(
    val histories : List<History>,
    val total : Int,
    val page : Int,
    val totalPage : Int
    )

data class History(
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
