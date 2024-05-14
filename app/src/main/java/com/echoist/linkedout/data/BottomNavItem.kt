package com.echoist.linkedout.data

import com.echoist.linkedout.R
import com.echoist.linkedout.page.Token

sealed class BottomNavItem(
    val title: String,
    val icon: Int,
    val screenRoute: String
) {
    data object Home :
        BottomNavItem(
            "HOME",
            R.drawable.bottom_nav_1,
            "HOME/${Token}" //
        )

    data object MyLog :
        BottomNavItem(
            "MYLOG",
            R.drawable.bottom_nav_2,
            "MYLOG"
        )

    data object Community :
        BottomNavItem(
            "COMMUNITY",
            R.drawable.bottom_nav_3,
            "COMMUNITY"
        )

    data object Settings :
        BottomNavItem(
            "SETTINGS",
            R.drawable.bottom_nav_4,
            "SETTINGS"
        )
}
