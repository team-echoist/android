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
