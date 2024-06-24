package com.echoist.linkedout.page.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.echoist.linkedout.R
import com.echoist.linkedout.data.BadgeBoxItemWithTag
import com.echoist.linkedout.page.home.MyBottomNavigation
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.SettingsViewModel

@Composable
fun BadgePage(navController: NavController, viewModel: SettingsViewModel) {
    val hasCalledApi = remember { mutableStateOf(false) }

    val badgeBoxItems = viewModel.getDetailBadgeList()

    if (!hasCalledApi.value) {

        hasCalledApi.value = true
    }
    LinkedOutTheme {
        Scaffold(
            topBar = {
                Column(Modifier.padding(horizontal = 20.dp)) {

                    BadgeTopAppBar(navController)
                }
            },
            bottomBar = { MyBottomNavigation(navController) },
            content = {
                Column(
                    Modifier
                        .padding(it)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                ) {
                    badgeBoxItems.forEach { it ->
                        BadgeItem(it,viewModel)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadgeTopAppBar(navController: NavController) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = {
            Text(text = "링크드아웃 뱃지", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        },
        navigationIcon = {
            Icon(
                modifier = Modifier.clickable { navController.popBackStack() },
                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = "back"
            )
        },

        )
}

@Composable
fun BadgeItem(badgeBoxItem: BadgeBoxItemWithTag, viewModel: SettingsViewModel) {
    val badgeTagList = badgeBoxItem.tags
    var isClicked by remember { mutableStateOf(false) }
    val arrowImage = if (isClicked) R.drawable.arrowup else R.drawable.arrowdown

    val text = remember {
        AnnotatedString.Builder().apply {
            append("해시태그")
            withStyle(
                style = SpanStyle(
                    color = Color(0xFF616FED),
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(" 10개  ")
            }
            append("사용하기")
        }.toAnnotatedString()
    }


    //레벨업 성공 시 레벨업 성공 표시
    BadgeLevelUpSuccess(viewModel,badgeBoxItem)

    Column {
        Text(text = badgeBoxItem.badgeName)
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            modifier = Modifier
                .animateContentSize()
                .fillMaxWidth()
                .background(Color(0xFf0D0D0D), shape = RoundedCornerShape(10))
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)){
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //경험치가 10 이상이면 보상받기 open / 클릭 시 레벨업 요청

                    BadgeImg(badgeBoxItem)
                    Spacer(modifier = Modifier.width(27.dp))
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.height(110.dp)
                    ) {
                        Text(text = "${badgeBoxItem.badgeEmotion} 감정 표현", fontSize = 14.sp)
                        Text(text = text, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        ExpProgressBar(progress = badgeBoxItem.exp.toFloat(), max = 10)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize(), contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            painter = painterResource(id = arrowImage),
                            tint = Color.White,
                            contentDescription = "",
                            modifier = Modifier.clickable { isClicked = !isClicked })
                    }
                }
                //경험치가 10 이상이면 보상받기 box 만들기.
                if (badgeBoxItem.exp >= 10){
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.7f)), contentAlignment = Alignment.Center){
                        Button(onClick = { viewModel.requestBadgeLevelUp(badgeBoxItem.badgeId!!) }) {
                            Text(text = "보상 받기")
                        } 
                    }
                }
            }

            if (isClicked) {
                HorizontalDivider(Modifier.fillMaxWidth())
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(15.dp)
                ) {

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(badgeTagList) {
                            Text(
                                text = it,
                                color = Color(0xFF536DFE), // Text color similar to the one in the image
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .border(
                                        1.dp,
                                        Color(0xFF536DFE),
                                        RoundedCornerShape(50)
                                    )
                                    .padding(horizontal = 5.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BadgeImg(badgeBoxItem: BadgeBoxItemWithTag){

    val baseModifier = Modifier
        .size(60.dp)
        .background(
            Color(0xFF0D0D0D),
            shape = RoundedCornerShape(10)
        )
    // 0레벨이면 블러처리, 아니면 블러처리 x
    val finalModifier = if (badgeBoxItem.level == 0) {
        baseModifier.blur(20.dp)
    } else {
        baseModifier
    }
    // 레벨이 0이면 흑백, 1부터는 컬러로
    val colorMatrix = ColorMatrix().apply {
        if (badgeBoxItem.level == 0) setToSaturation(0f)  
        else setToSaturation(1f)
    }
    Image(
        painter = painterResource(id = badgeBoxItem.badgeResourceId),
        contentDescription = "badgeImage",
        modifier = finalModifier,
        colorFilter = ColorFilter.colorMatrix(colorMatrix)
    )
}

//레벨업 보상획득 시 페이지
@Composable
fun BadgeLevelUpSuccess(viewModel: SettingsViewModel,badgeBoxItem: BadgeBoxItemWithTag) {

    AnimatedVisibility(
        viewModel.isLevelUpSuccess,
        enter = fadeIn(animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)),
        exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.7f)), contentAlignment = Alignment.Center){
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = badgeBoxItem.badgeResourceId), contentDescription = "imageId")
                Text(text = badgeBoxItem.badgeName)
                Text(text = "뱃지 획득!")
            }
        }
    }
}

//경험치 수치 바
@Composable
fun ExpProgressBar(progress: Float, max: Int) {
    val progressBarWidth = 170.dp // 프로그레스 바의 너비
    val progressBarHeight = 20.dp // 프로그레스 바의 높이

    Box(
        modifier = Modifier
            .width(progressBarWidth)
            .height(progressBarHeight)
            .background(Color(0xFF2C3054), RoundedCornerShape(50)), // 백그라운드 색상
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(progressBarWidth * (progress / max))
                .background(Color(0xFF536DFE), RoundedCornerShape(50)) // 프로그레스 바 색상
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${progress.toInt()}/${max}",
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 10.sp
            )
        }
    }
}
