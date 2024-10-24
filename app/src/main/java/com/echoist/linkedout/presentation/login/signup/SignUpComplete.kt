package com.echoist.linkedout.presentation.login.signup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.presentation.home.HomeViewModel
import com.echoist.linkedout.presentation.util.Routes
import com.echoist.linkedout.ui.theme.LinkedInColor
import kotlinx.coroutines.delay

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SignUpCompletePage(homeViewModel: HomeViewModel = hiltViewModel(), navController: NavController) {
    var isLoading by remember { androidx.compose.runtime.mutableStateOf(true) }

    LaunchedEffect(key1 = Unit) {
        homeViewModel.requestMyInfo()
        isLoading = true
        delay(3000)
        isLoading = false
        navController.navigate("${Routes.Home}/200") {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }

    val myInfo by homeViewModel.getMyInfo().collectAsState()

    Scaffold {
        Box(
                Modifier
                        .padding(it).fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Column(
                Modifier.padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoadingText(nickName = myInfo.nickname ?: "아무개")
                Spacer(modifier = Modifier.height(56.dp))
                GlideImage(
                    model = R.drawable.login_table,
                    modifier = Modifier.size(246.dp, 266.dp),
                    contentDescription = "login table"
                )
                Spacer(modifier = Modifier.height(87.dp))

            }
            Box(
                modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 20.dp), contentAlignment = Alignment.BottomEnd
            ) {
                GlideImage(
                    model = R.drawable.rightsidetext,
                    contentDescription = "rightsideText",
                        Modifier
                                .padding(bottom = 80.dp)
                                .size(240.dp, 90.dp)
                )
            }
        }
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = LinkedInColor)
            }
        }
    }
}

@Composable
fun LoadingText(nickName: String) {
    var isVisible by remember { androidx.compose.runtime.mutableStateOf(false) }

    // Delay before showing the nickname text
    LaunchedEffect(key1 = Unit) {
        delay(300) // 300ms delay
        isVisible = true
    }

    Column {

        Row {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn()
            ) {
                Text(
                    text = "'$nickName'",
                    fontSize = 20.sp,
                    color = LinkedInColor
                )
            }
            Text(text = "님", fontSize = 20.sp)
        }

        Text(text = "당신만을 위한 글쓰기 공간을 생성중입니다", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(13.dp))
        Text(
            text = "필명은 [마이페이지 > 프로필편집]에서 수정 가능합니다.",
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = LinkedInColor
        )
    }
}

@Preview
@Composable
fun RightSideText() {
    Box(
        modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
    ) {
        Text(
            text = "이곳에서 마음껏\n실험하고,부딪히고,성장하는\n아무개가 되어보세요 ",
            Modifier.fillMaxSize(),
            textAlign = TextAlign.End,
            fontSize = 20.sp
        )
    }
}


