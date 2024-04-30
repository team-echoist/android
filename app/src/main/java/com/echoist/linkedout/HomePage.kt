package com.echoist.linkedout

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Preview
@Composable
fun PrevBottomNav() {
    MyBottomNavigation(navController = rememberNavController())
}

@Composable
fun MyBottomNavigation(navController: NavHostController) {
    val items = listOf<BottomNavItem>(
        BottomNavItem.Home,
        BottomNavItem.MyLog,
        BottomNavItem.Community,
        BottomNavItem.Settings
    )
    NavigationBar(containerColor = Color.Black) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { it ->
            NavigationBarItem(
                selected = currentRoute == it.screenRoute,
                onClick = {
                    navController.navigate(it.screenRoute)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = it.icon),
                        contentDescription = it.title,
                        modifier = Modifier.size(25.dp),
                        tint = if (currentRoute == it.screenRoute) Color(0xFF68686) else Color.White
                    )
                })

        }
    }
}


sealed class BottomNavItem(
    val title: String,
    val icon: Int,
    val screenRoute: String
) {
    data object Home :
        BottomNavItem(
            "HOME",
            R.drawable.bottom_nav_1,
            "HOME" // nav Route 재지정 필요
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
