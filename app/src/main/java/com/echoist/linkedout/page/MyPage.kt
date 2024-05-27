package com.echoist.linkedout.page

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.ui.theme.LinkedOutTheme

@Preview
@Composable
fun MyPagePreview() {
    MyPage(navController = NavController(LocalContext.current))
}

@Composable
fun MyPage(
    navController: NavController
) {

    var detailEssay by mutableStateOf(
        EssayApi.EssayItem(
            author = UserInfo(1,"groove"),
            content = "이 에세이는 예시입니다. 예시입니다. 예시입니다니다. 예시입니다. 예시입니다. 예시입니다.",
            createdDate = "2024년 04월 28일 16:47",
            id = 1,
            linkedOutGauge = 5,
            status = "published",
            thumbnail = "http 값 있어요~",
            title = "예시 에세이",
            updatedDate = "2024-05-15",
            tags = listOf(EssayApi.Tag(1,"tag"),EssayApi.Tag(1,"tag"))
        )
    )
    var essayList : SnapshotStateList<EssayApi.EssayItem> = mutableStateListOf(detailEssay,detailEssay,detailEssay,detailEssay)


    val userItem by mutableStateOf(
        UserInfo(
            id = 1,
            nickname = "구루브",
            profileImage = "http",
            password = "1234",
            gender = "male",
            birthDate = "0725"
        )
    )
    val scrollState= rememberScrollState()

    LinkedOutTheme {
        Scaffold(
            topBar = { Text(fontSize = 24.sp, fontWeight = FontWeight.Bold, text = "MY", color = Color.White, modifier = Modifier.padding(start = 20.dp)) },
            bottomBar = { MyBottomNavigation(navController) },
            content = {
                Column(modifier = Modifier.padding(it).verticalScroll(scrollState) ) {
                    MySettings(item = userItem)
                    SettingBar("링크드아웃 배지"){}
                    LinkedOutBadgeGrid()
                    SettingBar("최근 본 글"){}
                    RecentEssayList(itemList = essayList)
                    SettingBar("멤버십 관리"){}
                    SettingBar("계정 관리"){}
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
fun SettingBar(text : String, onClick : ()-> Unit){
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable { }
        .padding(20.dp)
        .height(50.dp)){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart){
            Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF616FED))
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd){
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = "navigate", tint = Color(0xFF686868))

        }
    }
}

@Composable
fun RecentEssayItem(item: EssayApi.EssayItem){
    Box(modifier = Modifier.size(150.dp,120.dp)){
        Column {
            Text(text = item.title!!)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = item.content, maxLines = 3, overflow = TextOverflow.Ellipsis, fontSize = 10.sp)
        }
    }
}

@Composable
fun RecentEssayList(itemList : List<EssayApi.EssayItem>){
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
fun LinkedOutBadgeItem(item: String){
    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .size(100.dp, 100.dp)
        .clickable {  }
        .background(Color(0xFF0D0D0D), shape = RoundedCornerShape(10))){
        Column {
            Text(text = item, color = Color.White)
        }
    }
}

@Composable
fun LinkedOutBadgeGrid(){
    val itemList = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5","Item 1", "Item 2", "Item 3", "Item 4")
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(325.dp)
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
            items(itemList) { item ->
                LinkedOutBadgeItem(item)
                // 각 아이템에 대한 UI를 구성
                // 여기에는 각 아이템을 표시하는 Composable 함수 호출
            }
        }
    }
}



