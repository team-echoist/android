package com.echoist.linkedout.page

import android.content.ContentValues
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.components.EssayListItem
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.BookMarkViewModel
import com.echoist.linkedout.viewModels.CommunityViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview
@Composable
fun CommunityDetailPagePreview() {
    val viewModel : CommunityViewModel = viewModel()

    CommunityDetailPage(rememberNavController(), viewModel)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityDetailPage(navController: NavController, viewModel: CommunityViewModel) {
    var isClicked by remember { mutableStateOf(false) }

    LinkedOutTheme {
        Scaffold(
            topBar = {

                    CommunityTopAppBar(navController = navController, viewModel)

            },
            content = { it ->
                CompositionLocalProvider(LocalRippleConfiguration provides null) {
                    Box(
                        Modifier
                            .clickable { isClicked = !isClicked }
                            .padding(it)
                            .fillMaxSize(), contentAlignment = Alignment.TopCenter
                    ) {
                        LazyColumn {
                            item {

                                    DetailEssay(item = viewModel.detailEssay,viewModel)
                                    Spacer(modifier = Modifier.height(28.dp))
                                    SubscriberSimpleItem(
                                        item = viewModel.userItem,
                                        viewModel = viewModel,
                                        navController = navController
                                    )
                                    Spacer(modifier = Modifier.height(36.dp))
                                    Box(
                                        modifier = Modifier
                                            .height(12.dp)
                                            .fillMaxWidth()
                                            .background(Color(0xFF1A1A1A))
                                    )
                                    Box(
                                        modifier = Modifier
                                            .height(56.dp)
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp),
                                        contentAlignment = Alignment.CenterStart
                                    )
                                    {
                                        Text(
                                            text = "'${viewModel.userItem.nickname!!} 아무개'의 이전 글",
                                            fontSize = 14.sp,
                                            color = Color(0xFF616FED)
                                        )

                                    }
                            }
                            //todo 글쓴이의 이전 글 띄우기
                            items(items = viewModel.previousEssayList) { it -> //랜덤리스트 말고 수정할것. 그사람의 리스트로
                                EssayListItem(
                                    item = it,
                                    viewModel = viewModel,
                                    navController = navController
                                )
                            }


                        }

                        //수정 옵션
                        AnimatedVisibility(
                            visible = viewModel.isOptionClicked,
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
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(end = 23.dp),
                                contentAlignment = Alignment.TopEnd
                            ) {
                                ReportOption({},viewModel)
                            }
                        }

                    }
                }
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){
                    LaunchedEffect(isClicked) {
                        delay(3000)
                        isClicked = false
                    }

                    AnimatedVisibility(
                        visible = isClicked,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = 500,
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
                        SequenceBottomBar()
                    }
                }

            }

        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityTopAppBar(navController: NavController, viewModel: CommunityViewModel) {

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
                            Log.d(
                                ContentValues.TAG,
                                "pushpushpop: ${viewModel.detailEssayBackStack}"
                            )
                            if (viewModel.detailEssayBackStack.isNotEmpty()) {
                                viewModel.detailEssay = viewModel.detailEssayBackStack.peek()
                            }
                        }
                        navController.popBackStack()
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
                        viewModel.isOptionClicked = !viewModel.isOptionClicked
                    },
            )
        })
}

@Composable
fun ReportOption( onClickReport: () -> Unit,viewModel: CommunityViewModel) {
    Surface(modifier = Modifier.size(180.dp, 110.dp), shape = RoundedCornerShape(2)) {
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
                    contentDescription = "minus",
                    modifier = Modifier.clickable { viewModel.textSizeDown() }
                )
                Spacer(modifier = Modifier.width(30.dp))
                Text(text = "가")
                Spacer(modifier = Modifier.width(30.dp))
                Icon(
                    painter = painterResource(id = R.drawable.text_plus),
                    contentDescription = "plus",
                    modifier = Modifier.clickable { viewModel.textSizeUp() }

                )
            }
            HorizontalDivider()
            OptionItem(text = "신고하기", Color.Red, onClick =  {onClickReport()}, R.drawable.option_report)

        }
    }
}//todo bug 스토리를 만들면 가장 최근스토리에 싹 들어간다.


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailEssay(item: EssayApi.EssayItem,viewModel: CommunityViewModel) {
    var isEssayBookMarked by remember { mutableStateOf(false) }

    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            if (item.thumbnail != null) {
                GlideImage(
                    model = item.thumbnail, contentDescription = "", modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }
            Row {
                Text(text = item.title!!, fontSize = viewModel.titleTextSize, modifier = Modifier)
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {

                    val bookMarkViewModel : BookMarkViewModel = hiltViewModel()
                    val iconImg = if (isEssayBookMarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder

                    Icon(
                        imageVector = iconImg,
                        contentDescription = "",
                        Modifier
                            .size(30.dp)
                            .clickable {
                                isEssayBookMarked = !isEssayBookMarked

                                bookMarkViewModel.viewModelScope.launch {
                                    if (isEssayBookMarked) bookMarkViewModel.addBookMark()
                                    else bookMarkViewModel.deleteBookMark()
                                }

                            },
                    )
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = item.content!!,
                fontSize = viewModel.contentTextSize,
                modifier = Modifier,
                color = Color(0xFFB4B4B4)
            )

            Spacer(modifier = Modifier.height(46.dp))
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                Column {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                        (if (item.author !=null) item.author.nickname else "")?.let {
                            Text(
                                text = it,
                                fontSize = 12.sp,
                                textAlign = TextAlign.End,
                                color = Color(0xFF686868)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                        Text(
                            text = item.createdDate ?: "",
                            fontSize = 12.sp,
                            textAlign = TextAlign.End,
                            color = Color(0xFF686868)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (item.linkedOutGauge != null){
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                            Row {
                                repeat(item.linkedOutGauge) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ring),
                                        contentDescription = "ring",
                                        modifier = Modifier.size(14.dp),
                                        colorFilter = ColorFilter.tint(Color(0xFF686868))
                                    )
                                    if (it != item.linkedOutGauge - 1) Spacer(
                                        modifier = Modifier.width(
                                            4.dp
                                        )
                                    )
                                }
                            }
                        }
                    }

                }
            }
            Spacer(modifier = Modifier.height(28.dp))
            if (item.tags != null){
                Row {
                    repeat(item.tags.size){
                        SuggestionChip(
                            onClick = { },
                            label = { Text(item.tags[it].name) },
                            shape = RoundedCornerShape(50)
                        )
                        if (it != item.tags.size-1) Spacer(modifier = Modifier.width(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SequenceBottomBar(){
    Box(modifier = Modifier
        .background(Color.Black)
        .fillMaxWidth()
        .height(70.dp)){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart){

            Icon(imageVector = Icons.Default.BookmarkBorder, contentDescription = "bookMark",
                Modifier
                    .padding(start = 20.dp)
                    .size(35.dp))
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd){
            Row {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowLeft, contentDescription = "bookMark",Modifier.size(50.dp))
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowRight, contentDescription = "bookMark",Modifier.size(50.dp))


            }
        }


    }
}

