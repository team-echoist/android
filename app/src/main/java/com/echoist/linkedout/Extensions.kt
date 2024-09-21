package com.echoist.linkedout

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Patterns
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.echoist.linkedout.viewModels.WritingViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun String.isEmailValid(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun getCurrentRoute(navController: NavController): String? {
    // 현재 BackStackEntry를 가져와서 경로를 반환
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

data class LocalAccountInfo(val id: String, val pw: String)
//셀프알림 리마인드 true, false 설정

fun getPeriodString(index: Int): String = periodMap[index] ?: "오전"
fun getHourString(index: Int): String = hourMap[index] ?: "01"
fun getMinuteString(index: Int): String = minuteMap[index] ?: "00"


@Composable
fun keyboardAsState(): State<Keyboard> {
    val keyboardState = remember { mutableStateOf(Keyboard.Closed) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
                Keyboard.Opened
            } else {
                Keyboard.Closed
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }
    return keyboardState
}

enum class Keyboard {
    Opened, Closed
}