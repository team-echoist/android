package com.echoist.linkedout.page.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.data.BadgeBoxItem
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.page.home.MyBottomNavigation
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.SettingsViewModel
import kotlinx.coroutines.delay

//@Preview
//@Composable
//fun MyPagePreview() {
//    MyPage(navController = NavController(LocalContext.current))
//}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MyPage(
    viewModel: SettingsViewModel,
    navController: NavController
) {


    viewModel.readSimpleBadgeList(navController)
    viewModel.getUserInfo()

    val userItem by remember {
        mutableStateOf(
            UserInfo(
                id = 1,
                nickname = "구루브",
                profileImage = "http",
                password = "1234",
                gender = "male",
                birthDate = "0725"
            )
        )
    }
    val scrollState = rememberScrollState()

    LinkedOutTheme {
        Scaffold(
            topBar = {
                Text(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    text = "MY",
                    color = Color.White,
                    modifier = Modifier.padding(start = 20.dp)
                )
            },
            bottomBar = { MyBottomNavigation(navController) },
            content = {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .verticalScroll(scrollState)

                ) {
                    MySettings(item = userItem)
                    SettingBar("링크드아웃 배지") {viewModel.readDetailBadgeList(navController)}
                    LinkedOutBadgeGrid(viewModel)
                    SettingBar("최근 본 글") {}
                    RecentEssayList(itemList = viewModel.detailEssay)
                    SettingBar("멤버십 관리") {}
                    SettingBar("계정 관리") {}


                }

                if (viewModel.isBadgeClicked) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        BadgeDescriptionBox(viewModel.badgeBoxItem!!, viewModel)
                    }
                }


            }
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MySettings(item: UserInfo) {

    val annotatedString = remember {
        AnnotatedString.Builder().apply {
            withStyle(
                style = SpanStyle(
                    color = Color(0xFF616FED),
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("${item.nickname!!} ")
            }
            append("아무개")
        }.toAnnotatedString()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
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
                .background(Color(0xFF616FED), shape = RoundedCornerShape(20))
                .height(50.dp), contentAlignment = Alignment.Center
        ) {
            Text(text = "프로필 편집", fontSize = 14.sp, color = Color(0xFF616161))
        }

    }
}

@Composable
fun SettingBar(text: String, onClick: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(20.dp)
        .height(50.dp)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF616FED)
            )
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "navigate",
                tint = Color(0xFF686868)
            )

        }
    }
}

@Composable
fun RecentEssayItem(item: EssayApi.EssayItem) {
    Box(modifier = Modifier.size(150.dp, 120.dp)) {
        Column {
            Text(text = item.title!!)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = item.content!!,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun RecentEssayList(itemList: List<EssayApi.EssayItem>) {
    LazyRow(Modifier.padding(start = 20.dp)) {
        itemsIndexed(itemList) { index, item ->
            RecentEssayItem(item)

            // 마지막 항목인 경우에만 Spacer와 VerticalDivider 실행
            if (index < itemList.size - 1) {
                Spacer(modifier = Modifier.width(10.dp))
                VerticalDivider(Modifier.height(100.dp))
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Composable
fun BadgeDescriptionBox(badgeBoxItem: BadgeBoxItem, viewModel: SettingsViewModel) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(0.5f)))
    Box(modifier = Modifier.size(300.dp, 350.dp)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Image(
                painter = painterResource(id = R.drawable.badge_container),
                contentDescription = ""
            )
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = badgeBoxItem.badgeResourceId),
                contentDescription = "",
                Modifier
                    .size(150.dp)
                    .padding(end = 20.dp, bottom = 10.dp)
            )
            Spacer(modifier = Modifier.height(23.dp))
            Text(text = badgeBoxItem.badgeName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "${badgeBoxItem.badgeEmotion} 감정 표현 해시태그",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(
                    text = "  10개  ", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier =
                    Modifier
                        .background(Color.Blue, shape = RoundedCornerShape(40))
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = " 사용", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(23.dp))
            Button(
                onClick = { viewModel.isBadgeClicked = false },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20)
            ) {
                Text(text = "확인")
            }
        }
    }

}

//터치했을때 아이콘 확대축소 가능하게?
@Composable
fun LinkedOutBadgeItem(
    badgeBoxItem: BadgeBoxItem,
    viewModel: SettingsViewModel
) {
    // 기본 Modifier 정의
    val baseModifier = Modifier
        .size(110.dp)
        .clickable {
            if (badgeBoxItem.level >= 1) {
                viewModel.badgeBoxItem = badgeBoxItem
                viewModel.isBadgeClicked = true
            }
        }
        .background(
            Color(0xFF0D0D0D),
            shape = RoundedCornerShape(10)
        )

    // badgeBoxItem의 level에 따라서 blur 효과를 다르게 적용
    val finalModifier = if (badgeBoxItem.level == 0) {
        baseModifier.blur(20.dp)
    } else {
        baseModifier
    }

    val colorMatrix = ColorMatrix().apply {
        if (badgeBoxItem.level == 0) setToSaturation(0f)
        else setToSaturation(1f)
    }

    Box(contentAlignment = Alignment.Center, modifier = finalModifier) {
        Image(
            modifier = Modifier.size(80.dp),
            painter = painterResource(id = badgeBoxItem.badgeResourceId),
            contentDescription = "badge_sad",
            colorFilter = ColorFilter.colorMatrix(colorMatrix)
        )
    }
}


@Composable
fun LinkedOutBadgeGrid(viewModel: SettingsViewModel) {
val badgeList = viewModel.simpleBadgeList

    LaunchedEffect(Unit) {
        delay(50)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(horizontal = 16.dp), // 수평 방향으로 패딩 추가
        contentAlignment = Alignment.Center

    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(badgeList) {
                LinkedOutBadgeItem(it,viewModel)
            }
            // 각 아이템에 대한 UI를 구성
            // 여기에는 각 아이템을 표시하는 Composable 함수 호출
        }
    }
}



