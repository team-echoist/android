@file:OptIn(ExperimentalGlideComposeApi::class)

package com.echoist.linkedout.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.presentation.util.TUTORIAL_BULB
import kotlinx.coroutines.launch

@Composable
fun TabletTutorialScreen() {
    val pagerstate = rememberPagerState { 4 }
    val coroutineScope = rememberCoroutineScope()

    HorizontalPager(state = pagerstate, modifier = Modifier.fillMaxSize()) { page ->
        when (page) {
            0 -> Tutorial1Screen()

            1 ->
                Tutorial2Screen(draggedToLeft = {
                    coroutineScope.launch {
                        pagerstate.animateScrollToPage(
                            0,
                            animationSpec = tween(
                                durationMillis = 600,
                                easing = FastOutSlowInEasing
                            )
                        )

                    }
                }, isTutorial3Clicked = {
                    coroutineScope.launch {
                        pagerstate.animateScrollToPage(
                            2,
                            animationSpec = tween(
                                durationMillis = 600,
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
                })

            2 ->
                Tutorial3Screen(draggedToLeft = {
                    coroutineScope.launch {
                        pagerstate.animateScrollToPage(
                            1,
                            animationSpec = tween(
                                durationMillis = 600,
                                easing = FastOutSlowInEasing
                            )
                        )

                    }
                })
            3 ->
                Tutorial4Screen {
                }
        }
    }
    if (pagerstate.currentPage != 3) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 150.dp, end = 20.dp), contentAlignment = Alignment.TopEnd
        ) {
            Text(
                text = "건너뛰기 >>",
                modifier = Modifier.clickable { },
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = Color(0xFF929292),
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                )
            )
        }
    }

    if (pagerstate.currentPage != 3) {
        Box(
            modifier = Modifier
                .fillMaxSize() /* 부모 만큼 */
                .padding(bottom = 20.dp), contentAlignment = Alignment.BottomCenter
        ) {

            Row(
                Modifier
                    .height(100.dp)
                    .padding(bottom = 30.dp), //box 안에 있어야하는거같기도?
                horizontalArrangement = Arrangement.Center
            )
            {
                repeat(4) { iteration ->
                    val color =
                        if (pagerstate.currentPage == iteration) Color(0xFF616FED) else Color.White.copy(
                            alpha = 0.5f
                        )
                    if (pagerstate.currentPage == iteration) {
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(20.dp, 10.dp)

                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(10.dp, 10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Tutorial1Screen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp), contentAlignment = Alignment.Center
    ) {
        GlideImage(
            model = R.drawable.tutorial_bottomicons,
            contentDescription = "tutorial_bottomicons"
        )
    }
}

@Composable
fun Tutorial2Screen(isTutorial3Clicked: () -> Unit, draggedToLeft: () -> Unit) {
    var isClicked by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Box {
        Box(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    coroutineScope.launch {
                        if (dragAmount > 0) {
                            draggedToLeft()
                        } else {
                            isClicked = true
                        }
                    }
                }
            }) {
            GlideImage(
                model = TUTORIAL_BULB,
                contentDescription = "bulb_img",
                modifier = Modifier
                    .padding(start = 300.dp, bottom = 230.dp)
                    .size(100.dp)
                    .align(Alignment.Center)
                    .clickable { isClicked = true }
            )
            GlideImage(
                model = R.drawable.tutorial_2,
                contentDescription = "home_img",
                modifier = Modifier
                    .size(550.dp, 900.dp)
                    .align(Alignment.Center)
                    .padding(start = 100.dp, bottom = 100.dp)
                    .clickable { isClicked = true },
                contentScale = ContentScale.FillBounds
            )
        }

        AnimatedVisibility(
            visible = isClicked,
            enter = slideInVertically(
                initialOffsetY = { 2000 },
                animationSpec = tween(durationMillis = 500)
            ),
            exit = slideOutVertically(
                targetOffsetY = { 2000 },
                animationSpec = tween(durationMillis = 500)
            )
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                GlideImage(
                    model = R.drawable.tutorial_3,
                    contentDescription = "home_img",
                    modifier = Modifier
                        .size(281.dp, 411.dp)
                        .clickable {
                            isClicked = true
                            isTutorial3Clicked()
                        }
                )
            }
        }
    }
}

@Composable
fun Tutorial3Screen(draggedToLeft: () -> Unit) {
    var isClicked by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    if (!isClicked) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        // 감지된 드래그 방향과 양에 따라 동작 수행
                        coroutineScope.launch {
                            if (dragAmount > 0) {
                                // 오른쪽으로 스와이프
                                draggedToLeft()
                            } else {
                                // 왼쪽으로 스와이프
                                isClicked = true
                            }
                        }
                    }
                },
            contentAlignment = Alignment.BottomEnd
        ) {
            TutorialActionBtn { isClicked = true }
        }
    }

    AnimatedVisibility(
        visible = isClicked,
        enter = slideInVertically(
            initialOffsetY = { 2000 },
            animationSpec = tween(durationMillis = 500)
        ),
        exit = slideOutVertically(
            targetOffsetY = { 2000 },
            animationSpec = tween(durationMillis = 500)
        )
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .padding(bottom = 56.dp)
            ) {
                GlideImage(
                    model = R.drawable.tutorial_4,
                    contentDescription = "home_img",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}

@Composable
fun Tutorial4Screen(isCloseClicked: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.fillMaxWidth(0.4f)) {
            GlideImage(
                model = R.drawable.tutorial_5,
                contentDescription = "home_img",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 45.dp, end = 20.dp)
                .clickable { },
            contentAlignment = Alignment.TopEnd
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "close",
                tint = Color.White,
                modifier = Modifier
                    .size(30.dp)
                    .clickable { isCloseClicked() }
            )
        }
    }
}
