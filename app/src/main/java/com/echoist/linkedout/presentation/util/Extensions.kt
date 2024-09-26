package com.echoist.linkedout.presentation.util

import android.util.Patterns
import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier

fun String.isEmailValid(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun Modifier.throttleClick(
    throttleTime: Long = 1500L,
    onClick: () -> Unit
): Modifier {
    var lastClickTime = 0L
    return this.clickable {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= throttleTime) {
            onClick()
            lastClickTime = currentTime
        }
    }
}