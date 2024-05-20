package com.echoist.linkedout.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.echoist.linkedout.components.ChoiceBox
import com.echoist.linkedout.components.CommunityChips
import com.echoist.linkedout.components.CommunityTopAppBar
import com.echoist.linkedout.components.RandomSentences
import com.echoist.linkedout.components.SentenceChoice
import com.echoist.linkedout.components.TodaysLogTitle
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.CommunityViewModel

@Composable
fun CommunityPage(navController : NavController,viewModel: CommunityViewModel){
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

                    Column(
                        Modifier
                            .background(Color(0xFFD9D9D9))
                            .padding(it)
                            .padding(top = 20.dp)) {
                        SentenceChoice(viewModel)
                        Spacer(modifier = Modifier.height(10.dp))
                        RandomSentences()
                        Spacer(modifier = Modifier.height(40.dp))

                        TodaysLogTitle()


                    }
                    AnimatedVisibility(
                        visible = !viewModel.isClicked,
                        enter = fadeIn(animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)),
                        exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
                    ) {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 175.dp, end = 10.dp), contentAlignment = Alignment.TopEnd){
                            ChoiceBox()
                        }
                    }
            }
        )
    }

}

@Preview
@Composable
fun Prev(){
    CommunityPage(navController = rememberNavController(), CommunityViewModel())
}
