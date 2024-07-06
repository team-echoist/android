package com.echoist.linkedout.page.settings

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.INSPECT_POPUP_URL
import com.echoist.linkedout.LINKEDOUT_POPUP_URL
import com.echoist.linkedout.PRIVATE_POPUP_URL
import com.echoist.linkedout.PUBLISHED_POPUP_URL
import com.echoist.linkedout.R
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.data.BadgeBoxItem
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.page.home.MyBottomNavigation
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.CommunityViewModel
import com.echoist.linkedout.viewModels.SettingsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPage(
    navController: NavController
) {


    val viewModel : SettingsViewModel = hiltViewModel()


    viewModel.readSimpleBadgeList()
    viewModel.getMyInfo()
    viewModel.readRecentEssays()

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()



    val bottomSheetState =
        rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)
    val scaffoldState = androidx.compose.material3.rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    LinkedOutTheme {
        BottomSheetScaffold(
            sheetContainerColor = Color(0xFF111111),
            scaffoldState = scaffoldState,
            sheetContent = {

                //이미지 수정시
                AnimatedVisibility(
                    visible = viewModel.isClickedModifyImage,
                    enter = fadeIn(animationSpec = tween(durationMillis = 500, easing = LinearEasing)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
                ){
                    Log.d(TAG, "MyPage: ${viewModel.newProfile}")
                    SelectProfileIconBottomSheet(viewModel)
                }
                //기본

                AnimatedVisibility(
                    visible = !viewModel.isClickedModifyImage,
                    enter = fadeIn(animationSpec = tween(durationMillis = 500, easing = LinearEasing)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
                ){
                    ModifyMyProfileBottomSheet(
                        onClickComplete = {
                            viewModel.updateMyInfo(viewModel.newProfile, navController)
                        },
                        onClickCancel = {
                            scope.launch {
                                bottomSheetState.hide()

                            }
                        },viewModel)
                }


            },
            sheetPeekHeight = 0.dp
        ) {
            Scaffold(
                topBar = {
                    Text(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        text = "MY",
                        color = Color.White,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                },
                bottomBar = { MyBottomNavigation(navController) },
                content = {
                    Column(
                        modifier = Modifier
                            .padding(it)
                            .verticalScroll(scrollState)

                    ) {
                        MySettings(item = viewModel.getMyInfo()) {
                            scope.launch {
                                bottomSheetState.expand()
                            }
                        }
                        SettingBar("링크드아웃 배지") { viewModel.readDetailBadgeList(navController) }
                        LinkedOutBadgeGrid(viewModel)
                        SettingBar("최근 본 글") {navController.navigate("RecentViewedEssayPage")}
                        RecentEssayList(itemList = viewModel.getRecentViewedEssayList(),navController)
                        MembershipSettingBar("멤버십 관리"){}
                        SettingBar("계정 관리") {navController.navigate("AccountPage")}


                    }

                    if (viewModel.isBadgeClicked) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            BadgeDescriptionBox(viewModel.badgeBoxItem!!, viewModel)
                        }
                    }


                }
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MySettings(item: UserInfo, onClick: () -> Unit) {

    val annotatedString = remember {
        AnnotatedString.Builder().apply {
            withStyle(
                style = SpanStyle(
                    color = Color(0xFF616FED),
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("${item.nickname!!} ")
            }
            append("아무개")
        }.toAnnotatedString()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        if (item.profileImage != null){
            GlideImage(
                model = item.profileImage,
                contentDescription = "profileImage",
                Modifier.size(108.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(text = annotatedString, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.background(Color(0xFF0D0D0D), shape = RoundedCornerShape(20)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 10.dp, bottom = 10.dp)
                ) {
                    Text(text = "쓴글", fontSize = 10.sp, color = Color(0xFF616161))
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = item.essayStats!!.totalEssays.toString(),
                        fontSize = 18.sp,
                        color = Color(0xFF616161)
                    )
                }
                VerticalDivider(Modifier.height(41.dp), color = Color(0xFF191919))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "발행", fontSize = 10.sp, color = Color(0xFF616161))
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = item.essayStats!!.publishedEssays.toString(),
                        fontSize = 18.sp,
                        color = Color(0xFF616161)
                    )
                }
                VerticalDivider(Modifier.height(41.dp), color = Color(0xFF191919))

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "링크드아웃", fontSize = 10.sp, color = Color(0xFF616161))
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = item.essayStats!!.linkedOutEssays.toString(),
                        fontSize = 18.sp,
                        color = Color(0xFF616161)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .clickable { onClick() }
                .fillMaxWidth()
                .background(Color(0xFF616FED), shape = RoundedCornerShape(20))
                .height(50.dp), contentAlignment = Alignment.Center
        ) {
            Text(text = "프로필 편집", fontSize = 14.sp, color = Color.Black)
        }

    }
}

@Composable
fun SettingBar(text: String, onClick: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(20.dp)
        .height(50.dp)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF616FED)
            )
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "navigate",
                tint = Color(0xFF686868)
            )

        }
    }
}
@Composable
fun MembershipSettingBar(text: String, onClick: () -> Unit){

    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(20.dp)
        .height(50.dp)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF616FED)
            )
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
            Box(modifier = Modifier
                .size(60.dp, 24.dp)
                .background(Color(0xFF191919), shape = RoundedCornerShape(40)), contentAlignment = Alignment.Center){
                Text(text = "  준비중  ", fontWeight = FontWeight.SemiBold, color = LinkedInColor, fontSize = 12.sp )

            }

        }
    }
}

@Composable
fun RecentEssayItem(item: EssayApi.EssayItem,viewModel : CommunityViewModel = hiltViewModel(),navController: NavController) {
    Box(modifier = Modifier
        .size(150.dp, 120.dp)
        .clickable { /* 에세이로 이동 */
        viewModel.readDetailRecentEssay(item.id!!,navController)}) {
        Column {
            Text(text = item.title!!)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = item.content!!,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun RecentEssayList(itemList: List<EssayApi.EssayItem>,navController: NavController) {
    if (itemList.isEmpty()){
        Text(text = "최근 본 글이 없습니다.", modifier = Modifier.padding(horizontal = 20.dp), color = Color(0xFF494747))
    }
    else{
        LazyRow(Modifier.padding(start = 20.dp)) {
            itemsIndexed(itemList) { index, item ->
                RecentEssayItem(item,navController = navController)

                // 마지막 항목인 경우에만 Spacer와 VerticalDivider 실행
                if (index < itemList.size - 1) {
                    Spacer(modifier = Modifier.width(10.dp))
                    VerticalDivider(Modifier.height(100.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }
    }
    
}

@Composable
fun BadgeDescriptionBox(badgeBoxItem: BadgeBoxItem, viewModel: SettingsViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.5f))
    )
    Box(modifier = Modifier.size(300.dp, 350.dp)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Image(
                painter = painterResource(id = R.drawable.badge_container),
                contentDescription = ""
            )
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = badgeBoxItem.badgeResourceId),
                contentDescription = "",
                Modifier
                    .size(150.dp)
                    .padding(end = 20.dp, bottom = 10.dp)
            )
            Spacer(modifier = Modifier.height(23.dp))
            Text(text = badgeBoxItem.badgeName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "${badgeBoxItem.badgeEmotion} 감정 표현 해시태그",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(
                    text = "  10개  ", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier =
                    Modifier
                        .background(Color.Blue, shape = RoundedCornerShape(40))
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = " 사용", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(23.dp))
            Button(
                onClick = { viewModel.isBadgeClicked = false },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20)
            ) {
                Text(text = "확인")
            }
        }
    }

}

//터치했을때 아이콘 확대축소 가능하게?
@Composable
fun LinkedOutBadgeItem(
    badgeBoxItem: BadgeBoxItem,
    viewModel: SettingsViewModel
) {
    // 기본 Modifier 정의
    val baseModifier = Modifier
        .size(110.dp)
        .clickable {
            if (badgeBoxItem.level >= 1) {
                viewModel.badgeBoxItem = badgeBoxItem
                viewModel.isBadgeClicked = true
            }
        }
        .background(
            Color(0xFF0D0D0D),
            shape = RoundedCornerShape(10)
        )

    // badgeBoxItem의 level에 따라서 blur 효과를 다르게 적용
    val finalModifier = if (badgeBoxItem.level == 0) {
        baseModifier.blur(20.dp)
    } else {
        baseModifier
    }

    val colorMatrix = ColorMatrix().apply {
        if (badgeBoxItem.level == 0) setToSaturation(0f)
        else setToSaturation(1f)
    }

    Box(contentAlignment = Alignment.Center, modifier = finalModifier) {
        Image(
            modifier = Modifier.size(80.dp),
            painter = painterResource(id = badgeBoxItem.badgeResourceId),
            contentDescription = "badge_sad",
            colorFilter = ColorFilter.colorMatrix(colorMatrix)
        )
    }
}


@Composable
fun LinkedOutBadgeGrid(viewModel: SettingsViewModel) {
    val badgeList = viewModel.getSimpleBadgeList()

    LaunchedEffect(Unit) {
        delay(50)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(horizontal = 16.dp), // 수평 방향으로 패딩 추가
        contentAlignment = Alignment.Center

    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(badgeList) {
                LinkedOutBadgeItem(it, viewModel)
            }
            // 각 아이템에 대한 UI를 구성
            // 여기에는 각 아이템을 표시하는 Composable 함수 호출
        }
    }
}
@Composable
fun ModifyNickNameTextField( viewModel: SettingsViewModel) {

    var text by remember {
        mutableStateOf(viewModel.getMyInfo().nickname!!)
    }
    //수정 안했을때 기본 닉네임 설정. 원래 닉네임으로.
    viewModel.newProfile.nickname = text

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text = "링크드아웃 필명", fontSize = 12.sp, color = Color(0xFF464646))
        Spacer(modifier = Modifier.height(6.5.dp))
        OutlinedTextField(
            value = text,
            onValueChange = {
                if (it.length < 7){
                    text = it
                    viewModel.newProfile.nickname = text //수정할때마다 새 프로필 닉네임 변경
                    Log.d(TAG, "ModifyNickNameTextField: ${viewModel.newProfile}")
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = LinkedInColor,
                unfocusedBorderColor = Color(0xFF252525),
                focusedBorderColor = LinkedInColor,
                unfocusedTextColor = LinkedInColor,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent

            ),
            suffix = { Text(text = "아무개         ", color = Color.White) },
            )
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = "   *필명은 최대 6자, 한글로만 입력 가능합니다.", color = LinkedInColor, fontSize = 10.sp)

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyMyProfileBottomSheet(
    onClickComplete: () -> Unit,
    onClickCancel: () -> Unit,
    viewModel: SettingsViewModel
) {
    val focusManager = LocalFocusManager.current

    val url = viewModel.newProfile.profileImage //널이 아닐경우 그대로사용
        ?: viewModel.getMyInfo().profileImage //널일경우 이 값을사용
        ?: PRIVATE_POPUP_URL // 둘다 널일경우 기본.

    viewModel.newProfile.profileImage = url



    Box(modifier = Modifier
        .clickable { focusManager.clearFocus() }
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
        .height(770.dp)) {
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                title = {
                    Text(
                        text = "프로필 편집",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White
                    )
                },
                actions = {
                    Text(text = "완료",
                        color = Color.White,
                        modifier = Modifier.clickable {
                            onClickComplete()
                            focusManager.clearFocus()
                            Log.d(TAG, "MyPage: ${viewModel.newProfile}")

                        }
                    )
                },
                navigationIcon = {
                    Text(
                        text = "취소",
                        color = Color(0xFF686868),
                        modifier = Modifier.clickable {
                            //새로운 프로필을 공백으로 다시 초기화
                            onClickCancel()
                            focusManager.clearFocus()
                        }

                    )
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            SelectProfileImageIcon(
                { viewModel.isClickedModifyImage = true },
                imageUrl = url
            )
            Spacer(modifier = Modifier.height(26.dp))
            ModifyNickNameTextField( viewModel)

        }//이미지변경

    }
}



@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SelectProfileImageIcon(onClickModifyImage: () -> Unit, imageUrl : String ) {


    Box(modifier = Modifier.size(120.dp)) {
        GlideImage(
            modifier = Modifier.fillMaxSize(),
            model = imageUrl,
            contentDescription = "img"
        )
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color.White, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "",
                    Modifier
                        .size(15.dp)
                        .clickable { onClickModifyImage() })

            }
        }
    }
}



@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SelectProfileIconBottomSheet(viewModel: SettingsViewModel){

    val icons = listOf(PRIVATE_POPUP_URL,
        PUBLISHED_POPUP_URL,
        LINKEDOUT_POPUP_URL,PRIVATE_POPUP_URL,PRIVATE_POPUP_URL, INSPECT_POPUP_URL)

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
        .height(770.dp)) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                title = {
                    Text(
                        text = "아이콘 선택",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "arrow back",
                        modifier = Modifier
                            .clickable { viewModel.isClickedModifyImage = false }
                            .size(24.dp, 21.dp)
                            .padding(start = 10.dp),
                        tint = Color.White
                    )

                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(40.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                items(icons) {
                    GlideImage(
                        model = it,
                        contentDescription = "image",
                        Modifier
                            .size(120.dp)
                            .clickable {
                                viewModel.isClickedModifyImage = false
                                viewModel.newProfile.profileImage = it
                                Log.d(TAG, "MyPage: ${viewModel.newProfile}")

                            }
                    )
                }
            }
        }
    }
}



