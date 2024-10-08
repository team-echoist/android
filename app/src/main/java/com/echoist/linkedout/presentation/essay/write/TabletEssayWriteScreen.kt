package com.echoist.linkedout.presentation.essay.write

import android.content.ContentValues
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.presentation.util.Keyboard
import com.echoist.linkedout.presentation.util.keyboardAsState
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import kotlinx.coroutines.delay

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TabletEssayWriteRoute(
    navController: NavController,
    viewModel: WritingViewModel = hiltViewModel()
) {

    val isKeyBoardOpened by keyboardAsState()
    val scrollState = rememberScrollState()
    val background = Color.Black

    var isTextSettingSelected by remember { mutableStateOf(false) }
    var isTextBoldSelected by remember { mutableStateOf(false) }
    var isTextUnderLineSelected by remember { mutableStateOf(false) }
    var isTextMiddleLineSelected by remember { mutableStateOf(false) }


    //리치텍스트
    val textState = rememberRichTextState()
    val currentSpanStyle = textState.currentSpanStyle
    val isBold = currentSpanStyle.fontWeight == FontWeight.Bold
    val isItalic = currentSpanStyle.fontStyle == FontStyle.Italic
    val isUnderline = currentSpanStyle.textDecoration == TextDecoration.Underline

    LaunchedEffect(key1 = Unit) {
        textState.setHtml(viewModel.content)
    }

    LaunchedEffect(key1 = viewModel.isStored) {
        if (viewModel.isStored) {
            delay(2000)
            viewModel.isStored = false
        }
    }

    LinkedOutTheme {
        Box {
            Column(
                modifier = Modifier
                    .background(background)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                )
                {
                    WritingTopAppBar(
                        navController = navController,
                        viewModel,
                        textState
                    ) { viewModel.content = textState.toHtml() }

                    ContentTextField(viewModel = viewModel, textState)
                    Log.d(ContentValues.TAG, "WritingTopAppBar: ${textState.toHtml()}")

                    Spacer(modifier = Modifier.height(50.dp))

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box {
                            GlideImage(
                                model = viewModel.imageUri ?: viewModel.imageUrl,
                                contentDescription = "uri",
                                contentScale = ContentScale.Crop
                            )
                            Log.d(
                                ContentValues.TAG,
                                "WritingPage: ${viewModel.imageUri}, ${viewModel.imageUrl}"
                            )
                            if (viewModel.imageUri != null || (viewModel.imageUrl != null && viewModel.imageUrl!!.startsWith(
                                    "https"
                                ))
                            ) { //image url 주소가 널이 아니고 https값으로 시작해야 제대로된 Url link
                                Row( //변경버튼 클릭 시 화면이동
                                    Modifier
                                        .offset(x = 10.dp, y = 10.dp)
                                        .width(57.dp)
                                        .height(32.dp)
                                        .clickable { navController.navigate("CropImagePage") }
                                        .background(color = Color(0xFF616FED)),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center) {
                                    Text(text = "변경", fontSize = 16.sp, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
            //취소 클릭시 배경 어둡게
            if (viewModel.isCanCelClicked.value)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.7f))
                )
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) { //키보드 자리에 들어갈 컴포 넣기
                Column {
                    AnimatedVisibility(
                        visible = viewModel.isCanCelClicked.value,
                        enter = slideInVertically(
                            initialOffsetY = { 2000 },
                            animationSpec = tween(durationMillis = 500)
                        ),
                        exit = slideOutVertically(
                            targetOffsetY = { 2000 },
                            animationSpec = tween(durationMillis = 500)
                        )
                    ) {
                        WritingCancelCard(
                            viewModel = viewModel,
                            navController = navController
                        ) { viewModel.isStored = true }
                    }
                    //장소 찍는
                    if (viewModel.longitude != null && viewModel.latitude != null && viewModel.isTextFeatOpened.value) {
                        if (viewModel.isLocationClicked) {
                            Row {
                                LocationBox(viewModel = viewModel)
                                Row(
                                    Modifier
                                        .padding(horizontal = 10.dp)
                                        .horizontalScroll(rememberScrollState())
                                ) {
                                    viewModel.locationList.forEach {
                                        LocationBtn(viewModel = viewModel, text = it)
                                    }
                                }
                            }
                        }
                    } else if (isKeyBoardOpened == Keyboard.Closed && !viewModel.isTextFeatOpened.value) {
                        if (viewModel.locationList.isNotEmpty() && viewModel.longitude != null) { //직접 입력한 장소가 존재해야함.
                            LocationGroup(viewModel = viewModel)
                            Spacer(modifier = Modifier.height(4.dp))
                            if (viewModel.hashTagList.isEmpty()) {
                                Spacer(modifier = Modifier.height(80.dp))
                            }
                        }
                    }
                    //해시태그 찍는
                    if (viewModel.hashTagList.isNotEmpty() && viewModel.isTextFeatOpened.value) {
                        if (viewModel.isHashTagClicked) {
                            Row(
                                Modifier
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 20.dp)
                            ) {
                                viewModel.hashTagList.forEach {
                                    HashTagBtn(viewModel = viewModel, text = it)
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                            }
                        }

                    } else if (isKeyBoardOpened == Keyboard.Closed && !viewModel.isTextFeatOpened.value)
                        if (viewModel.hashTagList.isNotEmpty()) {
                            Column {
                                HashTagGroup(viewModel = viewModel)
                                Spacer(modifier = Modifier.height(80.dp))
                            }
                        }

                    if (isKeyBoardOpened == Keyboard.Opened || viewModel.isTextFeatOpened.value) {
                        if (isTextSettingSelected) {
                            TextSettingsBar(textState)
                        }
                        TextEditBar(
                            viewModel,
                            isTextSettingSelected = isTextSettingSelected,
                            onTextSettingSelected = {
                                isTextSettingSelected = it
                            },
                            isTextBoldSelected = isTextBoldSelected,
                            onTextBoldSelected =
                            {
                                isTextBoldSelected = it
                                if (isTextBoldSelected) textState.addSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
                                else textState.removeSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
                                isTextSettingSelected = false
                            },
                            isTextUnderLineSelected = isTextUnderLineSelected,
                            onTextUnderLineSelected =
                            {
                                isTextUnderLineSelected = it
                                if (isTextUnderLineSelected) textState.addSpanStyle(
                                    SpanStyle(
                                        textDecoration = TextDecoration.Underline
                                    )
                                )
                                else textState.removeSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                                isTextSettingSelected = false

                            },
                            isTextMiddleLineSelected = isTextMiddleLineSelected,
                            onTextMiddleLineSelected = {
                                isTextMiddleLineSelected = it
                                if (isTextMiddleLineSelected) textState.addSpanStyle(
                                    SpanStyle(
                                        textDecoration = TextDecoration.LineThrough
                                    )
                                )
                                else textState.removeSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))

                                isTextSettingSelected = false
                            }, textState
                        )
                        KeyboardLocationFunc(viewModel, navController, textState)
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = viewModel.isStored,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 10.dp)
                    .navigationBarsPadding(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(Color(0xFF212121), shape = RoundedCornerShape(20)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "임시 저장이 완료되었습니다.", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}