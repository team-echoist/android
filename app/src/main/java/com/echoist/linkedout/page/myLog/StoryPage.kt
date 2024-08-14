package com.echoist.linkedout.page.myLog

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.echoist.linkedout.data.RelatedEssay
import com.echoist.linkedout.formatDateTime
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.MyLogViewModel
import kotlinx.coroutines.delay


@Composable
fun StoryPage(viewModel: MyLogViewModel, navController: NavController) {
    var isApiFinished by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit ) {
        viewModel.readStoryEssayList()
        isApiFinished = true
    }

    if (isApiFinished) {

        LinkedOutTheme {
            Scaffold(topBar = { StoryTopAppBar(navController) }, bottomBar = {})
            {
                Column(Modifier.padding(it)) {
                    StoryTitleTextField(viewModel)
                    StoryEssayListScreen(viewModel, navController)
                }
            }
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryTopAppBar(navController: NavController) {
    TopAppBar(modifier = Modifier.padding(horizontal = 10.dp),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = "스토리 만들기", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, fontSize = 16.sp)
            }
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                Modifier
                    .size(30.dp)

                    .clickable {
                        navController.popBackStack()
                    },
                tint = Color.White
            )
        }, actions = { Box(modifier = Modifier.size(30.dp))}

    )
}
//todo 스토리 수정하기들어갔을때 리스트값이 없는데 전체 1개 표시됨.
@Composable
fun StoryTitleTextField(viewModel: MyLogViewModel) {
    var text by remember { mutableStateOf(viewModel.getSelectedStory().name) }
    viewModel.storyTextFieldTitle = text

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color(0xFF131313))
            .padding(horizontal = 20.dp), contentAlignment = Alignment.CenterStart
    ) {
        TextField(
            textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.storyTextFieldTitle,
            singleLine = true,
            onValueChange =
            {
                text = it
                viewModel.storyTextFieldTitle = text
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            placeholder = { Text(text = "스토리 제목", fontSize = 20.sp, color = Color(0xFF5F5F5F)) }
        )
    }
}


@Composable
fun EssayItem(
    essayItem: RelatedEssay,
    isSelected: Boolean,
    onItemSelected: (Boolean) -> Unit
) {
    val color = if (isSelected) LinkedInColor else Color(0xFF252525)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = essayItem.title, fontSize = 14.sp)
            Text(text = formatDateTime(essayItem.createdDate), fontSize = 10.sp, color = Color(0xFF727070))
        }
        IconButton(onClick = { onItemSelected(!isSelected) }) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                tint = color,
                contentDescription = null
            )
        }
    }
}
@SuppressLint("MutableCollectionMutableState")
@Composable
fun StoryEssayListScreen(viewModel: MyLogViewModel, navController: NavController) {
    val essayItems = if (viewModel.isCreateStory) viewModel.createStoryEssayItems else viewModel.modifyStoryEssayItems

    val listState = rememberLazyListState()
    LaunchedEffect(listState) {
        // 스크롤 상태를 감지하는 LaunchedEffect
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull() }
            .collect { lastVisibleItem ->
                // 리스트의 마지막 아이템에 도달하면 추가로딩
                if (lastVisibleItem?.index == essayItems.size - 1) {
                    viewModel.readStoryEssayList()
                }
            }
    }

    var isFunFinished by remember{ mutableStateOf(false) }
    LaunchedEffect(key1 = Unit ) {
        delay(100)
        isFunFinished = true
    }
    val essayIdList by remember { mutableStateOf(mutableListOf<Int>()) }

    if (isFunFinished){
        var selectedItems by remember { mutableStateOf(viewModel.findEssayInStory().toSet()) }

        val annotatedString = remember {
            AnnotatedString.Builder().apply {
                append("전체 ")

                withStyle(
                    style = SpanStyle(
                        color = Color(0xFF616FED),
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("${essayItems.size} ")
                }
                append("개")
            }.toAnnotatedString()
        }

        Log.d(TAG, "StoryEssayListScreen: $selectedItems")
        Log.d(TAG, "StoryEssayListScreen: ${viewModel.createStoryEssayItems}")
        Log.d(TAG, "StoryEssayListScreen: ${viewModel.modifyStoryEssayItems}")



        Column(Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp), contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = annotatedString, fontSize = 12.sp
                    )
                }

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "전체 선택", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(2.dp))
                        IconButton(
                            onClick = {
                                selectedItems = if (selectedItems.size == essayItems.size) {
                                    emptySet()
                                } else {
                                    essayItems.toSet()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                tint = if (selectedItems.size == essayItems.size) LinkedInColor else Color(
                                    0xFF252525
                                ),
                                contentDescription = null
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 스토리에 들어갈 에세이 목록
            LazyColumn(modifier = Modifier.weight(0.9f), state = listState) {
                items(essayItems) { item ->
                    val isSelected = item in selectedItems
                    EssayItem(
                        essayItem = item,
                        isSelected = isSelected,
                        onItemSelected = { selected ->
                            selectedItems = if (selected) {
                                selectedItems + item
                            } else {
                                selectedItems - item
                            }
                        }
                    )
                }
            }

            essayIdList.clear()
            selectedItems.forEach {
                essayIdList.add(it.id)
            }

            Log.d(TAG, "$essayIdList")

            val containerColor =
                if (viewModel.storyTextFieldTitle.isNotEmpty()) LinkedInColor else Color(0xFF868686)
            //에세이 스토리 추가버튼
            Box(modifier = Modifier.navigationBarsPadding()){
                Button(
                    onClick = {
                        if (viewModel.storyTextFieldTitle.isNotEmpty()) {
                            if (viewModel.isCreateStory){ //스토리 생성
                                viewModel.createStory(navController,essayIdList)
                                Log.d("에세이 수정테스트", "스토리 생성입니다.: $selectedItems")
                            }
                            else{ // 스토리 수정
                                viewModel.modifyStory(navController,essayIdList)
                                Log.d("에세이 수정테스트", "스토리 수정입니다.: $selectedItems")

                            }
                        }
                    },
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = containerColor),
                    shape = RoundedCornerShape(20)
                ) {
                    Text("총 ${selectedItems.size}개의 글 모으기",color = Color.Black)
                }
            }

        }
    }

}




