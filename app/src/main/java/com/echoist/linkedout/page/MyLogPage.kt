package com.echoist.linkedout.page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.echoist.linkedout.components.EssayChips
import com.echoist.linkedout.components.EssayPager
import com.echoist.linkedout.components.MyLogTopAppBar
import com.echoist.linkedout.page.Token.accessToken
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.MyLogViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyLogPage(navController : NavController,viewModel: MyLogViewModel){
    val pagerstate = rememberPagerState { 3 }


    LinkedOutTheme {
        Scaffold(
            topBar = {
                Column {
                    MyLogTopAppBar()
                    EssayChips(pagerstate)
                }
            },
            bottomBar = { MyBottomNavigation(navController) },
            floatingActionButton = { WriteFTB(navController,accessToken) },
            content = {
                Box(Modifier.padding(it)) {
                    EssayPager(pagerState = pagerstate,viewModel)
                }
            }
        )
    }

}
