package com.echoist.linkedout.presentation.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.data.api.EssayApi
import com.echoist.linkedout.data.dto.BottomNavItem
import com.echoist.linkedout.data.dto.UserInfo
import com.echoist.linkedout.presentation.essay.write.Token
import com.echoist.linkedout.presentation.essay.write.WritingViewModel
import com.echoist.linkedout.presentation.home.drawable.DrawableScreen
import com.echoist.linkedout.presentation.home.tutorial.TutorialScreen
import com.echoist.linkedout.presentation.util.Routes
import com.echoist.linkedout.presentation.util.TUTORIAL_BULB
import com.echoist.linkedout.presentation.util.UserStatus
import com.echoist.linkedout.presentation.util.calculateDaysDifference
import com.echoist.linkedout.presentation.util.getCurrentDateFormatted
import com.echoist.linkedout.presentation.util.navigateWithClearBackStack
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HomePage(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    writingViewModel: WritingViewModel,
    statusCode: Int
) {

    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val isUserDeleteApiFinished by viewModel.isUserDeleteApiFinished.collectAsState()
    val isExistLatestUpdate by viewModel.isExistLatestUpdate.collectAsState()

    Log.d("header token by autoLogin: in home", "${Token.accessToken} \n ${Token.refreshToken}")
    var userStatus by remember { mutableStateOf(UserStatus.Activated) }

    userStatus = when (statusCode) {
        202 -> UserStatus.DeActivated
        403 -> UserStatus.Banned
        else -> UserStatus.Activated //todo 현재 202만 탈퇴유저 밴, 관찰유저, 등등 추가예정
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.requestMyInfo()
        viewModel.requestUserGraphSummary()
        viewModel.requestGuleRoquis()
        viewModel.requestUnreadAlerts()
        viewModel.requestLatestNotice()
        viewModel.requestRegisterDevice(context) //로그인 후 홈 진입 시 한번만 회원정보 등록
        viewModel.requestLatestUpdate()
    }

    LaunchedEffect(key1 = isUserDeleteApiFinished) {
        if (isUserDeleteApiFinished) {
            navigateWithClearBackStack(navController, Routes.LoginPage)
            viewModel.setApiStatusToFalse()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawableScreen(viewModel = viewModel, navController)
        },
    ) {
        Scaffold(
            topBar = {
                CustomTopAppBar({
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }, { navController.navigate("NotificationPage") },
                    viewModel.isExistUnreadAlerts
                )
                {
                    viewModel.isFirstUser = true //튜토리얼 화면띄우기위함
                }
            },
            bottomBar = { MyBottomNavigation(navController) },
            floatingActionButton = { WriteFTB(navController, viewModel, writingViewModel) },
            content = {
                Column(modifier = Modifier.padding(it)) {

                }
                Box(modifier = Modifier.fillMaxSize()) {
                    GlideImage(
                        model = R.drawable.home_basic,
                        contentDescription = "home_img",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillWidth
                    )

                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 165.dp, bottom = 130.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        Modifier
                            .size(80.dp)
                            .clickable { viewModel.isVisibleGeulRoquis = true }) {
                        GlideImage( //전구 클릭하면 글로키 On
                            model = TUTORIAL_BULB,
                            contentDescription = "bulb_img",
                            modifier = Modifier
                                .size(80.dp)
                        )
                    }
                }
            }
        )
    }
    //터치 효과 x

    AnimatedVisibility(
        visible = viewModel.isVisibleGeulRoquis && !viewModel.isFirstUser, //튜토리얼을 건너뛰어야 글로키를 볼수있음
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
                .background(Color.Black.copy(0.7f)), contentAlignment = Alignment.Center
        ) {
            GeulRoquis(
                isHoldClicked = { viewModel.isVisibleGeulRoquis = false },
                isAcceptClicked = {
                    writingViewModel.title.value =
                        TextFieldValue("${getCurrentDateFormatted()} GeulRoquis")
                    writingViewModel.hint =
                        ("글로키란? : 글(geul)과 크로키(croquis)의 합성어로 글을 본격적으로 쓰기 전, 주어진 상황을 묘사하거나 상상을 덧대어 빠르게 스케치 하듯이 글을 쓰는 몸풀기를 말합니다. ")
                    writingViewModel.imageUrl = viewModel.geulRoquisUrl
                    navController.navigate("WritingPage")
                    viewModel.isVisibleGeulRoquis = false
                }, viewModel
            )
        }
    }

    if (viewModel.isFirstUser) { // 첫 회원이라면
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.7f))
        )
        TutorialScreen(
            isCloseClicked = {
                viewModel.isFirstUser = false
                viewModel.requestFirstUserToExistUser()
            },
            isSkipClicked = {
                viewModel.isFirstUser = false
                viewModel.requestFirstUserToExistUser()
            })
    }
    if (userStatus == UserStatus.DeActivated) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.7f)), contentAlignment = Alignment.Center
        )
        {
            ReactivateOrDeleteBox(
                isClickedReActivate = {
                    viewModel.requestUserReActivate()
                    userStatus = UserStatus.Activated
                })
            {
                viewModel.requestUserDelete()
            }
        }
    }
    if (viewModel.latestNoticeId != null) { //id값
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.7f)), contentAlignment = Alignment.Center
        )
        {
            NoticeAlert(isClickedClose = {
                viewModel.latestNoticeId = null
            }, isClickedOpened = {
                navController.navigate("${Routes.NoticeDetailPage}/${viewModel.latestNoticeId!!}")
            })
        }
    }

    if (isExistLatestUpdate) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.7f)), contentAlignment = Alignment.Center
        )
        {
            UpdateAlert(isClickedClose = {
                viewModel.updateIsExistLatestUpdate(false)
            }, isClickedOpened = {
                navController.navigate(Routes.UpdateHistoryScreen)
                viewModel.updateIsExistLatestUpdate(false)
            })
        }
    }
}

@Composable
fun WriteFTB(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    writingViewModel: WritingViewModel = hiltViewModel()
) {

    FloatingActionButton(
        modifier = Modifier.padding(end = 25.dp, bottom = 25.dp),
        onClick = {
            navController.navigate("WritingPage")
            viewModel.initializeDetailEssay()
            viewModel.setStorageEssay(EssayApi.EssayItem())
            writingViewModel.isModifyClicked = false
            writingViewModel.initialize()
        },
        shape = RoundedCornerShape(100.dp),
        containerColor = Color.White
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ftb_edit),
            contentDescription = "edit",
            modifier = Modifier.size(20.dp),
            tint = Color.Black
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    onClick: () -> Unit,
    onClickNotification: () -> Unit,
    isExistUnreadAlerts: Boolean,
    isClickedTutorial: () -> Unit
) {
    val img = if (isExistUnreadAlerts) R.drawable.icon_noti_on else R.drawable.icon_noti_off
    TopAppBar(
        title = { }, colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
        navigationIcon = {
            Icon(
                tint = Color.White,
                painter = painterResource(id = R.drawable.hamburber),
                contentDescription = "Menu",
                modifier = Modifier
                    .clickable { onClick() }
                    .padding(start = 10.dp)
                    .size(24.dp)
            )
        },
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.icon_information),
                contentDescription = "Notifications",
                tint = Color.White,
                modifier = Modifier
                    .clickable { isClickedTutorial() }
                    .padding(end = 10.dp)
                    .size(30.dp)
            )
            Icon(
                painter = painterResource(id = img),
                contentDescription = "Notifications",
                tint = Color.Unspecified,
                modifier = Modifier
                    .clickable { onClickNotification() }
                    .padding(end = 10.dp)
                    .size(30.dp)
            )
        },
    )

}

@Preview
@Composable
fun PrevBottomNav() {
    MyBottomNavigation(navController = rememberNavController())
}

@Composable
fun MyBottomNavigation(navController: NavController, onClick: (String) -> Unit = {}) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.MyLog,
        BottomNavItem.Community,
        BottomNavItem.Settings
    )

    NavigationBar(containerColor = Color.Black) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent),
                selected = isRouteSelected(currentRoute, item.screenRoute),
                onClick = {
                    navController.navigate(item.screenRoute)
                    onClick(item.screenRoute)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size(32.dp),
                        tint = if (isRouteSelected(
                                currentRoute,
                                item.screenRoute
                            )
                        ) Color.White else Color(0xFF686868)
                    )
                }
            )
        }
    }
}

// 현재 라우트에서 동적 파라미터를 제거하고 비교
private fun isRouteSelected(currentRoute: String?, screenRoute: String): Boolean {
    return currentRoute?.substringBefore('/') == screenRoute.substringBefore('/')
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MyProfile(item: UserInfo, onClick: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(20.dp)
            .clickable { onClick() }, contentAlignment = Alignment.BottomCenter
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(80.dp)
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
            Spacer(modifier = Modifier.width(20.dp))
            Column(Modifier.weight(7f)) {
                Row {
                    Text(text = item.nickname ?: "", color = Color(0xFF616FED))
                    Text(text = " 아무개", color = Color.White)
                }
                Spacer(modifier = Modifier.height(7.dp))
                Text(
                    text = "${calculateDaysDifference(item.createdDate ?: "2024-08-19T19:31:18.945+09:00")}일째 링크드 아웃!",
                    color = Color(0xFF686868),
                    fontSize = 12.sp
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "go",
                tint = Color(0xFF686868)
            )
        }
    }
}


@Composable
fun LineChartExample(modifier: Modifier = Modifier, essayCounts: List<Int>) {
    val lineData = createLineData(essayCounts)
    // 현재 달을 가져옴
    val currentMonth =
        LocalDate.now().month.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault())

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(horizontal = 20.dp),
        factory = { context ->
            LineChart(context).apply {
                data = lineData
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.textColor = 0xFF616FED.toInt() //링크드아웃 컬러
                xAxis.valueFormatter =
                    IndexAxisValueFormatter(arrayOf("", currentMonth, "", "", "이번 주"))
                xAxis.granularity = 1f
                xAxis.labelCount = 5
                xAxis.setDrawAxisLine(false)

                axisRight.isEnabled = false
                axisLeft.isEnabled = false
                setTouchEnabled(false)
                description.isEnabled = false

                legend.isEnabled = false

                animateX(1000)
            }
        },
        update = { chart ->
            chart.data = lineData
            chart.invalidate() // 차트 갱신
        }
    )
}

fun createLineData(essayCounts: List<Int>): LineData {
    val entries = listOf(
        Entry(0f, essayCounts[0].toFloat()),
        Entry(1f, essayCounts[1].toFloat()),
        Entry(2f, essayCounts[2].toFloat()),
        Entry(3f, essayCounts[3].toFloat()),
        Entry(4f, essayCounts[4].toFloat())
    )

    val dataSet = LineDataSet(entries, "").apply {
        color = 0xFF616FED.toInt()
        circleRadius = 4f // 원의 반지름 크기를 작게 설정
        valueTextColor = android.graphics.Color.BLACK
        setCircleColor(0xFF616FED.toInt())
        lineWidth = 2f // 선의 두께를 줄여 설정
        circleHoleRadius = 2f // 원의 구멍 반지름 크기를 작게 설정
        setDrawCircles(true)
        setDrawValues(false)
        setDrawIcons(false)


    }

    return LineData(dataSet)
}


@Composable
fun MyLinkedOutBar() {
    val currentDate = LocalDate.now()
    val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(60.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            Text(text = "주간 링크드아웃 지수", fontSize = 10.sp, color = Color.White)
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
            Text(text = "$formattedDate 현재", fontSize = 10.sp, color = Color(0xFF686868))
        }
    }
}

@Composable
fun MyDrawableItem(text: String, onClick: () -> Unit) {

    NavigationDrawerItem(
        modifier = Modifier.height(70.dp),
        label = {
            Text(text = text, color = Color(0xFF797979))
        },
        selected = false,
        onClick = { onClick() },
    )
}

@Preview
@Composable
fun ShopDrawerItem() {

    NavigationDrawerItem(
        modifier = Modifier.height(80.dp),
        label = {
            Text(text = "상점", color = Color.White, fontWeight = FontWeight.SemiBold)
        },
        selected = false,
        onClick = { },
        badge = {
            Box(
                modifier = Modifier
                    .size(60.dp, 24.dp)
                    .background(Color(0xFF191919), shape = RoundedCornerShape(40)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "  준비중  ",
                    fontWeight = FontWeight.SemiBold,
                    color = LinkedInColor,
                    fontSize = 12.sp
                )

            }
        }

    )
}

@Composable
fun LogoutBtn(isLogoutClicked: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 70.dp, end = 20.dp), contentAlignment = Alignment.BottomEnd
    ) {
        Box(
            modifier = Modifier
                .size(80.dp, 36.dp)
                .background(Color(0xFF191919), shape = RoundedCornerShape(20))
                .clickable { isLogoutClicked() },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "로그아웃", fontSize = 12.sp, color = Color(0xFF606060))
        }
    }
}

@Composable
fun LogoutBox(isCancelClicked: () -> Unit, isLogoutClicked: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp)
            .background(Color.Black.copy(0.4f))
    )
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF121212), shape = RoundedCornerShape(10))
                .height(243.dp), contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "로그아웃하시겠습니까?", color = Color.White, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(48.dp))
                Row {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(
                                0xFF868686
                            )
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(61.dp),
                        onClick = { isCancelClicked() },
                        shape = RoundedCornerShape(20)
                    ) {
                        Text(text = "취소", color = Color.Black, fontWeight = FontWeight.SemiBold)

                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF616FED)),
                        modifier = Modifier
                            .weight(1f)
                            .height(61.dp),
                        onClick = {
                            isLogoutClicked()
                            /*TODO*/
                        },
                        shape = RoundedCornerShape(20)
                    ) {
                        Text(text = "로그아웃", color = Color.Black, fontWeight = FontWeight.SemiBold)

                    }
                }

            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GeulRoquis(isHoldClicked: () -> Unit, isAcceptClicked: () -> Unit, viewModel: HomeViewModel) {

    Box(
        modifier = Modifier
            .background(color = Color(0xF2121212), shape = RoundedCornerShape(4))
            .size(281.dp, 411.dp)
    ) {
        GlideImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(256.dp),
            model = R.drawable.geulroquis_background,
            contentDescription = "linkedout logo",
            contentScale = ContentScale.Crop
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(31.dp))
            Text(text = "오늘의 글로키가 도착했어요!", color = Color.White, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "2024-07-24 GeulRoquis", color = Color.White, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Log.d("글로키 링크", viewModel.geulRoquisUrl)
            if (viewModel.geulRoquisUrl.isEmpty() || !viewModel.geulRoquisUrl.startsWith("https")) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = LinkedInColor)
                }
            } else {
                GlideImage(
                    model = viewModel.geulRoquisUrl,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth()
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "보류", color = Color.White, fontSize = 16.sp, modifier = Modifier
                    .clickable { isHoldClicked() }
                    .weight(1f), textAlign = TextAlign.Center)
                VerticalDivider(color = Color(0xFF292929))
                Text(text = "수락", color = Color.White, fontSize = 16.sp, modifier = Modifier
                    .clickable { isAcceptClicked() }
                    .weight(1f), textAlign = TextAlign.Center)

            }

        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Preview
@Composable
fun t() {

    Box(
        modifier = Modifier
            .background(color = Color(0xF2121212), shape = RoundedCornerShape(4))
            .size(281.dp, 411.dp)
    ) {
        GlideImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(256.dp),
            model = R.drawable.geulroquis_background,
            contentDescription = "linkedout logo",
            contentScale = ContentScale.Crop
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(31.dp))
            Text(text = "오늘의 글로키가 도착했어요!", color = Color.White, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "2024-07-24 GeulRoquis", color = Color.White, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(20.dp))


            GlideImage(
                model = R.drawable.home_basic,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(240.dp)
                    .fillMaxWidth()
            )


            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "보류", color = Color.White, fontSize = 16.sp, modifier = Modifier
                        .weight(1f), textAlign = TextAlign.Center
                )
                VerticalDivider(color = Color(0xFF292929))
                Text(
                    text = "수락", color = Color.White, fontSize = 16.sp, modifier = Modifier
                        .weight(1f), textAlign = TextAlign.Center
                )

            }

        }
    }
}

//탈퇴유저에게 보여줄 카드.
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ReactivateOrDeleteBox(isClickedReActivate: () -> Unit, isClickedDeActivate: () -> Unit) {

    Box(
        modifier = Modifier
            .size(300.dp, 286.dp)
            .background(Color(0xFF1D1D1D), shape = RoundedCornerShape(4))
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
            GlideImage(
                model = R.drawable.geulroquis_background,
                contentDescription = "background logo",
                modifier = Modifier.padding(start = 130.dp, bottom = 100.dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "오랜만에 봬요!", fontSize = 24.sp, color = Color.White)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "다시 링크드아웃에\n돌아오신 걸 환영해요.",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = { isClickedDeActivate() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(20),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF929292))
            ) {
                Text(text = "지금 계정 삭제하기", color = Color.Black)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { isClickedReActivate() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 20.dp),

                shape = RoundedCornerShape(20)
            ) {
                Text(text = "계정 복구하기", color = Color.Black)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NoticeAlert(isClickedClose: () -> Unit, isClickedOpened: () -> Unit) {

    Box(
        modifier = Modifier
            .width(280.dp)
            .height(203.dp)
            .background(color = Color(0xFF121212), shape = RoundedCornerShape(size = 10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            GlideImage(model = R.drawable.notice_linkedouticon, contentDescription = "")
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "아무개님, 새로운 공지사항이 있어요!",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                )
            )
            Spacer(modifier = Modifier.height(39.dp))
            Row {
                Button(
                    onClick = { isClickedOpened() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF868686)),
                    shape = RoundedCornerShape(20), modifier = Modifier
                        .height(50.dp)
                        .padding(start = 15.dp, end = 5.dp)
                        .weight(1f)
                ) {
                    Text(text = "자세히 보기", color = Color.Black, fontSize = 12.sp)
                }
                Button(
                    onClick = { isClickedClose() },
                    shape = RoundedCornerShape(20),
                    modifier = Modifier
                        .height(50.dp)
                        .padding(start = 5.dp, end = 15.dp)
                        .weight(1f)
                ) {
                    Text(text = "닫기", color = Color.Black, fontSize = 12.sp)
                }
            }

        }
    }
}

@Composable
fun ReLogInWaringAlert(isClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .height(150.dp)
            .background(Color(0xFF191919), shape = RoundedCornerShape(10)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "세션이 만료되었습니다.",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "다시 로그인 해주세요.", color = Color.White, fontSize = 14.sp)

        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp, end = 20.dp), contentAlignment = Alignment.BottomEnd
        ) {
            Text(text = "확인", color = LinkedInColor, modifier = Modifier.clickable { isClicked() })
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UpdateAlert(isClickedClose: () -> Unit, isClickedOpened: () -> Unit) {
    Box(
        modifier = Modifier
            .width(280.dp)
            .height(411.dp)
            .background(color = Color(0xFF121212), shape = RoundedCornerShape(size = 10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(24.dp))
            GlideImage(model = R.drawable.badge_update, contentDescription = "")
            Spacer(modifier = Modifier.height(40.dp))
            GlideImage(
                model = R.drawable.logo_update,
                contentDescription = "",
                modifier = Modifier.size(146.dp, 97.33.dp)
            )
            Spacer(modifier = Modifier.height(37.dp))
            Text(
                text = "아무개님, 새로운 버전으로\n업데이트되었습니다!",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFFFFE045),
                    textAlign = TextAlign.Center,
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "[자세히 보기]를 탭해 업데이트 히스토리를 확인해보세요.",
                style = TextStyle(
                    fontSize = 11.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFFFFE045),
                    textAlign = TextAlign.Center,
                )
            )
            Spacer(modifier = Modifier.height(27.dp))

            Row {
                Button(
                    onClick = { isClickedOpened() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF868686)),
                    shape = RoundedCornerShape(20), modifier = Modifier
                        .height(50.dp)
                        .padding(start = 15.dp, end = 5.dp)
                        .weight(1f)
                ) {

                    Text(
                        text = "자세히 보기",
                        color = Color.Black,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Button(
                    onClick = { isClickedClose() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFE045)),
                    shape = RoundedCornerShape(20),
                    modifier = Modifier
                        .height(50.dp)
                        .padding(start = 5.dp, end = 15.dp)
                        .weight(1f)
                ) {
                    Text(text = "닫기", color = Color.Black, fontSize = 12.sp)
                }
            }
        }
    }
}

