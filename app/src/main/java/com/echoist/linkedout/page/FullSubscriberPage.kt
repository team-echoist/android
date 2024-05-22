package com.echoist.linkedout.page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.api.UserApi
import com.echoist.linkedout.components.CommuTopAppBar
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.CommunityViewModel


//@Preview
//@Composable
//fun Prev3() {
//    FullSubscriberPage(viewModel = CommunityViewModel(), navController = rememberNavController())
//}
@Composable
fun FullSubscriberPage(
    viewModel: CommunityViewModel,
    navController: NavController
) {

    LinkedOutTheme {
        Scaffold(
            topBar = {
                CommuTopAppBar(text = "전체 구독 목록", navController)
            },
            bottomBar = { MyBottomNavigation(navController) },
            content = {

                    LazyColumn(modifier = Modifier
                        .padding(it)
                        .padding(top = 30.dp)){
                        items(viewModel.subscribeUserList){it-> //todo 랜덤리스트에서 구독자 에세이 리스트로 바꿔야함.
                            SubscriberSimpleItem(item = it,viewModel,navController)
                            Spacer(modifier = Modifier.height(10.dp))

                        }


                    }

            }
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SubscriberSimpleItem(
    item: UserApi.UserInfo,
    viewModel: CommunityViewModel,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .clickable {
                viewModel.userItem = item
                navController.navigate("SubscriberPage")
            }
            .fillMaxWidth()
            .height(100.dp)
            .padding(start = 20.dp, end = 20.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(20)), contentAlignment = Alignment.CenterStart) {
            Row(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                GlideImage(
                    model = R.drawable.bottom_nav_3,
                    contentDescription = "profileimage",
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.width(17.dp))
                Text(text = "${item.nickname} ", fontSize = 16.sp)
                Text(text = "아무개", fontSize = 16.sp, color = Color(0xFF656565))

            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 20.dp)
                .size(73.dp, 24.dp), contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "구독중",
                color = Color(0xFF616FED),
                fontSize = 12.sp,
                modifier = Modifier
                    .background(Color(0xFF191919), shape = RoundedCornerShape(20))
                    .padding(start = 20.dp, top = 3.dp, end = 20.dp, bottom = 3.dp)
            )
        }
    }
}