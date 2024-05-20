package com.echoist.linkedout.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.echoist.linkedout.components.CommunityChips
import com.echoist.linkedout.components.CommunityTopAppBar
import com.echoist.linkedout.ui.theme.LinkedOutTheme

@Composable
fun CommunityPage(navController : NavController){
    val pagerstate = rememberPagerState { 3 }
    val hasCalledApi = remember { mutableStateOf(false) }

    LinkedOutTheme {
        Scaffold(
            topBar = {
                Column {

                    CommunityTopAppBar()
                    CommunityChips()
                }
            },
            bottomBar = { MyBottomNavigation(navController) },
            content = {
                Box(Modifier.padding(it)) {
                }
            }
        )
    }

}

@Preview
@Composable
fun Prev(){
    CommunityPage(navController = rememberNavController())
}
