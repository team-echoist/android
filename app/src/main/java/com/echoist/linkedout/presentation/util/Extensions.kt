package com.echoist.linkedout.presentation.util

import android.util.Patterns

fun String.isEmailValid(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}