package com.echoist.linkedout

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

const val MAX_TITLE_SIZE = 26
const val MAX_CONTENT_SIZE = 20
const val MIN_TITLE_SIZE = 16
const val MIN_CONTENT_SIZE = 12

const val LIGHT_MODE = "라이트 모드"
const val DARK_MODE = "다크 모드"

const val BASE_URL = "https://linkedoutapp.com/"

const val PRIVATE_POPUP_URL = "https://driqat77mj5du.cloudfront.net/service/popup1_high.gif"
const val PUBLISHED_POPUP_URL = "https://driqat77mj5du.cloudfront.net/service/popup2_high.gif"
const val LINKEDOUT_POPUP_URL = "https://driqat77mj5du.cloudfront.net/service/popup3_high.gif"
const val INSPECT_POPUP_URL = "https://driqat77mj5du.cloudfront.net/service/popup4_high.gif"

const val PROFILE_IMAGE_01 = "https://driqat77mj5du.cloudfront.net/service/profile_icon_01.png"
const val PROFILE_IMAGE_02 = "https://driqat77mj5du.cloudfront.net/service/profile_icon_02.png"
const val PROFILE_IMAGE_03 = "https://driqat77mj5du.cloudfront.net/service/profile_icon_03.png"
const val PROFILE_IMAGE_04 = "https://driqat77mj5du.cloudfront.net/service/profile_icon_04.png"
const val PROFILE_IMAGE_05 = "https://driqat77mj5du.cloudfront.net/service/profile_icon_05.png"
const val PROFILE_IMAGE_06 = "https://driqat77mj5du.cloudfront.net/service/profile_icon_06.png"
const val PROFILE_IMAGE_07 = "https://driqat77mj5du.cloudfront.net/service/profile_icon_07.png"
const val PROFILE_IMAGE_08 = "https://driqat77mj5du.cloudfront.net/service/profile_icon_08.png"
const val PROFILE_IMAGE_09 = "https://driqat77mj5du.cloudfront.net/service/profile_icon_09.png"
const val PROFILE_IMAGE_10 = "https://driqat77mj5du.cloudfront.net/service/profile_icon_10.png"
const val PROFILE_IMAGE_11 = "https://driqat77mj5du.cloudfront.net/service/profile_icon_11.png"
const val PROFILE_IMAGE_12 = "https://driqat77mj5du.cloudfront.net/service/profile_icon_12.png"

const val TUTORIAL_BULB = "https://driqat77mj5du.cloudfront.net/service/geulroquis_bulb.gif"



fun formatDateTime(input: String): String {
    // Define the input format
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

    // Parse the input string to a LocalDateTime
    val dateTime = LocalDateTime.parse(input, inputFormatter)

    // Define the output format
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

    // Format the LocalDateTime to the desired output format
    return dateTime.format(outputFormatter)
}

fun formatElapsedTime(isoDateTimeString: String): String {
    // 작성 시간과 현재 시간 가져오기
    val writtenTime = ZonedDateTime.parse(isoDateTimeString, DateTimeFormatter.ISO_DATE_TIME)
    val currentTime = ZonedDateTime.now(ZoneId.of("Z"))

    // 시간 차이 계산
    val duration = ChronoUnit.SECONDS.between(writtenTime, currentTime)

    // 초, 분, 시간, 일로 변환
    val days = duration / (24 * 3600)
    val hours = (duration % (24 * 3600)) / 3600
    val minutes = (duration % 3600) / 60
    val seconds = duration % 60

    return when {
        days > 0 -> "${days}일 전"
        hours > 0 -> "${hours}시간 전"
        minutes > 0 -> "${minutes}분 전"
        else -> "${seconds}초 전"
    }
}