package com.echoist.linkedout.page

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.BookMarkViewModel
import kotlinx.coroutines.delay

@Composable
fun CommunitySavedEssayPage(navController: NavController, viewModel: BookMarkViewModel) {
    viewModel.readMyBookMarks()
    LinkedOutTheme {
        Scaffold(topBar = {
            SavedEssayTopAppBar(
                { viewModel.isSavedEssaysModifyClicked = false },
                onClickModify = {
                    viewModel.isSavedEssaysModifyClicked = !viewModel.isSavedEssaysModifyClicked
                },
                navController = navController
            )
        },
            bottomBar = {})
        {
            Column(Modifier.padding(it)) {
                SavedEssayListScreen(viewModel, navController)

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedEssayTopAppBar(
    onClickNavBack: () -> Unit,
    onClickModify: () -> Unit,
    navController: NavController
) {

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "",
                    Modifier
                        .size(30.dp)
                        .clickable {
                            navController.popBackStack()
                            onClickNavBack()
                        },
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "저장한 글", fontWeight = FontWeight.SemiBold, fontSize = 24.sp)

            }
        },
        actions = {
            Spacer(modifier = Modifier.width(13.dp))
            Text(text = "편집", fontSize = 16.sp, modifier = Modifier.clickable { onClickModify() })
            Spacer(modifier = Modifier.width(15.dp))


        }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SavedEssayListItem(
    item: EssayApi.EssayItem,
    isSelected: Boolean,
    onItemSelected: (Boolean) -> Unit,
    onClickItem: () -> Unit,
    viewModel: BookMarkViewModel
) {
    val color = if (isSystemInDarkTheme()) Color.White else Color.Black

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .clickable {
                onClickItem()
                //navigate
            }
            .height(140.dp)
    ) {



        Row {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(7f)
                    .padding(top = 0.dp, start = 20.dp, end = 20.dp)
            ) {
                Row(
                    Modifier.padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier,
                        text = item.title!!,
                        color = color,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(text = "   • 10 분", fontSize = 10.sp, color = Color(0xFF686868))
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = item.content!!,
                    maxLines = 2,
                    color = color,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (item.thumbnail != null) {
                GlideImage(
                    model = item.thumbnail,
                    contentDescription = null,
                    modifier = Modifier
                        .size(110.dp)
                        .padding(start = 20.dp, top = 20.dp, bottom = 20.dp)
                        .weight(3f)
                )
            }
        }

        if (viewModel.isSavedEssaysModifyClicked && !isSelected){
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.7f)))
        }


        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, bottom = 10.dp)
        ) {
            Text(
                text = item.author?.nickname ?: "",
                fontSize = 10.sp,
                color = Color(0xFF686868)
            )
        }
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.fillMaxSize()
        ) {
            HorizontalDivider(color = Color(0xFF686868))
        }
        val checkColor = if (isSelected) LinkedInColor else Color(0xFF252525)

        if (viewModel.isSavedEssaysModifyClicked) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                IconButton(onClick = { onItemSelected(!isSelected)
                }) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        tint = checkColor,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun SavedEssayListScreen(viewModel: BookMarkViewModel, navController: NavController) {
    val essayItems = viewModel.bookMarkEssayList

    var isFunFinished by remember { mutableStateOf(false) }
    LaunchedEffect(essayItems) {
        delay(100)
        isFunFinished = true
    }

    if (isFunFinished) {
        var selectedItems by remember { mutableStateOf<Set<EssayApi.EssayItem>>(emptySet()) }

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

        Log.d(ContentValues.TAG, "StoryEssayListScreen: $selectedItems")

        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(20.dp))

            if (viewModel.isSavedEssaysModifyClicked) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(text = annotatedString, fontSize = 12.sp)
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            val textColor = if (selectedItems.size != essayItems.size) Color(0xFF727070) else Color.White

                            Text(text = "전체 선택", fontSize = 12.sp, color = textColor)
                            Spacer(modifier = Modifier.width(5.dp))
                            IconButton(
                                modifier = Modifier.size(20.dp),
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
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (essayItems.isNotEmpty()){
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(essayItems) { item ->
                        val isSelected = item in selectedItems
                        SavedEssayListItem(
                            item = item,
                            isSelected = isSelected,
                            onItemSelected = { selected ->
                                selectedItems = if (selected) {
                                    selectedItems + item
                                } else {
                                    selectedItems - item
                                }
                            },
                            onClickItem = {
                                if (!viewModel.isSavedEssaysModifyClicked) {
                                    viewModel.readDetailEssay(item.id!!, navController)
                                }
                            },
                            viewModel = viewModel
                        )
                    }
                }
            }
            else
            {

                Text(text = "저장한 글이 없습니다.", fontSize = 20.sp, modifier = Modifier.padding(start = 20.dp))
            }

                val containerColor =
                    if (selectedItems.isNotEmpty()) LinkedInColor else Color(0xFF868686)

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomCenter) {
                    Button(
                        onClick = {
                            if (selectedItems.isNotEmpty()) {
                                // 에세이 선택 삭제
                            }
                        },
                        modifier = Modifier
                            .height(90.dp)
                            .fillMaxWidth()
                            .padding(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
                        shape = RoundedCornerShape(20)
                    ) {
                        Text("총 ${selectedItems.size}개 삭제")
                    }
                }

        }
    }
}

