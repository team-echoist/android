package com.echoist.linkedout.components

import android.content.ContentValues
import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echoist.linkedout.R
import com.echoist.linkedout.viewModels.CommunityViewModel
import com.echoist.linkedout.viewModels.SentenceInfo
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import me.saket.extendedspans.ExtendedSpans
import me.saket.extendedspans.RoundedCornerSpanPainter
import me.saket.extendedspans.drawBehind

@Preview
@Composable
fun RandomSentences() {
    val annotatedString = buildAnnotatedString {
        withStyle(style = ParagraphStyle(lineHeight = 40.sp)) { // 원하는 줄 간격 설정
            pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence1")
            withStyle(
                style = SpanStyle(
                    color = Color.White,
                    background = Color.Black,
                    fontSize = 14.sp,
                )
            ) {
                append("이원영은 초파리를 좋아했다.       ")

            }
            pop()

            pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence2")
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    background = Color.White,
                    fontSize = 14.sp,
                )
            ) {
                append("빗소리가 커서 괜찮지 않냐고 물었지만 사실 너무 커서 무서웠다. 무서웠지만? 무서웠어        ")

            }
            pop()

            pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence3")
            withStyle(
                style = SpanStyle(
                    color = Color.White,
                    background = Color.Black,
                    fontSize = 14.sp,
                )
            ) {
                append("살짝 열린 창문 사이로 몇분 전 내가 비탈이 보였다.        ")

            }
            pop()

            pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence4")
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    background = Color.White,
                    fontSize = 14.sp,
                )
            ) {
                append(" 빗소리가 커서 괜찮지 않냐 너무 커서 무서웠다.  ")

            }
            pop()
        }
    }
    val extendedSpans = remember {
        ExtendedSpans(
            RoundedCornerSpanPainter(
                cornerRadius = 20.sp,
                topMargin = 0.sp,
                bottomMargin = 0.sp,
                padding = RoundedCornerSpanPainter.TextPaddingValues(
                    horizontal = 14.sp,
                    vertical = 0.sp
                )
            )
        )
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 6.dp)
            , contentAlignment = Alignment.Center

        ) {
            var layoutResult: TextLayoutResult? = remember { null }

            Text(
                text = remember("text") {
                    extendedSpans.extend(annotatedString)
                },
                modifier = Modifier
                    .drawBehind(extendedSpans)
                    .pointerInput(Unit) {
                        detectTapGestures { offsetPosition ->
                            layoutResult?.let {
                                val position = it.getOffsetForPosition(offsetPosition)
                                annotatedString
                                    .getStringAnnotations(
                                        tag = "SentenceTag",
                                        start = position,
                                        end = position
                                    )
                                    .firstOrNull()
                                    ?.let { annotation ->
                                        when (annotation.item) {
                                            "Sentence1" -> Log.d(
                                                ContentValues.TAG,
                                                "AnnotatedClickableText: ta"
                                            )

                                            "Sentence2" -> Log.d(
                                                "tag",
                                                "AnnotatedClickableText: Sentence 2 clicked"
                                            )

                                            "Sentence3" -> Log.d(
                                                "tag",
                                                "AnnotatedClickableText: Sentence 3 clicked"
                                            )

                                            "Sentence4" -> Log.d(
                                                "tag",
                                                "AnnotatedClickableText: Sentence 4 clicked"
                                            )
                                        }
                                    }
                            }
                        }
                    },
                onTextLayout = {
                    layoutResult = it
                    extendedSpans.onTextLayout(it)
                }
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CommunityTopAppBar(){
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = {
            Text(text = "커뮤니티", fontWeight = FontWeight.Bold, color = Color.Black)
        },
        actions = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "", Modifier.size(30.dp), tint = Color.Black)
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = "",
                Modifier.size(30.dp),
                tint = Color.Black
            )

        }
    )
}


@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
fun CommunityChips(){
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val color = if (pagerState.currentPage == 0) Color.Black else Color.White

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(26.dp)){

        HorizontalDivider(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 27.dp)
            , thickness = 1.dp
            , color = Color(0xFF686868))

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 17.dp)) {
            Column(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(60.dp, 40.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    color = color,
                    fontSize = 14.sp,
                    text = "랜덤",// 색상을 먼저 적용합니다
                    modifier = Modifier.clickable {

                            coroutineScope.launch {
                                pagerState.animateScrollToPage(0)
                            }

                    }
                )

                Spacer(modifier = Modifier.height(4.dp))
                if (pagerState.currentPage == 0){
                    HorizontalDivider(modifier = Modifier
                        .width(65.dp),
                        color = color,
                        thickness = 3.dp)
                }

            }
            Column(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(60.dp, 40.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    color = color,
                    fontSize = 14.sp,
                    text = "구독",// 색상을 먼저 적용합니다
                    modifier = Modifier.clickable {

                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }

                    } // Modifier.clickable을 마지막에 적용합니다
                )

                Spacer(modifier = Modifier.height(4.dp))
                if (pagerState.currentPage == 1){
                    HorizontalDivider(modifier = Modifier
                        .width(65.dp),
                        color = color,
                        thickness = 3.dp)
                }
            }

        }

    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SentenceChoice(viewModel: CommunityViewModel){
    val color = Color.Black

    Box(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .height(82.dp),
        contentAlignment = Alignment.Center
    ){

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart){
            Column {
                Text(text = "한 문장을 모아", fontWeight = FontWeight.SemiBold, color = color)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "글의 시작을 알리는 문장들을 만나보세요.", color = Color(0xFF696969), fontSize = 12.sp)

            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterEnd
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){

                AnimatedContent(
                    targetState = viewModel.sentenceInfo,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) with fadeOut(animationSpec = tween(300))
                    }, label = ""
                ) { targetState ->
                    Text(
                        text = if (targetState == SentenceInfo.First) "첫 문장" else "마지막 문장",
                        color = Color.Black
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.arrowdown),
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        viewModel.isClicked = !viewModel.isClicked
                    }
                    , tint = Color.Black
                )
            }

            }

        }
    }


@Composable
fun ChoiceBox(viewModel: CommunityViewModel){
    val color = Color.Black
    Card(
        shape = RoundedCornerShape(20),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFCFCFCF)),
        modifier = Modifier.size(
            120.dp,
            70.dp)
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "첫 문장",
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clickable {
                        viewModel.isClicked = false
                        viewModel.sentenceInfo = SentenceInfo.First
                    },
                color = color
            )
            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(Modifier.fillMaxWidth(),color = Color(0xFFC5C5C5))
            Spacer(modifier = Modifier.height(6.dp),)

            Text(
                text = "마지막 문장",
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clickable {
                        viewModel.isClicked = false
                        viewModel.sentenceInfo = SentenceInfo.Last

                    },
                color = color
            )

        }
    }
}

@Preview
@Composable
fun TodaysLogTitle(){
    Box(modifier = Modifier
        .background(Color.Black)
        .fillMaxWidth()
        .padding(start = 20.dp, end = 20.dp)
        .height(90.dp)){
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "오늘의 글", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color(0xFF616FED))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "오늘 쓰여진 다양하고 솔직한 글들을 읽어보세요", fontSize = 12.sp, color = Color(0xFF696969))

        }
    }
}





