package com.echoist.linkedout.presentation.util

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

const val PREFS_NAME = "prefs"
const val KEY_PERIOD_INDEX = "period_index"
const val KEY_HOUR_INDEX = "hour_index"
const val KEY_MINUTE_INDEX = "minute_index"

const val ID_LOCAL_STORAGE = "id"
const val PW_LOCAL_STORAGE = "pw"

const val DISPLAY_INFO = DARK_MODE

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
    const val ThemeModeScreen = "ThemeModeScreen"
    const val NotificationPage = "NotificationPage"
    const val NotificationSettingScreen = "NotificationSettingScreen"
    const val NoticeDetailPage = "NoticeDetailPage"
    const val SupportScreen = "SupportScreen"
    const val LinkedOutSupportScreen = "LinkedOutSupportScreen"
    const val InquiryScreen = "InquiryScreen"
    const val NoticeScreen = "NoticeScreen"
    const val UpdateHistoryScreen = "UpdateHistoryScreen"
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