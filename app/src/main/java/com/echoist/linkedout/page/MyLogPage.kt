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
import androidx.navigation.NavController
import com.echoist.linkedout.components.EssayChips
import com.echoist.linkedout.components.EssayPager
import com.echoist.linkedout.components.ModifyStoryBox
import com.echoist.linkedout.components.MyLogTopAppBar
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.MyLogViewModel


@Composable
fun MyLogPage(navController : NavController,viewModel: MyLogViewModel){
    val pagerstate = rememberPagerState { 3 }
    val hasCalledApi = remember { mutableStateOf(false) }

    if (!hasCalledApi.value) {
        viewModel.readMyEssay()
        viewModel.readPublishEssay()

        hasCalledApi.value = true
    }

//todo 첫 구글로그인할때 토큰안들어가는듯

    LinkedOutTheme {

        Scaffold(

            topBar = {
                Column {

                    MyLogTopAppBar()
                    EssayChips(pagerstate,viewModel)
                }
            },
            bottomBar = { MyBottomNavigation(navController) },
            floatingActionButton = { WriteFTB(navController) },
            content = {
                Box(Modifier.padding(it)) {
                    EssayPager(pagerState = pagerstate,viewModel, navController = navController)
                }
            }
        )
        if (viewModel.isModifyStoryClicked){
            ModifyStoryBox(onEditClick = {
                navController.navigate("StoryPage")
                viewModel.isModifyStoryClicked = false}, onDeleteClick = { /*TODO*/
                viewModel.isModifyStoryClicked = false}, onCancelClick = {viewModel.isModifyStoryClicked = false})

        }

    }

}
