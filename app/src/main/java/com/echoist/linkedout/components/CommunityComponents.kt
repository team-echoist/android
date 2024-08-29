package com.echoist.linkedout.components

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.TYPE_RECOMMEND
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.formatElapsedTime
import com.echoist.linkedout.viewModels.CommunityViewModel
import com.echoist.linkedout.viewModels.SentenceInfo
import kotlinx.coroutines.launch
import me.saket.extendedspans.ExtendedSpans
import me.saket.extendedspans.RoundedCornerSpanPainter
import me.saket.extendedspans.drawBehind

@Composable
fun RandomSentences(viewModel: CommunityViewModel, navController: NavController) {
    val oneSentenceList by if (viewModel.sentenceInfo == SentenceInfo.First) viewModel.firstSentences.collectAsState() else viewModel.lastSentences.collectAsState()


    //한줄추천 받아온 리스트값이 비어있거나 25사이즈 전이면 안보여줌.
    if (oneSentenceList.isNotEmpty() && oneSentenceList.size > 25) {

        val annotatedString = remember(viewModel.sentenceInfo, oneSentenceList) {
            buildAnnotatedString {
                withStyle(style = ParagraphStyle(lineHeight = 40.sp)) { // 원하는 줄 간격 설정
                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence1")
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            background = Color.Black,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("    ${oneSentenceList[0].content}      ")
                        Log.d(TAG, "RandomSentences: ${oneSentenceList[0].content}")

                    }
                    pop()

                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence2")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF262626),
                            background = Color.White,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("${oneSentenceList[1].content}       ")

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
                        append("${oneSentenceList[2].content}      ")

                    }
                    pop()

                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence4")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF262626),
                            background = Color.White,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("${oneSentenceList[3].content}       ")

                    }
                    pop()

                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence4")
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            background = Color.Black,
                            fontSize = 14.sp,
                        )
                    ) {
                        append(" ${oneSentenceList[4].content} ")

                    }
                    pop()
                }
            }
        }
        val annotatedString2 = remember(viewModel.sentenceInfo, oneSentenceList) {
            buildAnnotatedString {
                withStyle(style = ParagraphStyle(lineHeight = 40.sp)) { // 원하는 줄 간격 설정
                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence1")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF262626),
                            background = Color.White,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("    ${oneSentenceList[5].content}      ")
                        Log.d(TAG, "RandomSentences: ${oneSentenceList[0].content}")

                    }
                    pop()

                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence2")
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            background = Color.Black,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("${oneSentenceList[6].content}       ")

                    }
                    pop()

                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence3")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF262626),
                            background = Color.White,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("${oneSentenceList[7].content}      ")

                    }
                    pop()

                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence4")
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            background = Color.Black,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("${oneSentenceList[8].content}       ")

                    }
                    pop()

                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence4")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF262626),
                            background = Color.White,
                            fontSize = 14.sp,
                        )
                    ) {
                        append(" ${oneSentenceList[9].content} ")

                    }
                    pop()
                }
            }
        }
        Log.d(TAG, "RandomSentences: ${annotatedString.text}")

        val annotatedString3 = remember(viewModel.sentenceInfo, oneSentenceList) {
            buildAnnotatedString {
                withStyle(style = ParagraphStyle(lineHeight = 40.sp)) { // 원하는 줄 간격 설정
                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence1")
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            background = Color.Black,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("    ${oneSentenceList[10].content}      ")
                        Log.d(TAG, "RandomSentences: ${oneSentenceList[0].content}")

                    }
                    pop()

                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence2")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF262626),
                            background = Color.White,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("${oneSentenceList[11].content}       ")

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
                        append("${oneSentenceList[12].content}      ")

                    }
                    pop()

                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence4")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF262626),
                            background = Color.White,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("${oneSentenceList[13].content}       ")

                    }
                    pop()

                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence4")
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            background = Color.Black,
                            fontSize = 14.sp,
                        )
                    ) {
                        append(" ${oneSentenceList[14].content} ")

                    }
                    pop()
                }
            }
        }
        val annotatedString4 = remember(viewModel.sentenceInfo, oneSentenceList) {
            buildAnnotatedString {
                withStyle(style = ParagraphStyle(lineHeight = 40.sp)) { // 원하는 줄 간격 설정
                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence1")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF262626),
                            background = Color.White,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("    ${oneSentenceList[15].content}      ")
                        Log.d(TAG, "RandomSentences: ${oneSentenceList[0].content}")

                    }
                    pop()

                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence2")
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            background = Color.Black,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("${oneSentenceList[16].content}       ")

                    }
                    pop()

                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence3")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF262626),
                            background = Color.White,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("${oneSentenceList[17].content}      ")

                    }
                    pop()

                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence4")
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            background = Color.Black,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("${oneSentenceList[18].content}       ")

                    }
                    pop()

                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence4")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF262626),
                            background = Color.White,
                            fontSize = 14.sp,
                        )
                    ) {
                        append(" ${oneSentenceList[19].content} ")

                    }
                    pop()
                }
            }
        }
        val annotatedString5 = remember(viewModel.sentenceInfo, oneSentenceList) {
            buildAnnotatedString {
                withStyle(style = ParagraphStyle(lineHeight = 40.sp)) { // 원하는 줄 간격 설정
                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence1")
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            background = Color.Black,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("    ${oneSentenceList[20].content}      ")
                        Log.d(TAG, "RandomSentences: ${oneSentenceList[0].content}")

                    }
                    pop()

                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence2")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF262626),
                            background = Color.White,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("${oneSentenceList[21].content}       ")

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
                        append("${oneSentenceList[22].content}      ")

                    }
                    pop()

                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence4")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF262626),
                            background = Color.White,
                            fontSize = 14.sp,
                        )
                    ) {
                        append("${oneSentenceList[23].content}       ")

                    }
                    pop()

                    pushStringAnnotation(tag = "SentenceTag", annotation = "Sentence4")
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            background = Color.Black,
                            fontSize = 14.sp,
                        )
                    ) {
                        append(" ${oneSentenceList[24].content} ")

                    }
                    pop()
                }
            }
        }


        val extendedSpans = remember {
            ExtendedSpans(
                RoundedCornerSpanPainter(
                    cornerRadius = 20.sp,
                    topMargin = -2.sp,
                    bottomMargin = -2.sp,
                    padding = RoundedCornerSpanPainter.TextPaddingValues(
                        horizontal = 14.sp,
                        vertical = 0.sp
                    )
                )
            )
        }
        val extendedSpans2 = remember {
            ExtendedSpans(
                RoundedCornerSpanPainter(
                    cornerRadius = 20.sp,
                    topMargin = -2.sp,
                    bottomMargin = -2.sp,
                    padding = RoundedCornerSpanPainter.TextPaddingValues(
                        horizontal = 14.sp,
                        vertical = 0.sp
                    )
                )
            )
        }
        val extendedSpans3 = remember {
            ExtendedSpans(
                RoundedCornerSpanPainter(
                    cornerRadius = 20.sp,
                    topMargin = -2.sp,
                    bottomMargin = -2.sp,
                    padding = RoundedCornerSpanPainter.TextPaddingValues(
                        horizontal = 14.sp,
                        vertical = 0.sp
                    )
                )
            )
        }

        val extendedSpans4 = remember {
            ExtendedSpans(
                RoundedCornerSpanPainter(
                    cornerRadius = 20.sp,
                    topMargin = -2.sp,
                    bottomMargin = -2.sp,
                    padding = RoundedCornerSpanPainter.TextPaddingValues(
                        horizontal = 14.sp,
                        vertical = 0.sp
                    )
                )
            )
        }
        val extendedSpans5 = remember {
            ExtendedSpans(
                RoundedCornerSpanPainter(
                    cornerRadius = 20.sp,
                    topMargin = -2.sp,
                    bottomMargin = -2.sp,
                    padding = RoundedCornerSpanPainter.TextPaddingValues(
                        horizontal = 14.sp,
                        vertical = 0.sp
                    )
                )
            )
        }

        Column {

            var layoutResult: TextLayoutResult? = remember { null }
            Text(
                text = remember(annotatedString) {
                    extendedSpans.extend(annotatedString)
                }, fontSize = 14.sp,
                modifier = Modifier
                    .horizontalScroll(state = rememberScrollState())
                    // .fillMaxWidth()
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
                                            "Sentence1" -> viewModel.readDetailEssay(
                                                oneSentenceList[0].id!!,
                                                navController = navController
                                            )

                                            "Sentence2" -> viewModel.readDetailEssay(
                                                oneSentenceList[1].id!!,
                                                navController = navController
                                            )

                                            "Sentence3" -> viewModel.readDetailEssay(
                                                oneSentenceList[2].id!!,
                                                navController = navController
                                            )

                                            "Sentence4" -> viewModel.readDetailEssay(
                                                oneSentenceList[3].id!!,
                                                navController = navController
                                            )

                                            "Sentence5" -> viewModel.readDetailEssay(
                                                oneSentenceList[4].id!!,
                                                navController = navController
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

            var layoutResult2: TextLayoutResult? = remember { null }
            Text(
                text = remember(annotatedString2) {
                    extendedSpans.extend(annotatedString2)
                }, fontSize = 14.sp,
                modifier = Modifier
                    .offset(y = 4.dp)
                    .horizontalScroll(state = rememberScrollState())
                    // .fillMaxWidth()
                    .drawBehind(extendedSpans2)
                    .pointerInput(Unit) {
                        detectTapGestures { offsetPosition ->
                            layoutResult2?.let {
                                val position = it.getOffsetForPosition(offsetPosition)
                                annotatedString2
                                    .getStringAnnotations(
                                        tag = "SentenceTag",
                                        start = position,
                                        end = position
                                    )
                                    .firstOrNull()
                                    ?.let { annotation ->
                                        when (annotation.item) {
                                            "Sentence1" -> viewModel.readDetailEssay(
                                                oneSentenceList[5].id!!,
                                                navController = navController
                                            )

                                            "Sentence2" -> viewModel.readDetailEssay(
                                                oneSentenceList[6].id!!,
                                                navController = navController
                                            )

                                            "Sentence3" -> viewModel.readDetailEssay(
                                                oneSentenceList[7].id!!,
                                                navController = navController
                                            )

                                            "Sentence4" -> viewModel.readDetailEssay(
                                                oneSentenceList[8].id!!,
                                                navController = navController
                                            )

                                            "Sentence5" -> viewModel.readDetailEssay(
                                                oneSentenceList[9].id!!,
                                                navController = navController
                                            )
                                        }
                                    }
                            }
                        }
                    },
                onTextLayout = {
                    layoutResult2 = it
                    extendedSpans2.onTextLayout(it)
                }
            )
            var layoutResult3: TextLayoutResult? = remember { null }
            Text(
                text = remember(annotatedString3) {
                    extendedSpans3.extend(annotatedString3)
                }, fontSize = 14.sp,
                modifier = Modifier
                    .offset(y = 8.dp)
                    .horizontalScroll(state = rememberScrollState())
                    // .fillMaxWidth()
                    .drawBehind(extendedSpans3)
                    .pointerInput(Unit) {
                        detectTapGestures { offsetPosition ->
                            layoutResult3?.let {
                                val position = it.getOffsetForPosition(offsetPosition)
                                annotatedString3
                                    .getStringAnnotations(
                                        tag = "SentenceTag",
                                        start = position,
                                        end = position
                                    )
                                    .firstOrNull()
                                    ?.let { annotation ->
                                        when (annotation.item) {
                                            "Sentence1" -> viewModel.readDetailEssay(
                                                oneSentenceList[10].id!!,
                                                navController = navController
                                            )

                                            "Sentence2" -> viewModel.readDetailEssay(
                                                oneSentenceList[11].id!!,
                                                navController = navController
                                            )

                                            "Sentence3" -> viewModel.readDetailEssay(
                                                oneSentenceList[12].id!!,
                                                navController = navController
                                            )

                                            "Sentence4" -> viewModel.readDetailEssay(
                                                oneSentenceList[13].id!!,
                                                navController = navController
                                            )

                                            "Sentence5" -> viewModel.readDetailEssay(
                                                oneSentenceList[14].id!!,
                                                navController = navController
                                            )
                                        }
                                    }
                            }
                        }
                    },
                onTextLayout = {
                    layoutResult3 = it
                    extendedSpans3.onTextLayout(it)
                }
            )

            var layoutResult4: TextLayoutResult? = remember { null }
            Text(
                text = remember(annotatedString4) {
                    extendedSpans4.extend(annotatedString4)
                }, fontSize = 14.sp,
                modifier = Modifier
                    .offset(y = 12.dp)

                    .horizontalScroll(state = rememberScrollState())
                    // .fillMaxWidth()
                    .drawBehind(extendedSpans4)
                    .pointerInput(Unit) {
                        detectTapGestures { offsetPosition ->
                            layoutResult4?.let {
                                val position = it.getOffsetForPosition(offsetPosition)
                                annotatedString4
                                    .getStringAnnotations(
                                        tag = "SentenceTag",
                                        start = position,
                                        end = position
                                    )
                                    .firstOrNull()
                                    ?.let { annotation ->
                                        when (annotation.item) {
                                            "Sentence1" -> viewModel.readDetailEssay(
                                                oneSentenceList[15].id!!,
                                                navController = navController
                                            )

                                            "Sentence2" -> viewModel.readDetailEssay(
                                                oneSentenceList[16].id!!,
                                                navController = navController
                                            )

                                            "Sentence3" -> viewModel.readDetailEssay(
                                                oneSentenceList[17].id!!,
                                                navController = navController
                                            )

                                            "Sentence4" -> viewModel.readDetailEssay(
                                                oneSentenceList[18].id!!,
                                                navController = navController
                                            )

                                            "Sentence5" -> viewModel.readDetailEssay(
                                                oneSentenceList[19].id!!,
                                                navController = navController
                                            )
                                        }
                                    }
                            }
                        }
                    },
                onTextLayout = {
                    layoutResult4 = it
                    extendedSpans4.onTextLayout(it)
                }

            )

            var layoutResult5: TextLayoutResult? = remember { null }
            Text(
                text = remember(annotatedString5) {
                    extendedSpans5.extend(annotatedString5)
                }, fontSize = 14.sp,
                modifier = Modifier
                    .offset(y = 16.dp)

                    .horizontalScroll(state = rememberScrollState())
                    // .fillMaxWidth()
                    .drawBehind(extendedSpans5)
                    .pointerInput(Unit) {
                        detectTapGestures { offsetPosition ->
                            layoutResult5?.let {
                                val position = it.getOffsetForPosition(offsetPosition)
                                annotatedString5
                                    .getStringAnnotations(
                                        tag = "SentenceTag",
                                        start = position,
                                        end = position
                                    )
                                    .firstOrNull()
                                    ?.let { annotation ->
                                        when (annotation.item) {
                                            "Sentence1" -> viewModel.readDetailEssay(
                                                oneSentenceList[20].id!!,
                                                navController = navController
                                            )

                                            "Sentence2" -> viewModel.readDetailEssay(
                                                oneSentenceList[21].id!!,
                                                navController = navController
                                            )

                                            "Sentence3" -> viewModel.readDetailEssay(
                                                oneSentenceList[22].id!!,
                                                navController = navController
                                            )

                                            "Sentence4" -> viewModel.readDetailEssay(
                                                oneSentenceList[23].id!!,
                                                navController = navController
                                            )

                                            "Sentence5" -> viewModel.readDetailEssay(
                                                oneSentenceList[24].id!!,
                                                navController = navController
                                            )
                                        }
                                    }
                            }
                        }
                    },
                onTextLayout = {
                    layoutResult5 = it
                    extendedSpans5.onTextLayout(it)
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityTopAppBar(
    text: String,
    pagerState: PagerState,
    onSearchClick: () -> Unit,
    onClickBookMarked: () -> Unit
) {
    val color = if (pagerState.currentPage == 0) Color.Black else Color.White


    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = {
            Text(text = text, fontWeight = FontWeight.Bold, color = color)
        },
        actions = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                Modifier
                    .padding(start = 10.dp)
                    .size(30.dp)
                    .clickable { onSearchClick() },
                tint = color
            )
            Spacer(modifier = Modifier.width(13.dp))
            Icon(
                painter = painterResource(id = R.drawable.icon_bookmarkfill_black),
                contentDescription = "icon_bookmarkfill_black",
                Modifier
                    .padding(end = 10.dp)
                    .size(30.dp)
                    .clickable { onClickBookMarked() },
                tint = color
            )


        }
    )
}


@Composable
fun CommunityChips(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    val color = if (pagerState.currentPage == 0) Color.Black else Color.White

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
    ) {

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp), thickness = 1.dp, color = Color(0xFFAEAEAE)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 17.dp)
        ) {
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
                if (pagerState.currentPage == 0) {
                    HorizontalDivider(
                        modifier = Modifier
                            .width(65.dp),
                        color = color,
                        thickness = 2.dp
                    )
                }

            }
            //todo 구독페이지 다음버전 출시예정 pagerstate 2로 수정하고 주석해제.
//            Column(
//                modifier = Modifier
//                    .padding(end = 12.dp)
//                    .size(60.dp, 40.dp),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    color = color,
//                    fontSize = 14.sp,
//                    text = "구독",// 색상을 먼저 적용합니다
//                    modifier = Modifier.clickable {
//
//                        coroutineScope.launch {
//                            pagerState.animateScrollToPage(1)
//                        }
//
//                    } // Modifier.clickable을 마지막에 적용합니다
//                )
//
//                Spacer(modifier = Modifier.height(4.dp))
//                if (pagerState.currentPage == 1) {
//                    HorizontalDivider(
//                        modifier = Modifier
//                            .width(65.dp),
//                        color = color,
//                        thickness = 3.dp
//                    )
//                }
//            }

        }

    }
}

@Preview
@Composable
fun TodaysLogTitle() {
    Box(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(95.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(26.dp))
            Text(
                text = "오늘의 글",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color(0xFF616FED)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "오늘 쓰여진 다양하고 솔직한 글들을 읽어보세요", fontSize = 12.sp, color = Color(0xFF696969))
            Spacer(modifier = Modifier.height(22.dp))
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EssayListItem(
    item: EssayApi.EssayItem,
    viewModel: CommunityViewModel,
    navController: NavController
) {
    val color = Color.White

    val annotatedTitleString = buildAnnotatedString {
        // 첫 번째 텍스트 (title)
        append(item.title ?: "")
        addStyle(
            style = SpanStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = color
            ),
            start = 0,
            end = item.title?.length ?: 0
        )

        val bulletText = "   • ${formatElapsedTime(item.createdDate!!)}"
        append(bulletText)
        addStyle(
            style = SpanStyle(
                fontSize = 10.sp,
                color = Color(0xFF686868)
            ),
            start = item.title?.length ?: 0,
            end = item.title?.length?.plus(bulletText.length) ?: bulletText.length
        )
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Color.Black)
        .clickable {
            Log.d(TAG, "EssayListItem: ${item.id}")
            viewModel.readDetailEssay(item.id!!, navController, TYPE_RECOMMEND)
            viewModel.detailEssayBackStack.push(item)
            //navigate
        }
        .height(140.dp)) {
        //타이틀
        Row {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(7f)
                    .padding(top = 0.dp, start = 20.dp, end = 20.dp)// Column 비율 조정
            ) {
                Row(
                    Modifier.padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (item.status == "linkedout") {
                        Icon(
                            painter = painterResource(id = R.drawable.option_linkedout),
                            contentDescription = "linkedout icon",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))

                    }
                    Text(
                        text = annotatedTitleString,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                }


                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = item.content!!,
                    maxLines = 2,
                    fontSize = 14.sp,
                    color = color,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (item.thumbnail != null && item.thumbnail!!.startsWith("https")) { //널이 아니거나 Https로 시작해야됨
                GlideImage(
                    model = item.thumbnail,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(135.dp)
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                        //.weight(3f) // 이미지 비율 조정
                        .clip(RoundedCornerShape(10.dp)) // 둥근 모서리 적용
                )
            }
        }


        Box(
            contentAlignment = Alignment.BottomStart, modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, bottom = 10.dp)
        ) {
            if (item.author != null) { //author이 널이거나
                Text(
                    text = item.author!!.nickname ?: "알 수 없는 아무개",
                    fontSize = 10.sp,
                    color = Color(0xFF686868)
                )
            } else {
                Text(
                    text = "알 수 없는 아무개",
                    fontSize = 10.sp,
                    color = Color(0xFF686868)
                )
            }

        }
        Box(
            contentAlignment = Alignment.BottomEnd, modifier = Modifier
                .fillMaxSize()
        ) {
            HorizontalDivider(color = Color(0xFF333333))
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {

        }


    }
}

@Composable
fun RandomCommunityPage(viewModel: CommunityViewModel, navController: NavController) {

    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        // 스크롤 상태를 감지하는 LaunchedEffect
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull() }
            .collect { lastVisibleItem ->
                // 리스트의 마지막 아이템에 도달하면
                if (lastVisibleItem?.index == viewModel.randomEssayList.size - 1) {
                    viewModel.readRandomEssays()
                }
            }
    }
    LazyColumn(
        state = listState,
        modifier = Modifier
            .background(Color(0xFFD9D9D9))
    ) {
        item {
            Column {
                Box {
                    Column(modifier = Modifier.padding(horizontal = 20.dp))
                    {
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = "한 문장을 모아",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "글의 시작을 알리는 문장들을 만나보세요.",
                            color = Color(0xFF696969),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(23.dp))
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 24.dp), contentAlignment = Alignment.TopEnd
                    ) {
                        SentenceChoiceBox(viewModel)
                    }

                }
                Spacer(modifier = Modifier.height(10.dp))
                RandomSentences(viewModel, navController)
                Spacer(modifier = Modifier.height(40.dp))

                TodaysLogTitle()

            }

        }
        items(viewModel.randomEssayList) { it ->
            EssayListItem(item = it, viewModel = viewModel, navController = navController)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SubscribeUserItem(item: UserInfo, viewModel: CommunityViewModel) {
    val background by animateColorAsState(
        if (viewModel.currentClickedUserId == item.id) Color(
            0xFF222222
        ) else Color.Transparent, label = ""
    )
    val backgroundTrans by animateColorAsState(
        if (viewModel.currentClickedUserId == item.id) Color.White else Color.White.copy(alpha = 0.4f),
        label = ""  //todo white값을 unspecified.0.4f로 맞추면될듯
    )

    Box(
        modifier = Modifier
            .width(70.dp)
            .background(background, shape = RoundedCornerShape(25))
            .padding(top = 20.dp)
            .clickable {
                if (viewModel.currentClickedUserId == item.id) {
                    viewModel.currentClickedUserId = null
                } else {
                    viewModel.currentClickedUserId = item.id
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier.size(50.dp),
                color = backgroundTrans // Unspecified
            ) {
                GlideImage(
                    model = item.profileImage,
                    contentDescription = null,
                    modifier = Modifier
                )
            }
            Text(text = item.nickname!!, fontSize = 12.sp, color = backgroundTrans)
        }
    }
}


@Composable
fun SubscribeUserList(viewModel: CommunityViewModel, navController: NavController) {
    Row(
        modifier = Modifier.background(Color.Black),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        LazyRow(Modifier.weight(9f)) { // todo subscribeList 받아와야할것
            items(viewModel.subscribeUserList) { it ->
                SubscribeUserItem(item = it, viewModel = viewModel)
            }
        }
        Text(text = "전체", modifier = Modifier
            .weight(1f)
            .clickable {
                navController.navigate("FullSubscriberPage")
            })
    }


}

@Preview
@Composable
fun prev() {
    val viewModel: CommunityViewModel = viewModel()
    Column {
        SubscribeUserItem(item = viewModel.userItem, viewModel = viewModel)
        EssayListItem(
            item = viewModel.detailEssay,
            viewModel = viewModel,
            navController = rememberNavController()
        )
        SubscribeUserList(viewModel = viewModel, navController = rememberNavController())
    }
}

@Composable
fun SubscribePage(
    viewModel: CommunityViewModel,
    navController: NavController,
    pagerstate: PagerState
) {
    val color = if (pagerstate.currentPage == 0) Color(0xFFD9D9D9) else Color.Black

    LazyColumn(
        Modifier
            .background(color)
            .padding(top = 40.dp)
    ) {
        item {
            Column {
                SubscribeUserList(viewModel, navController)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 15.dp, end = 20.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    androidx.compose.animation.AnimatedVisibility(visible = viewModel.currentClickedUserId != null) {
                        Box(modifier = Modifier
                            .clickable {
                                viewModel.findUser()
                                navController.navigate("SubscriberPage")
                            }
                            .background(Color(0xFF191919), shape = RoundedCornerShape(20))) {
                            Text(
                                text = "프로필 보기",
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 6.dp)
                            )
                        }
                    }
                }

            }

        }
        items(viewModel.followingList) { it ->
            EssayListItem(item = it, viewModel = viewModel, navController = navController)
        }


    }
}

@Composable
fun CommunityPager(
    pagerState: PagerState,
    viewModel: CommunityViewModel,
    navController: NavController
) {

    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> RandomCommunityPage(viewModel, navController)
            1 -> SubscribePage(viewModel, navController, pagerState)
        }
    }
}

@Composable
fun SentenceChoiceBox(viewModel: CommunityViewModel) {

    val first = "첫 문장"
    val last = "마지막 문장"
    var isClicked by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("첫 문장") }

    val arrow = if (isClicked) R.drawable.arrowup else R.drawable.arrowdown
    val animatedHeight by animateDpAsState(
        targetValue = if (isClicked) 80.dp else 40.dp,
        label = ""
    )

    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .size(120.dp, animatedHeight)
            .background(Color(0xFFCFCFCF), shape = RoundedCornerShape(10)),
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                modifier = Modifier
                    .weight(4f)
                    .padding(start = 10.dp)
                    .clickable {
                        if (isClicked) isClicked = false
                    },
                fontSize = 13.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                painter = painterResource(arrow),
                tint = Color.Black,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(24.dp)
                    .clickable { isClicked = !isClicked },
                contentDescription = "arrow"
            )

        }

        if (isClicked) {
            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = Color(0xFFC5C5C5))
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (text == first) last else first,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clickable {
                        if (text == last) {
                            text = first
                            viewModel.sentenceInfo = SentenceInfo.First
                        } else {
                            text = last
                            viewModel.sentenceInfo = SentenceInfo.Last
                        }
                        isClicked = false

                    },
                fontSize = 13.sp,
                color = Color.Black
            )
        }
    }
}




