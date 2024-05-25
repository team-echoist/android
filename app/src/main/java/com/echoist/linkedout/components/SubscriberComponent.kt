package com.echoist.linkedout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.viewModels.CommunityViewModel

//@Preview
//@Composable
//fun PreviewTest() {
//    Column {
//        CommuTopAppBar("test", NavController(LocalContext.current))
//        SubscriberPage(CommunityViewModel(), NavController(LocalContext.current))
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommuTopAppBar(text: String, navController: NavController,viewModel: CommunityViewModel) {

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = {
            Row {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "",
                    Modifier
                        .size(30.dp)
                        .clickable {
                            navController.popBackStack()
                            viewModel.unSubscribeClicked = false
                        }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = text, fontWeight = FontWeight.Bold)

            }
        },
        actions = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "", Modifier.size(30.dp))
            Spacer(modifier = Modifier.width(13.dp))
            Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = "",
                Modifier.size(30.dp),
            )
            Spacer(modifier = Modifier.width(15.dp))
        }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SubscriberProfile(viewModel: CommunityViewModel) {

    val annotatedString = remember {
        AnnotatedString.Builder().apply {
            withStyle(
                style = SpanStyle(
                    color = Color(0xFF616FED),
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("${viewModel.userItem.nickname!!} ")
            }
            append("아무개")
        }.toAnnotatedString()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        GlideImage(
            model = R.drawable.bottom_nav_3,
            contentDescription = "profileImage",
            Modifier.size(108.dp)
        ) //todo 프로필이미지로 변경 viewmodel.userItem.profileImage
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = annotatedString, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.background(Color(0xFF0D0D0D), shape = RoundedCornerShape(20)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 10.dp, bottom = 10.dp)
                ) {
                    Text(text = "쓴글", fontSize = 10.sp, color = Color(0xFF616161))
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(text = "21", fontSize = 18.sp, color = Color(0xFF616161))
                }
                VerticalDivider(Modifier.height(41.dp), color = Color(0xFF191919))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "발행", fontSize = 10.sp, color = Color(0xFF616161))
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(text = "5", fontSize = 18.sp, color = Color(0xFF616161))
                }
                VerticalDivider(Modifier.height(41.dp), color = Color(0xFF191919))

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "링크드아웃", fontSize = 10.sp, color = Color(0xFF616161))
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(text = "12", fontSize = 18.sp, color = Color(0xFF616161))
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF191919), shape = RoundedCornerShape(20))
                .height(40.dp), contentAlignment = Alignment.Center
        ) {
            Text(text = "구독중", fontSize = 14.sp, color = Color(0xFF616161))
        }
        Spacer(modifier = Modifier.height(30.dp))

    }
}


@Composable
fun SubscriberPage(viewModel: CommunityViewModel,navController : NavController){
    LazyColumn{
        item {
            SubscriberProfile(viewModel = viewModel)

        }
        items(viewModel.randomList){it-> //todo 랜덤리스트에서 구독자 에세이 리스트로 바꿔야함.
            EssayListItem(item = it, viewModel = viewModel, navController = navController)
        }


    }

}
