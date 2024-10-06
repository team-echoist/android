package com.echoist.linkedout.presentation.util

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.echoist.linkedout.BuildConfig
import com.echoist.linkedout.presentation.essay.write.WritingViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun getCurrentRoute(navController: NavController): String? {
    return navController.currentBackStackEntry?.destination?.route
}

fun getFileFromUri(uri: Uri, context: Context): File {
    val contentResolver = context.contentResolver
    val fileName = getFileName(uri, contentResolver)
    val file = File(context.cacheDir, fileName.toString())
    val inputStream: InputStream? = contentResolver.openInputStream(uri)
    val outputStream = FileOutputStream(file)
    inputStream?.copyTo(outputStream)
    inputStream?.close()
    outputStream.close()
    return file
}

private fun getFileName(uri: Uri, contentResolver: ContentResolver): String? {
    var name: String? = null
    val cursor = contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            name = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
        }
    }
    return name
}

fun startActivityToPlayStore(context: Context) {
    val playStoreIntent = Intent(Intent.ACTION_VIEW).apply {
        data =
            Uri.parse("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
        setPackage("com.android.vending")
    }
    ContextCompat.startActivity(context, playStoreIntent, Bundle.EMPTY)
}

// 공통 버튼 로직을 함수로 추출
fun handleEssayAction(status: String, viewModel: WritingViewModel, navController: NavController) {
    if (viewModel.isModifyClicked) {
        viewModel.modifyEssay(navController, status)
    } else {
        viewModel.writeEssay(navController, status)
    }
    viewModel.essayPrimaryId?.let { essayId ->
        viewModel.deleteEssay(essayId = essayId)
    }
}

val periodMap = mapOf(0 to "AM", 1 to "PM")
val hourMap = (0..11).associateWith { (it + 1).toString().padStart(2, '0') }
val minuteMap = (0..5).associateWith { (it * 10).toString().padStart(2, '0') }

fun getPeriodString(index: Int): String = periodMap[index] ?: "오전"
fun getHourString(index: Int): String = hourMap[index] ?: "01"
fun getMinuteString(index: Int): String = minuteMap[index] ?: "00"

fun navigateWithClearBackStack(
    navController: NavController,
    route: String,
    launchSingleTop: Boolean = true
) {
    navController.navigate(route) {
        popUpTo(0) {
            inclusive = true
        }
        this.launchSingleTop = launchSingleTop
    }
}

//2024-07-01T14:22:46.803+09:00 to 2024.07.01
fun formatDateTime(input: String): String {

    try {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        val dateTime = ZonedDateTime.parse(input, inputFormatter)
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

        return dateTime.format(outputFormatter)
    } catch (e: Exception) {
        e.printStackTrace()
        return input
    }
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