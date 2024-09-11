package com.echoist.linkedout

import android.util.Patterns
import androidx.navigation.NavController

fun String.isEmailValid(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun getCurrentRoute(navController: NavController): String? {
    // 현재 BackStackEntry를 가져와서 경로를 반환
    return navController.currentBackStackEntry?.destination?.route
}
