package com.echoist.linkedout.page

import SingleEssayResponse
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.components.LastEssayPager
import com.echoist.linkedout.data.DetailEssayResponse
import com.echoist.linkedout.data.EssayListResponse
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.MyLogViewModel
import retrofit2.Response

class FakeEssayApi : EssayApi {
    // EssayApi 인터페이스의 메서드들을 오버라이드하여 가짜 데이터나 테스트 데이터를 반환합니다.
    override suspend fun writeEssay(
        accessToken: String,
        essayData: EssayApi.WritingEssayItem
    ): Response<SingleEssayResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun modifyEssay(
        accessToken: String,
        essayData: EssayApi.WritingEssayItem
    ): Response<SingleEssayResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEssay(accessToken: String, essayId: Int): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun readMyEssay(
        accessToken: String,
        published: Boolean?,
        categoryId: String,
        limit: Int
    ): Response<EssayListResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun readRandomEssays(
        accessToken: String,
        limit: Int
    ): Response<EssayListResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun readFollowingEssays(
        accessToken: String,
        limit: Int
    ): Response<EssayListResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun readOneSentences(
        accessToken: String,
        type: String,
        limit: Int
    ): Response<EssayListResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun readDetailEssay(
        accessToken: String,
        essayId: Int
    ): Response<DetailEssayResponse> {
        TODO("Not yet implemented")
    }
}

@Composable
fun MyLogDetailPage(navController: NavController, viewModel: MyLogViewModel) {
    val scrollState = rememberScrollState()


    LinkedOutTheme {
        Scaffold(
            topBar = {
                DetailTopAppBar(navController = navController, viewModel)
            },
            content = {
                Box(
                    Modifier
                        .padding(it)
                        .fillMaxSize(), contentAlignment = Alignment.TopCenter
                ) {

                    Column(Modifier.verticalScroll(scrollState)) {
                        DetailEssay(viewModel = viewModel)
                        LastEssayPager(viewModel = viewModel, navController = navController)
                    }
                    //수정 옵션
                    AnimatedVisibility(
                        visible = viewModel.isActionClicked,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = 1000,
                                easing = FastOutSlowInEasing
                            )
                        ),
                        exit = fadeOut(
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = LinearEasing
                            )
                        )
                    ) {

                        ModifyOption(viewModel, navController = navController)

                    }

                }
            }
        )
    }


}

@Composable
fun ModifyOption(viewModel: MyLogViewModel, navController: NavController) {

    var isStoryClicked by remember { mutableStateOf(false) }

    if (isStoryClicked){
        StoryModifyBox({
            isStoryClicked = false
            //todo 박스가 닫히는것 외에도 기능구현필요
        },{
            isStoryClicked = false
        })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 23.dp),
        contentAlignment = Alignment.TopEnd
    ){
        Surface(modifier = Modifier.size(180.dp, 305.dp), shape = RoundedCornerShape(2)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.text_minus),
                        contentDescription = "minus"
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(text = "가")
                    Spacer(modifier = Modifier.width(30.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.text_plus),
                        contentDescription = "minus"
                    )

                }
                HorizontalDivider()
                OptionItem(text = "수정", Color.White,{},R.drawable.ftb_edit)
                HorizontalDivider()
                OptionItem(text = "발행", Color.White,{},R.drawable.option_link)
                HorizontalDivider()
                OptionItem(text = "Linked-out", Color.White,{},R.drawable.option_linkedout)
                HorizontalDivider()
                OptionItem(text = "스토리 선택", Color(0xFF616FED),
                    {
                        isStoryClicked = true
                    },R.drawable.option_check)
                HorizontalDivider()
                OptionItem(text = "삭제", Color.Red,{
                    viewModel.deleteEssay(navController = navController)
                    Log.d(TAG, "ModifyOption: dd")
                },R.drawable.option_trash)

            }
        }
    }

}

@Composable
fun OptionItem(
    text: String,
    color: Color,
    onClick: () -> Unit,
    iconResource : Int
) {
    Box(
        Modifier
            .size(180.dp, 44.dp)
            .clickable { onClick() }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 20.dp,
                    end = 20.dp
                ), contentAlignment = Alignment.CenterStart
        ) {
            Text(text = text, fontSize = 16.sp, color = color)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 20.dp,
                    end = 20.dp
                ), contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                tint = color,
                painter = painterResource(id = iconResource),
                contentDescription = "",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopAppBar(navController: NavController, viewModel: MyLogViewModel) {

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = { },
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "arrow back",
                tint = if (isSystemInDarkTheme()) Color(0xFF727070) else Color.Gray,
                modifier = Modifier
                    .padding(start = 20.dp)
                    .clickable {
                        if (viewModel.detailEssayBackStack.isNotEmpty()) {
                            viewModel.detailEssayBackStack.pop()
                            Log.d(TAG, "pushpushpop: ${viewModel.detailEssayBackStack}")
                            if (viewModel.detailEssayBackStack.isNotEmpty()) {
                                viewModel.detailEssay = viewModel.detailEssayBackStack.peek()
                            }
                        }
                        navController.popBackStack()
                        viewModel.isActionClicked = false
                    } //뒤로가기
            )
        },
        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.more),
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 20.dp)
                    .clickable {
                        viewModel.isActionClicked = !viewModel.isActionClicked
                    },
            )
        })
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailEssay(viewModel: MyLogViewModel) {
    val essay = viewModel.detailEssay
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(170.dp))
            Text(text = essay.title!!, fontSize = 24.sp, modifier = Modifier)
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = essay.content!!,
                fontSize = 16.sp,
                modifier = Modifier,
                color = Color(0xFFB4B4B4)
            )
            Spacer(modifier = Modifier.height(46.dp))
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                Column {
                    Text(
                        text = "구루브",
                        fontSize = 12.sp,
                        textAlign = TextAlign.End,
                        color = Color(0xFF686868)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = essay.createdDate!!,
                        fontSize = 12.sp,
                        textAlign = TextAlign.End,
                        color = Color(0xFF686868)
                    )

                }
            }

        }
        if (essay.thumbnail != null) {
            GlideImage(
                model = essay.thumbnail, contentDescription = "", modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }
    }

}

@Composable
fun StoryModifyBox(
    isDeleteClicked: () -> Unit,
    isModifyClicked: () -> Unit
) {

    val items = listOf("Item 1", "Item 2","Item 3", "Item 4")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.5f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .background(Color(0xFF121212), shape = RoundedCornerShape(20))
                .align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "이 글을 어떤 스토리로 추가/변경 할까요?",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                SingleSelectableList(items)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { isDeleteClicked() },
                        modifier = Modifier
                            .weight(1f)
                            .height(61.dp)
                            .padding(end = 10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF868686)),
                        shape = RoundedCornerShape(20)
                    ) {
                        Text(text = "스토리에서 삭제", color = Color.Black, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = { isModifyClicked() },
                        modifier = Modifier
                            .weight(1f)
                            .height(61.dp)
                            .padding(start = 10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF616FED)),
                        shape = RoundedCornerShape(20)
                    ) {
                        Text(text = "추가/변경", color = Color.Black, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun SingleSelectableList(items: List<String>) { //todo string이 아닌 스토리아이템으로 가야할듯.
    // 선택된 항목을 추적하기 위한 상태 변수
    var selectedItem by remember { mutableStateOf<String?>(null) }

    LazyColumn(Modifier.height(250.dp)) {
        items(items) { item ->
            val isSelected = item == selectedItem
            val backgroundColor = if (isSelected) Color.Gray else Color.Transparent

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clickable {
                        // 같은 항목을 클릭하면 선택을 해제, 그렇지 않으면 항목을 선택
                        selectedItem = if (selectedItem == item) null else item

                    }
                    .background(backgroundColor)
                    .padding(16.dp)
            ) {

                    Text(text = item)
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd){
                    if (isSelected) Icon(painter = painterResource(id = R.drawable.option_check), tint = LinkedInColor, contentDescription = "")
                }

            }
        }
    }
}











