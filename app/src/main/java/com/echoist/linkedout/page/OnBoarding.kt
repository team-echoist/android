package com.echoist.linkedout.page

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.echoist.linkedout.R
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import kotlinx.coroutines.delay

@Preview
@Composable
fun PrevOnBoardingPage() {
    OnBoardingPage(rememberNavController())
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingPage(navController: NavController) {
    var isChanged by remember { mutableStateOf(true) }


    LaunchedEffect(key1 = true) {
        if (isChanged) {
            delay(3800)
            isChanged = false
            Log.d(TAG, "OnBoardingPage: fucking")
        }
    }

    LinkedOutTheme {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            val pagerstate = rememberPagerState { 4 }

            HorizontalPager(state = pagerstate) { page ->
                when (page) {
                    0 -> if (!isChanged) {
                        OnBoardingPager(
                            mainText = "오직 나만을 위한 글쓰기",
                            subText = "모든 글쓰기 활동은 '필명'으로 진행됩니다.\n여러 관계에서 벗어나\n솔직한 나를 표현해보세요.",
                            R.raw.onboarding1,
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            VideoPlayer(resId = R.raw.onboarding5)

                        }
                    }

                    1 ->
                        OnBoardingPager(
                        mainText = "글의 행방 결정하기",
                        subText = "작성한 글은 3가지 방향으로 보낼 수 있어요.\n원하는 방법으로 감정을 해소하세요.",
                        R.raw.onboarding2
                    )

                    2 ->
                        OnBoardingPager(
                        mainText = "에세이 엮기",
                        subText = "써두었던 글을 모아\n나만의 에세이 모음집을 만들 수 있어요.",
                        R.raw.onboarding3
                    )

                    3 ->
                        OnBoardingPager(
                            mainText = "다양한 감정 마주하기",
                            subText = "타인의 솔직한 글을 읽는 경험을 할 수 있어요\n문장 속에 담긴 다양한 감정을 마주해보세요.",
                            resId = R.raw.onboarding4
                        )


                }


            }
            if (!isChanged) {
                Box(
                    modifier = Modifier
                        .fillMaxSize() /* 부모 만큼 */
                        .padding(bottom = 20.dp), contentAlignment = Alignment.BottomCenter
                ) {

                    Row(
                        Modifier
                            .height(100.dp)
                            .padding(bottom = 10.dp), //box 안에 있어야하는거같기도?
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
                    if (pagerstate.currentPage == 3) {
                        Button(modifier = Modifier
                            .padding(top = 33.dp)
                            .size(
                                200.dp,
                                50.dp
                            ),
                            onClick = {
                                navController.navigate("LoginPage")


                            }) {
                            Text(text = "시작하기", color = Color.Black)

                        }
                    }
                }


            }
        }
    }


}

@Composable
fun OnBoardingPager(
    mainText: String,
    subText: String,
    resId: Int
) {
    Box(modifier = Modifier.fillMaxSize()){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            VideoPlayer(resId)
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(282.dp))
            // 비디오 플레이어가 준비되었을 때만 VideoPlayer를 그립니다.


            Text(text = mainText, color = Color.White, textAlign = TextAlign.Center, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = subText, color = Color(0xFF686868), textAlign = TextAlign.Center)
        }
    }

}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(resId: Int) {
    val context = LocalContext.current

    val eventListener = remember {
        object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {
                    Player.STATE_IDLE -> {
                        // ExoPlayer가 아무것도 하지 않는 상태
                    }

                    Player.STATE_BUFFERING -> {
                        Log.d(TAG, "onPlaybackStateChanged: 버퍼링입니다")
                        // 미디어 버퍼링 중
                    }

                    Player.STATE_READY -> {

                        // 미디어 재생이 준비된 상태
                    }

                    Player.STATE_ENDED -> {
                        // 미디어 재생이 완료된 상태
                    }
                }
            }
        }
    }
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }
    exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ALL
    exoPlayer.addListener(eventListener)

    val uri = Uri.parse("android.resource://${context.packageName}/$resId")
    val mediaItem = MediaItem.fromUri(uri)
    exoPlayer.setMediaItem(mediaItem)
    exoPlayer.prepare()
    exoPlayer.playWhenReady = true


    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp) // 원하는 높이 설정


    )

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

}






