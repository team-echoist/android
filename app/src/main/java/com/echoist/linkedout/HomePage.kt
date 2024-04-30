package com.echoist.linkedout

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Preview
@Composable
fun TestBottomNav() {
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
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { it ->
            NavigationBarItem(
                selected = currentRoute == it.screenRoute,
                onClick = {
                    navController.navigate(it.screenRoute)
                },
                icon = {
                    if (currentRoute == it.screenRoute) {
                        Icon(
                            painter = painterResource(id = it.selectedIcon),
                            contentDescription = it.title
                        )
                    } else
                        Icon(
                            painter = painterResource(id = it.unSelectedIcon),
                            contentDescription = it.title,
                            modifier = Modifier.size(20.dp)
                        )
                })
        }
    }
}




sealed class BottomNavItem(
    val title: String,
    val unSelectedIcon: Int,
    val selectedIcon: Int,
    val screenRoute: String
) {
    data object Home :
        BottomNavItem(
            "HOME",
            R.drawable.nav_1_unselected,
            R.drawable.nav_1_selected,
            "HOME" // nav Route 재지정 필요
        )

    data object MyLog :
        BottomNavItem(
            "MYLOG",
            R.drawable.nav_2_unselected,
            R.drawable.nav_2_selected,
            "MYLOG"
        )

    data object Community :
        BottomNavItem(
            "COMMUNITY",
            R.drawable.nav_3_unselected,
            R.drawable.nav_3_selected,
            "COMMUNITY"
        )

    data object Settings :
        BottomNavItem(
            "SETTINGS",
            R.drawable.nav_4_unselected,
            R.drawable.nav_4_selected,
            "SETTINGS"
        )
}
