package com.echoist.linkedout.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.echoist.linkedout.components.CommuTopAppBar
import com.echoist.linkedout.components.SubscriberPage
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.CommunityViewModel

@Composable
fun SubscriberProfilePage(
    viewModel: CommunityViewModel,
    navController: NavController
){

    LinkedOutTheme {
        Scaffold(
            topBar = {
                    CommuTopAppBar(text = "프로필",navController,viewModel)
            },
            bottomBar = { MyBottomNavigation(navController) },
            content = {
                Column(modifier = Modifier.padding(it)) {
                    SubscriberPage(viewModel, navController)
                }
            }
        )
    }
}

@Preview
@Composable
fun Prev2(){
    SubscriberProfilePage(viewModel = CommunityViewModel(), navController = rememberNavController())
}