package com.echoist.linkedout.page.home

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
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.ModalDrawerSheet
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.TUTORIAL_BULB
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.data.BottomNavItem
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.getCurrentDateFormatted
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.HomeViewModel
import com.echoist.linkedout.viewModels.WritingViewModel
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


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController,viewModel: HomeViewModel,writingViewModel : WritingViewModel) {

    LaunchedEffect(key1 = Unit) {
        viewModel.requestMyInfo()
        viewModel.requestUserGraphSummaryResponse()
        viewModel.requestGuleRoquis()
        viewModel.requestUnreadAlerts()

    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            ModalBottomSheetContent(viewModel = viewModel,navController)
        }

    ) {
        val context = LocalContext.current
        viewModel.requestRegisterDevice(context) //로그인 후 홈 진입 시 회원정보등록

        LinkedOutTheme {

            Scaffold(
                topBar = {

                    CustomTopAppBar( {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },{navController.navigate("NotificationPage")},viewModel.isExistUnreadAlerts)
                },
                bottomBar = { MyBottomNavigation(navController) },
                floatingActionButton = { WriteFTB(navController,viewModel,writingViewModel) },
                content = {
                    Column(modifier = Modifier.padding(it)) {

                    }
                    Box(modifier = Modifier.fillMaxSize()){
                        GlideImage(
                            model = R.drawable.home_basic,
                            contentDescription = "home_img",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillWidth
                        )

                    }
                }
            )
        }
        //터치 효과 x

            Box(modifier = Modifier
                .fillMaxSize()
                .padding(start = 165.dp, bottom = 130.dp), contentAlignment = Alignment.Center){
                CompositionLocalProvider(LocalRippleConfiguration provides  null) {
                GlideImage( //전구 클릭하면 글로키 On
                    model = TUTORIAL_BULB,
                    contentDescription = "bulb_img",
                    modifier = Modifier
                        .size(80.dp)
                        .clickable { viewModel.isVisibleGeulRoquis = true }
                )
            }
        }


        AnimatedVisibility(
            visible = viewModel.isVisibleGeulRoquis,
            enter = fadeIn(animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)),
            exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
        ){
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.7f)), contentAlignment = Alignment.Center){
                GeulRoquis(
                    isHoldClicked = {viewModel.isVisibleGeulRoquis = false},
                    isAcceptClicked = {
                        writingViewModel.title.value = TextFieldValue("${getCurrentDateFormatted()} GeulRoquis")
                        writingViewModel.hint = ("글로키란? : 글(geul)과 크로키(croquis)의 합성어로 글을 본격적으로 쓰기 전, 주어진 상황을 묘사하거나 상상을 덧대어 빠르게 스케치 하듯이 글을 쓰는 몸풀기를 말합니다. ")
                        writingViewModel.imageUrl = viewModel.geulRoquisUrl
                        navController.navigate("WritingPage")
                        viewModel.isVisibleGeulRoquis = false
                    },viewModel)
            }
        }



        if (viewModel.isFirstUser){ // todo 첫 회원이라면
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.7f)))
            TutorialPage(
                isCloseClicked = {
                    viewModel.isFirstUser = false
                viewModel.setFirstUserToExistUser()
                                          },
                isSkipClicked = {
                    viewModel.isFirstUser = false
                viewModel.setFirstUserToExistUser()
                })
        }
    }
}


@Composable
fun ModalBottomSheetContent(viewModel: HomeViewModel,navController: NavController){
    var isLogoutClicked by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    ModalDrawerSheet(
        drawerShape = RectangleShape,
        drawerContainerColor = Color(0xFF121212)
    ) {
        Column(Modifier.verticalScroll(scrollState)) {
            MyProfile(item = viewModel.getMyInfo()){navController.navigate("SETTINGS")}
            HorizontalDivider(thickness = 6.dp, color = Color(0xFF191919))
            MyLinkedOutBar()
            LineChartExample(essayCounts = viewModel.essayCount)
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(thickness = 6.dp, color = Color(0xFF191919))
            ShopDrawerItem()
            HorizontalDivider(thickness = 6.dp, color = Color(0xFF191919))
            MyDrawableItem("화면 설정") {navController.navigate("DarkModeSettingPage")}
            MyDrawableItem("환경 설정") {navController.navigate("notificationSettingPage")}
            MyDrawableItem("고객지원") {navController.navigate("SupportPage")}
            MyDrawableItem("업데이트 기록") {navController.navigate("UpdateHistoryPage")}

            LogoutBtn{isLogoutClicked = true} //todo logout 기능 만들기
            // ...other drawer items
        }

    }
    AnimatedVisibility(
        visible = isLogoutClicked,
        enter = slideInVertically(
            initialOffsetY = { 2000 },
            animationSpec = tween(durationMillis = 500)
        ),
        exit = slideOutVertically(
            targetOffsetY = { 2000 },
            animationSpec = tween(durationMillis = 500)
        )
    ){
        LogoutBox(
            isCancelClicked = { isLogoutClicked = false
                              navController.navigate("LoginPage")
                              navController.popBackStack("LoginPage",false)},
            isLogoutClicked = { isLogoutClicked = false
                navController.navigate("LoginPage")
                navController.popBackStack("LoginPage",false)}
        )
    }
}

@Composable
fun WriteFTB(navController: NavController,viewModel: HomeViewModel,writingViewModel: WritingViewModel) {


    FloatingActionButton(
        modifier = Modifier.padding(end = 25.dp, bottom = 25.dp),
        onClick = {
            navController.navigate("WritingPage")
            viewModel.initializeDetailEssay()
            viewModel.setStorageEssay(EssayApi.EssayItem())
            writingViewModel.isModifyClicked = false
                  },
        shape = RoundedCornerShape(100.dp),
        containerColor = if (isSystemInDarkTheme()) Color.White else Color.Gray
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
fun CustomTopAppBar(onClick: () -> Unit,onClickNotification : ()->Unit,isExistUnreadAlerts : Boolean) {
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
                painter = painterResource(id = img),
                contentDescription = "Notifications",
                tint = Color.White,
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
fun MyBottomNavigation(navController: NavController) {
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
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute)

                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size(32.dp),
                        tint = if (currentRoute == item.screenRoute) Color.White else Color(
                            0xFF686868
                        )
                    )
                })

        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MyProfile(item: UserInfo, onClick: () -> Unit) {
    LinkedOutTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(20.dp)
                .clickable { onClick() }
            , contentAlignment = Alignment.BottomCenter
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
                    Text(text = "43일째 링크드 아웃!", color = Color(0xFF686868))
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "go",
                    tint = Color(0xFF686868)
                )
            }
        }
    }

}

@Composable
fun LineChartExample(modifier: Modifier = Modifier, essayCounts: List<Int>) {
    val lineData = createLineData(essayCounts)
    // 현재 달을 가져옴
    val currentMonth = LocalDate.now().month.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault())


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
                xAxis.valueFormatter = IndexAxisValueFormatter(arrayOf("", currentMonth, "", "", "이번 주"))
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
fun MyLinkedOutBar(){
    val currentDate = LocalDate.now()
    val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))

    LinkedOutTheme {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(60.dp)){
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart){
                Text(text = "주간 링크드아웃 지수", fontSize = 10.sp, color = Color.White)
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd){
                Text(text = "$formattedDate 현재", fontSize = 10.sp, color = Color(0xFF686868))
            }
        }
    }

    
}

@Composable
fun MyDrawableItem(text : String , onClick: () -> Unit) {
    LinkedOutTheme {
        NavigationDrawerItem(
            modifier = Modifier.height(70.dp),
            label = {
                Text(text = text, color = Color(0xFF797979), fontWeight = FontWeight.SemiBold)
            },
            selected = false,
            onClick = { onClick() },
        )
    }

}

@Preview
@Composable
fun ShopDrawerItem() {
    LinkedOutTheme {
        NavigationDrawerItem(
            modifier = Modifier.height(80.dp),
            label = {
                Text(text = "상점", color = Color.White, fontWeight = FontWeight.SemiBold)
            },
            selected = false,
            onClick = { },
            badge = {
                Box(modifier = Modifier
                    .size(60.dp, 24.dp)
                    .background(Color(0xFF191919), shape = RoundedCornerShape(40)), contentAlignment = Alignment.Center){
                    Text(text = "  준비중  ", fontWeight = FontWeight.SemiBold, color = LinkedInColor, fontSize = 12.sp )

                }
            }

        )
    }

}

@Composable
fun LogoutBtn( isLogoutClicked :()-> Unit){
    LinkedOutTheme {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 70.dp, end = 20.dp), contentAlignment = Alignment.BottomEnd){
            Box(
                modifier = Modifier
                    .size(80.dp, 36.dp)
                    .background(Color(0xFF191919), shape = RoundedCornerShape(20))
                    .clickable { isLogoutClicked() },
                contentAlignment = Alignment.Center
            ){
                Text(text = "로그아웃", fontSize = 12.sp, color = Color(0xFF606060))
            }
        }
    }

}

@Composable
fun LogoutBox( isCancelClicked: () ->Unit, isLogoutClicked: () -> Unit){
    LinkedOutTheme {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp)
            .background(Color.Black.copy(0.4f)))
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF121212), shape = RoundedCornerShape(10))
                .height(243.dp), contentAlignment = Alignment.Center){
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
                            Text(text = "취소", color = Color.Black,fontWeight = FontWeight.SemiBold)

                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF616FED)),
                            modifier = Modifier
                                .weight(1f)
                                .height(61.dp),
                            onClick = { isLogoutClicked()
                                /*TODO*/ },
                            shape = RoundedCornerShape(20)
                        ) {
                            Text(text = "로그아웃", color = Color.Black, fontWeight = FontWeight.SemiBold)

                        }
                    }

                }
            }
        }
    }

}

@Composable
fun TutorialPage(isCloseClicked: () -> Unit,isSkipClicked : ()->Unit){
    val pagerstate = rememberPagerState { 4 }
    val coroutineScope = rememberCoroutineScope()
    LinkedOutTheme {
        HorizontalPager(state = pagerstate, modifier = Modifier.fillMaxSize()) { page ->
            when (page) {
                0 -> Tutorial_1()


                1 ->
                    Tutorial_2(draggedToLeft = {
                        coroutineScope.launch {
                            pagerstate.animateScrollToPage(0,
                                animationSpec = tween(
                                    durationMillis = 600,
                                    easing = FastOutSlowInEasing
                                )
                            )

                        }
                    }, isTutorial3Clicked = {
                        coroutineScope.launch {
                            pagerstate.animateScrollToPage(2,
                                animationSpec = tween(
                                    durationMillis = 600,
                                    easing = FastOutSlowInEasing
                                )
                            )

                        }
                    })

                2 ->
                    Tutorial_3(draggedToLeft = {
                        coroutineScope.launch {
                            pagerstate.animateScrollToPage(1,
                                animationSpec = tween(
                                    durationMillis = 600,
                                    easing = FastOutSlowInEasing
                                )
                            )

                        }
                    },isCompleteClicked =
                    {
                        coroutineScope.launch {
                            pagerstate.animateScrollToPage(3,
                                animationSpec = tween(
                                    durationMillis = 600,
                                    easing = FastOutSlowInEasing
                                ))

                        } //왼쪽드래그 -> 1페이지로이동
                    })



                3 ->
                    Tutorial_4(){
                        isCloseClicked()
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
                    modifier = Modifier.clickable { isSkipClicked() },
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

        if (pagerstate.currentPage != 3){
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
}
    


@OptIn(ExperimentalGlideComposeApi::class)
@Preview
@Composable
fun Tutorial_1(){
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp), contentAlignment = Alignment.Center){
        GlideImage(model = R.drawable.tutorial_bottomicons, contentDescription = "tutorial_bottomicons")
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Tutorial_2(isTutorial3Clicked : ()->Unit, draggedToLeft: () -> Unit){
    var isClicked by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Box{
        Box(modifier = Modifier.fillMaxSize()){
            GlideImage(
                model = R.drawable.tutorial_2,
                contentDescription = "home_img",
                modifier = Modifier
                    .fillMaxSize()
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
                    }
                    .clickable { isClicked = true },
                contentScale = ContentScale.FillWidth
            )
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(start = 165.dp, bottom = 130.dp), contentAlignment = Alignment.Center){
            GlideImage(
                model = TUTORIAL_BULB,
                contentDescription = "bulb_img",
                modifier = Modifier
                    .size(80.dp)
                    .clickable { isClicked = true }
            )

        }
        AnimatedVisibility(visible = isClicked,
            enter = slideInVertically(
                initialOffsetY = { 2000 },
                animationSpec = tween(durationMillis = 500)
            ),
            exit = slideOutVertically(
                targetOffsetY = { 2000 },
                animationSpec = tween(durationMillis = 500)
            )) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Tutorial_3(isCompleteClicked : () -> Unit,draggedToLeft:()->Unit){
    var isClicked by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    LinkedOutTheme {
        if (!isClicked){
            Box(
                modifier = Modifier
                    .fillMaxSize()
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
                    }
                    .padding(bottom = 112.dp, end = 15.dp),
                contentAlignment = Alignment.BottomEnd
            ){
                TutorialActionBtn{isClicked = true}
            }
        }

        AnimatedVisibility(visible = isClicked,
            enter = slideInVertically(
                initialOffsetY = { 2000 },
                animationSpec = tween(durationMillis = 500)
            ),
            exit = slideOutVertically(
                targetOffsetY = { 2000 },
                animationSpec = tween(durationMillis = 500)
            )) {
            Box{
                Box(modifier = Modifier.fillMaxSize()){
                    GlideImage(
                        model = R.drawable.tutorial_4,
                        contentDescription = "home_img",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillWidth
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 65.dp, end = 20.dp),
                    contentAlignment = Alignment.TopEnd
                ){
                    Text(text = "완료", color = Color.White, modifier = Modifier.clickable {
                        isCompleteClicked()

                    })
                }
            }
        }
    }



}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Tutorial_4(isCloseClicked : ()->Unit){
    LinkedOutTheme {
        Box{
            Box(modifier = Modifier.fillMaxSize()){
                GlideImage(
                    model = R.drawable.tutorial_5,
                    contentDescription = "home_img",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 45.dp, end = 20.dp)
                    .clickable { },
                contentAlignment = Alignment.TopEnd
            ){
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "close",
                    tint = Color.White,
                    modifier = Modifier.clickable { isCloseClicked()}
                )
            }
        }
    }


}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TutorialActionBtn(modifier: Modifier = Modifier,isFTBClicked : ()->Unit){
    Box(modifier){
        FloatingActionButton(
            modifier = Modifier.padding(end = 25.dp, bottom = 25.dp),
            onClick = {isFTBClicked()},
            shape = RoundedCornerShape(100.dp),
            containerColor = if (isSystemInDarkTheme()) Color.White else Color.Gray
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ftb_edit),
                contentDescription = "edit",
                modifier = Modifier.size(20.dp),
                tint = Color.Black
            )
        }
        Box(Modifier.offset (x = (-10).dp,y= (-10).dp)){
            GlideImage(
                model = R.drawable.tutorial_circle,
                contentDescription = "circle",
                modifier = Modifier.size(75.dp)
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GeulRoquis(isHoldClicked : ()-> Unit, isAcceptClicked : () -> Unit, viewModel: HomeViewModel){
    LinkedOutTheme {
        Box(modifier = Modifier
            .background(color = Color(0xF2121212), shape = RoundedCornerShape(4))
            .size(281.dp, 411.dp)){
            GlideImage(
                model = R.drawable.geulroquis_background,
                contentDescription = "linkedout logo",
                contentScale = ContentScale.Crop
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(31.dp))
                Text(text = "오늘의 글로키가 도착했어요!", color = Color.White, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "2024-07-24 GeulRoquis", color = Color.White, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(20.dp))

                Log.d("글로키 링크", viewModel.geulRoquisUrl)
                if (viewModel.geulRoquisUrl.isEmpty() || !viewModel.geulRoquisUrl.startsWith("https")){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = LinkedInColor)
                    }
                }
                else{
                    GlideImage(
                        model = viewModel.geulRoquisUrl,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(260.dp)
                            .fillMaxWidth()
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "보류", color = Color.White, fontSize = 16.sp, modifier = Modifier
                        .clickable { isHoldClicked() }
                        .weight(1f), textAlign = TextAlign.Center)
                    VerticalDivider(color = Color(0xFF292929))
                    Text(text = "수락", color = Color.White, fontSize = 16.sp,modifier = Modifier
                        .clickable { isAcceptClicked() }
                        .weight(1f),textAlign = TextAlign.Center)

                }

            }
        }
    }

}


