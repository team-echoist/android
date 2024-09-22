package com.echoist.linkedout.data.dto

import com.echoist.linkedout.R
import com.echoist.linkedout.presentation.util.Routes

sealed class BottomNavItem(
    val title: String,
    val icon: Int,
    val screenRoute: String
) {
    data object Home :
        BottomNavItem(
            Routes.Home,
            R.drawable.bottom_nav_1,
            "${Routes.Home}/200" //
        )

    data object MyLog :
        BottomNavItem(
            Routes.MyLog,
            R.drawable.bottom_nav_2,
            "${Routes.MyLog}/0"
        )

    data object Community :
        BottomNavItem(
            Routes.Community,
            R.drawable.bottom_nav_3,
            Routes.Community
        )

    data object Settings :
        BottomNavItem(
            Routes.Settings,
            R.drawable.bottom_nav_4,
            Routes.Settings
        )
}
