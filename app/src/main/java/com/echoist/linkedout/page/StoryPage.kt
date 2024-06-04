package com.echoist.linkedout.page

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.StoryViewModel


@Preview
@Composable
fun PreviewStoryPage() {
    StoryPage(viewModel = StoryViewModel())
}

@Composable
fun StoryPage(viewModel: StoryViewModel) {


    LinkedOutTheme {
        Scaffold(topBar = { StoryTopAppBar() }
        , bottomBar = {})
        {
            Column(Modifier.padding(it)) {
                StoryTitleTextField(viewModel)
                EssayListScreen()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryTopAppBar() {
    TopAppBar(modifier = Modifier.padding(horizontal = 10.dp),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = "스토리 만들기", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            }
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                Modifier
                    .size(30.dp)
                    .clickable { /** nav back **/ },
                tint = Color.White
            )
        }, actions = { Text(text = "완료") }
    )
}

@Composable
fun StoryTitleTextField(viewModel: StoryViewModel) {
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
                viewModel.storyTextFieldTitle = it
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            placeholder = { Text(text = "에세이 제목", fontSize = 20.sp) }
        )
    }
}


@Composable
fun EssayItem(
    essayItem: EssayApi.EssayItem,
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
            Text(text = essayItem.title!!)
            Text(text = essayItem.createdDate!!, fontSize = 10.sp, color = Color(0xFF727070))
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

@Composable
fun EssayListScreen() {
    val essayItems = listOf(
        EssayApi.EssayItem(title = "답하기 싫은 질문", createdDate = "2024.02.22"),
        EssayApi.EssayItem(title = "해맑수록 내 땅", createdDate = "2024.02.22"),
        EssayApi.EssayItem(title = "한강이 싫어졌다", createdDate = "2024.02.22"),
        EssayApi.EssayItem(title = "독일에 가고 싶을 때", createdDate = "2024.02.22"),
        EssayApi.EssayItem(title = "모기가 생기는 계절", createdDate = "2024.02.22"),
        EssayApi.EssayItem(title = "돈을 쓰면 쓸수록", createdDate = "2024.02.22"),
        EssayApi.EssayItem(title = "늦게 일어날 때 드는 기분", createdDate = "2024.02.22")
    )

    var selectedItems by remember { mutableStateOf(setOf<EssayApi.EssayItem>()) }

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

    Column(Modifier.padding(horizontal = 20.dp)) {
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp), contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = annotatedString
                )
            }

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "전체 선택")
                    Spacer(modifier = Modifier.width(5.dp))
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

        LazyColumn(modifier = Modifier.weight(0.9f)) {
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

        Button(
            onClick = { /* handle collect action */ },
            modifier = Modifier

                .height(90.dp)
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(20)
        ) {
            Text("총 ${selectedItems.size}개의 글 모으기")
        }

    }



}





