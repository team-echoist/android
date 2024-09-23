package com.echoist.linkedout.presentation.userInfo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.echoist.linkedout.presentation.util.PRIVATE_POPUP_URL
import com.echoist.linkedout.presentation.util.PROFILE_IMAGE_01
import com.echoist.linkedout.presentation.util.PROFILE_IMAGE_02
import com.echoist.linkedout.presentation.util.PROFILE_IMAGE_03
import com.echoist.linkedout.presentation.util.PROFILE_IMAGE_04
import com.echoist.linkedout.presentation.util.PROFILE_IMAGE_05
import com.echoist.linkedout.presentation.util.PROFILE_IMAGE_06
import com.echoist.linkedout.presentation.util.PROFILE_IMAGE_07
import com.echoist.linkedout.presentation.util.PROFILE_IMAGE_08
import com.echoist.linkedout.presentation.util.PROFILE_IMAGE_09
import com.echoist.linkedout.presentation.util.PROFILE_IMAGE_10
import com.echoist.linkedout.presentation.util.PROFILE_IMAGE_11
import com.echoist.linkedout.presentation.util.PROFILE_IMAGE_12
import com.echoist.linkedout.R
import com.echoist.linkedout.presentation.util.Routes
import com.echoist.linkedout.presentation.util.TYPE_RECOMMEND
import com.echoist.linkedout.data.api.EssayApi
import com.echoist.linkedout.data.dto.BadgeBoxItem
import com.echoist.linkedout.data.dto.EssayStats
import com.echoist.linkedout.data.dto.UserInfo
import com.echoist.linkedout.presentation.home.MyBottomNavigation
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.presentation.community.CommunityViewModel
import com.echoist.linkedout.presentation.essay.EssayViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPage(
    navController: NavController,
    viewModel: MyPageViewModel = hiltViewModel(),
    essayViewModel: EssayViewModel = hiltViewModel(),
    communityViewModel: CommunityViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val userInfo by viewModel.userProfile.collectAsState()
    val badgeList by viewModel.badgeList.collectAsState()
    val recentEssayList by essayViewModel.recentEssayList.collectAsState()

    val bottomSheetState =
        rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)
    val scaffoldState = androidx.compose.material3.rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    LaunchedEffect(true) {
        viewModel.requestMyInfo()
        viewModel.readSimpleBadgeList()
        viewModel.getMyInfo()
    }

    BottomSheetScaffold(
        sheetContainerColor = Color(0xFF111111),
        scaffoldState = scaffoldState,
        sheetContent = {
            // 이미지 수정 바텀시트
            AnimatedVisibility(
                visible = viewModel.isClickedModifyImage,
                enter = fadeIn(animationSpec = tween(durationMillis = 500, easing = LinearEasing)),
                exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
            ) {
                SelectProfileIconBottomSheet(
                    uploadImage = { uri ->
                        scope.launch {
                            viewModel.uploadImage(uri, context)
                        }
                    },
                    onClickImage = { imageUrl ->
                        viewModel.onImageChange(imageUrl)
                        viewModel.isClickedModifyImage = false
                    },
                    onClickBack = { viewModel.isClickedModifyImage = false }
                )
            }
            // 프로필 편집 바텀시트
            AnimatedVisibility(
                visible = !viewModel.isClickedModifyImage,
                enter = fadeIn(animationSpec = tween(durationMillis = 500, easing = LinearEasing)),
                exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
            ) {
                ModifyMyProfileBottomSheet(
                    onClickComplete = {
                        viewModel.updateMyInfo(navController)
                    },
                    onClickCancel = {
                        scope.launch {
                            bottomSheetState.hide()
                        }
                    },
                    onClickImageChange = {
                        viewModel.isClickedModifyImage = true
                    }
                )
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
                    modifier = Modifier
                        .padding(start = 20.dp, top = 10.dp)
                        .safeDrawingPadding()
                )
            },
            bottomBar = { MyBottomNavigation(navController) },
            content = {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .verticalScroll(scrollState)
                ) {
                    MySettings(userInfo) {
                        scope.launch {
                            bottomSheetState.expand()
                        }
                    }
                    SettingBar("링크드아웃 배지") { navController.navigate(Routes.BadgePage) }
                    LinkedOutBadgeGrid(badgeList) {
                        viewModel.badgeBoxItem = it
                        viewModel.isBadgeClicked = true
                    }
                    SettingBar("최근 본 글") { navController.navigate(Routes.RecentViewedEssayPage) }
                    RecentEssayList(recentEssayList) {
                        communityViewModel.readDetailRecentEssay(
                            it,
                            navController,
                            TYPE_RECOMMEND
                        )
                    }
                    MembershipSettingBar("멤버십 관리") {}
                    SettingBar("계정 관리") { navController.navigate(Routes.AccountPage) }
                }
                if (viewModel.isBadgeClicked) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        BadgeDescriptionBox(viewModel.badgeBoxItem!!) {
                            viewModel.isBadgeClicked = false
                        }
                    }
                }
            }
        )
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
                append("${item.nickname ?: ""} ")
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
        if (item.profileImage != null) {
            Box(
                modifier = Modifier
                    .size(108.dp)
                    .clip(CircleShape)
            ) {
                GlideImage(
                    model = item.profileImage,
                    contentDescription = "profileImage",
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            // 이미지를 원 중앙에 정렬
                            clip = true
                            shape = CircleShape
                        },
                    contentScale = ContentScale.Crop // 이미지를 자르고 원에 맞게 보여줍니다.
                )
            }
        }
        val essayStats = item.essayStats ?: EssayStats()

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

                        text = essayStats.totalEssays.toString(),
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
                        text = essayStats.publishedEssays.toString(),
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
                        text = essayStats.linkedOutEssays.toString(),
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MembershipSettingBar(text: String, onClick: () -> Unit) {
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
            Box(
                modifier = Modifier
                    .size(60.dp, 24.dp)
                    .background(Color(0xFF191919), shape = RoundedCornerShape(40)),
                contentAlignment = Alignment.Center
            ) {
                GlideImage(model = R.drawable.icon_comingsoon, contentDescription = "comingsoon")
            }
        }
    }
}

@Composable
fun RecentEssayItem(
    item: EssayApi.EssayItem,
    onClickEssayItem: (Int) -> Unit
) {
    Box(modifier = Modifier
        .size(150.dp, 120.dp)
        .clickable { /* 에세이로 이동 */
            onClickEssayItem(item.id!!)
        }) {
        Column {
            Text(text = item.title!!)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = item.content!!,
                lineHeight = 27.2.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun RecentEssayList(
    itemList: List<EssayApi.EssayItem>,
    onClickEssayItem: (Int) -> Unit
) {
    if (itemList.isEmpty()) {
        Text(
            text = "최근 본 글이 없습니다.",
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color(0xFF494747)
        )
    } else {
        LazyRow(Modifier.padding(start = 20.dp)) {
            itemsIndexed(itemList) { index, item ->
                RecentEssayItem(item, onClickEssayItem)
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
fun BadgeDescriptionBox(badgeBoxItem: BadgeBoxItem, isBadgeClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.5f))
    )
    Box(modifier = Modifier.size(300.dp, 350.dp)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Image(
                painter = painterResource(id = R.drawable.badge_container),
                contentDescription = "badge_container"
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
                onClick = { isBadgeClicked() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20)
            ) {
                Text(text = "확인")
            }
        }
    }

}

//터치했을때 아이콘 확대축소 가능하게?
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LinkedOutBadgeItem(
    badgeBoxItem: BadgeBoxItem,
    onClickBadge: (BadgeBoxItem) -> Unit
) {
    // 기본 Modifier 정의
    val baseModifier = Modifier
        .size(110.dp)
        .clickable {
            if (badgeBoxItem.level >= 1) {
                onClickBadge(badgeBoxItem)
            }
        }
        .background(
            Color(0xFF0D0D0D),
            shape = RoundedCornerShape(10)
        )

    // badgeBoxItem의 level에 따라서 blur 효과를 다르게 적용
    val finalModifier = if (badgeBoxItem.level == 0) {
        baseModifier.blur(10.dp)
    } else {
        baseModifier
    }

    val colorMatrix = ColorMatrix().apply {
        if (badgeBoxItem.level == 0) setToSaturation(0f)
        else setToSaturation(1f)
    }

    Box(contentAlignment = Alignment.Center, modifier = finalModifier) { //todo 수정
        Image(
            modifier = Modifier.size(80.dp),
            painter = painterResource(id = badgeBoxItem.badgeResourceId),
            contentDescription = "badge_sad",
            colorFilter = ColorFilter.colorMatrix(colorMatrix)
        )
//        GlideImage(
//            modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillWidth,
//            model = "https://cdn.discordapp.com/attachments/1218950730971349023/1265604412982951966/blur.png?ex=66a21d75&is=66a0cbf5&hm=8232b6c7cee7d84ff65b06d87ddcc854e43a29f408f00b16fa94504977f54ee1&",
//            contentDescription = "")
    }
}

@Composable
fun LinkedOutBadgeGrid(badgeList: List<BadgeBoxItem>, onClickBadge: (BadgeBoxItem) -> Unit) {
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
                LinkedOutBadgeItem(it) {
                    onClickBadge(it)
                }
            }
            // 각 아이템에 대한 UI를 구성
            // 여기에는 각 아이템을 표시하는 Composable 함수 호출
        }
    }
}

@Composable
fun ModifyNickNameTextField(
    nickname: String,
    nicknameCheckCode: Int,
    onNicknameChange: (String) -> Unit,
) {
    var text by remember { mutableStateOf(nickname) }
    var message by remember {
        mutableStateOf("   *필명은 최대 6자, 한글로만 입력 가능합니다.")
    }

    message = when (nicknameCheckCode) {
        200, 201 -> "   * 사용가능한 닉네임입니다!"
        409 -> "   * 이미 사용중인 닉네임입니다."
        400 -> "   * 필명은 3자 이상 6자 이하의 완성된 한글 단어로만 구성할 수 있습니다."
        else -> ("   * $nicknameCheckCode")  // 에러코드
    }

    val color = when (nicknameCheckCode) {
        409, 400 -> Color.Red
        else -> LinkedInColor
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text = "링크드아웃 필명", fontSize = 12.sp, color = Color(0xFF464646))
        Spacer(modifier = Modifier.height(6.5.dp))
        OutlinedTextField(
            value = text,
            isError = nicknameCheckCode == 409 || nicknameCheckCode == 400,
            onValueChange = {
                if (it.length < 7) {
                    text = it
                    onNicknameChange(it) // 상위에서 처리하는 이벤트로 변경
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
                focusedContainerColor = Color.Transparent,
                errorTextColor = Color.Red,
                errorBorderColor = Color.Red
            ),
            shape = RoundedCornerShape(10),
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = message, color = color, fontSize = 10.sp)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyMyProfileBottomSheet(
    viewModel: MyPageViewModel = hiltViewModel(),
    onClickComplete: () -> Unit,
    onClickCancel: () -> Unit,
    onClickImageChange: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val userProfile by viewModel.tempProfile.collectAsState()

    val url = userProfile.profileImage
        ?: PRIVATE_POPUP_URL

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
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .clickable {
                                onClickComplete()
                                focusManager.clearFocus()
                            }
                    )
                },
                navigationIcon = {
                    Text(
                        text = "취소",
                        color = Color(0xFF686868),
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .clickable {
                                onClickCancel()
                                focusManager.clearFocus()
                            }
                    )
                }
            )
            Spacer(modifier = Modifier.height(40.dp))
            SelectProfileImageIcon(imageUrl = url) {
                onClickImageChange()
            }
            Spacer(modifier = Modifier.height(26.dp))
            ModifyNickNameTextField(
                nickname = userProfile.nickname ?: "에러",
                nicknameCheckCode = viewModel.nicknameCheckCode,
                onNicknameChange = { viewModel.onNicknameChange(it) }
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SelectProfileImageIcon(imageUrl: String, onClickModifyImage: () -> Unit) {
    Box(modifier = Modifier.size(120.dp)) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        ) {
            GlideImage(
                model = imageUrl,
                contentDescription = "profileImage",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        // 이미지를 원 중앙에 정렬
                        clip = true
                        shape = CircleShape
                    },
                contentScale = ContentScale.Crop // 이미지를 자르고 원에 맞게 보여줍니다.
            )
        }
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
fun SelectProfileIconBottomSheet(
    uploadImage: (Uri) -> Unit,
    onClickImage: (String) -> Unit,
    onClickBack: () -> Unit
) {
    val icons = listOf(
        PROFILE_IMAGE_01,
        PROFILE_IMAGE_02,
        PROFILE_IMAGE_03,
        PROFILE_IMAGE_04,
        PROFILE_IMAGE_05,
        PROFILE_IMAGE_06,
        PROFILE_IMAGE_07,
        PROFILE_IMAGE_08,
        PROFILE_IMAGE_09,
        PROFILE_IMAGE_10,
        PROFILE_IMAGE_11,
        PROFILE_IMAGE_12
    )

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            selectedImageUri?.let {
                uploadImage(it)
                onClickImage(it.toString())
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(770.dp)
    ) {
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
                            .clickable { onClickBack() }
                            .padding(start = 10.dp)
                            .size(30.dp),
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
                itemsIndexed(icons) { index, icon ->
                    if (index == 1) {
                        GlideImage(
                            model = icon,
                            contentDescription = "image",
                            Modifier
                                .size(120.dp)
                                .clickable {
                                    val intent = Intent(Intent.ACTION_PICK)
                                    intent.type = "image/*"
                                    galleryLauncher.launch(intent)
                                }
                        )
                    } else {
                        // 나머지 항목
                        GlideImage(
                            model = icon,
                            contentDescription = "image",
                            Modifier
                                .size(120.dp)
                                .clickable {
                                    onClickImage(icon)
                                }
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }
}