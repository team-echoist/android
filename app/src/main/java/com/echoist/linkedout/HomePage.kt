package com.echoist.linkedout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.echoist.linkedout.ui.theme.LinkedOutTheme

@Preview
@Composable
fun HomePage(){
    LinkedOutTheme{
        Scaffold(bottomBar = {PrevBottomNav()}, topBar = { CustomTopAppBar() }) {
            Column(modifier = Modifier.padding(it)) {
                Text(text = "Welcome to Home Page!", modifier = Modifier.padding(16.dp))

            }

        }
    }
}
@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(){
    TopAppBar(title = { }, colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
        navigationIcon = {
            Icon(
                tint = Color.White,
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                modifier = Modifier
                    .clickable { }
                    .padding(start = 20.dp)
            )
        },
        actions = {
            Icon(
                Icons.Default.Notifications,
                contentDescription = "Notifications",
                modifier = Modifier
                    .padding(end = 20.dp)
                    .size(30.dp)
            )
        })

}


@Preview
@Composable
fun PrevBottomNav() {
    MyBottomNavigation(navController = rememberNavController())
}

@Composable
fun MyBottomNavigation(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.MyLog,
        BottomNavItem.Community,
        BottomNavItem.Settings
    )
    NavigationBar(containerColor = Color.Black) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach {item->
            NavigationBarItem(
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size(25.dp),
                        tint = if (currentRoute == item.screenRoute) Color(0xFF686868) else Color.White
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
            "HOME" // nav Route 전부 재지정 필요
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
