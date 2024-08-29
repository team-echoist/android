package com.echoist.linkedout

import androidx.navigation.NavController
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

const val TYPE_RECOMMEND = "recommend"
const val TYPE_PUBLISHED = "public"
const val TYPE_PRIVATE = "private"
const val TYPE_STORY = "story"


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

const val PRIVACY_POLICY_URL = "https://www.linkedoutapp.com/privacy-policy" // 개인정보 처리방침
const val LOCATION_POLICY_URL = "https://www.linkedoutapp.com/location-terms" // 위치기반 서비스 이용약관
const val OPERATIONAL_POLICY_URL = "https://www.linkedoutapp.com/operational_policy" // 운영정책
const val TERMS_POLICY_URL = "https://www.linkedoutapp.com/terms" // 이용약관

object DeviceType {
    const val DESKTOP = "Desktop"
    const val LAPTOP = "Laptop"
    const val MOBILE = "Mobile"
    const val TABLET = "Tablet"
    const val UNKNOWN = "Unknown"
}

object Routes {
    const val OnBoarding = "OnBoarding"
    const val LoginPage = "LoginPage"
    const val SignUp = "SIGNUP"
    const val AgreeOfProvisionsPage = "AgreeOfProvisionsPage"
    const val SignUpComplete = "SignUpComplete"
    const val Home = "HOME"
    const val DarkModeSettingPage = "DarkModeSettingPage"
    const val NotificationPage = "NotificationPage"
    const val NotificationSettingPage = "NotificationSettingPage"
    const val NoticeDetailPage = "NoticeDetailPage"
    const val SupportPage = "SupportPage"
    const val LinkedOutSupportPage = "LinkedOutSupportPage"
    const val InquiryPage = "InquiryPage"
    const val NoticePage = "NoticePage"
    const val UpdateHistoryPage = "UpdateHistoryPage"
    const val MyLog = "MYLOG"
    const val StoryPage = "StoryPage"
    const val StoryDetailPage = "StoryDetailPage"
    const val DetailEssayInStoryPage = "DetailEssayInStoryPage"
    const val MyLogDetailPage = "MyLogDetailPage"
    const val CompletedEssayPage = "CompletedEssayPage"
    const val Community = "COMMUNITY"
    const val CommunityDetailPage = "CommunityDetailPage"
    const val CommunitySavedEssayPage = "CommunitySavedEssayPage"
    const val SubscriberPage = "SubscriberPage"
    const val FullSubscriberPage = "FullSubscriberPage"
    const val Settings = "SETTINGS"
    const val RecentViewedEssayPage = "RecentViewedEssayPage"
    const val RecentEssayDetailPage = "RecentEssayDetailPage"
    const val AccountPage = "AccountPage"
    const val ChangeEmailPage = "ChangeEmailPage"
    const val ChangePwPage = "ChangePwPage"
    const val ResetPwPageWithEmail = "ResetPwPageWithEmail"
    const val ResetPwPage = "ResetPwPage"
    const val AccountWithdrawalPage = "AccountWithdrawalPage"
    const val BadgePage = "BadgePage"
    const val WritingPage = "WritingPage"
    const val WritingCompletePage = "WritingCompletePage"
    const val TemporaryStoragePage = "TemporaryStoragePage"
    const val CropImagePage = "CropImagePage"
    const val TermsAndConditionsPage = "TermsAndConditionsPage"
    const val PrivacyPolicyPage = "PrivacyPolicyPage"
    const val LocationPolicyPage = "LocationPolicyPage"
    const val FontCopyRight = "FontCopyRight"
    const val Search = "Search"
    const val ChangeEmail = "ChangeEmail"
    const val ChangePassword = "ChangePassword"
    const val DeleteAccount = "DeleteAccount"
}

enum class UserStatus {
    Activated, Monitored, Banned, DeActivated
}

fun navigateWithClearBackStack(
    navController: NavController,
    route: String,
    launchSingleTop: Boolean = true
) {
    navController.navigate(route) {
        popUpTo(navController.graph.startDestinationId) {
            inclusive = true
        }
        this.launchSingleTop = launchSingleTop
    }
}

//2024-07-01T14:22:46.803+09:00 to 2024.07.01
fun formatDateTime(input: String): String {

    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    val dateTime = LocalDateTime.parse(input, inputFormatter)
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

    return dateTime.format(outputFormatter)
}
//2024-07-01T14:22:46.803 09:00 to 2024-07-01T14:22:46.803+09:00 to 2024.07.01

fun parseAndFormatDateTime(dateTimeString: String): String {
    // 공백을 +로 바꾸어 표준 형식으로 만듦
    val correctedDateTimeString = dateTimeString.replace(" ", "+")
    // 원본 문자열의 형식 지정
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    val zonedDateTime = ZonedDateTime.parse(correctedDateTimeString, inputFormatter)
    return outputFormatter.format(zonedDateTime.toLocalDate())
}

fun getCurrentDateFormatted(): String {
    // 현재 날짜를 가져옵니다.
    val currentDate = LocalDate.now()
    // 원하는 포맷을 정의합니다.
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    // 포맷을 사용하여 날짜를 문자열로 변환합니다.
    return currentDate.format(formatter)
}

// 00 일째 링크드아웃! 의 계산함수
fun calculateDaysDifference(dateString: String): Long {
    // 입력된 날짜를 ZonedDateTime으로 파싱
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    val parsedDate = ZonedDateTime.parse(dateString, formatter)

    // 현재 날짜를 가져오기 (시스템 기본 시간대 사용)
    val currentDate = LocalDate.now()

    // 입력된 날짜의 LocalDate 가져오기
    val targetDate = parsedDate.toLocalDate()

    // 두 날짜 간의 차이 계산
    return ChronoUnit.DAYS.between(targetDate, currentDate)
}

// 로그인 이후 30일 후의 날짜계산
fun calculateDateAfter30Days(): String {
    val currentDate = LocalDate.now() // 현재 날짜
    val futureDate = currentDate.plusDays(30) // 30일 이후의 날짜
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // 원하는 날짜 형식

    return futureDate.format(formatter) // 형식에 맞춰 문자열로 반환
}

// yyyy-mm-dd형태의 문자열이 주어졌을 때, 해당 날짜가 오늘 이후인지 확인하는 함수
fun isDateAfterToday(dateString: String): Boolean {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateString, formatter) // 문자열을 LocalDate로 변환
    val today = LocalDate.now() // 현재 날짜

    return date.isAfter(today) // 주어진 날짜가 오늘 이후인지 판단
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