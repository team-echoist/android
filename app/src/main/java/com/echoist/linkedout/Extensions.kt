package com.echoist.linkedout

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Patterns
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
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

fun startActivityToPlayStore(context: Context){
    val playStoreIntent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
        setPackage("com.android.vending")
    }
    ContextCompat.startActivity(context, playStoreIntent, Bundle.EMPTY)
}