package com.echoist.linkedout.page.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.DensitySmall
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.data.BottomNavItem
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.HomeViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HomePage(navController: NavController,viewModel: HomeViewModel) {

    LaunchedEffect(key1 = Unit) {
        viewModel.readMyInfo()

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
                    },{navController.navigate("NotificationPage")})
                },
                bottomBar = { MyBottomNavigation(navController) },
                floatingActionButton = { WriteFTB(navController,viewModel) },
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
            LineChartExample()
            HorizontalDivider(thickness = 6.dp, color = Color(0xFF191919))
            ShopDrawerItem()
            HorizontalDivider(thickness = 6.dp, color = Color(0xFF191919))
            MyDrawableItem("화면 설정") {navController.navigate("DarkModeSettingPage")}
            MyDrawableItem("알림 설정") {navController.navigate("notificationSettingPage")}
            MyDrawableItem("고객지원") {navController.navigate("SupportPage")}
            MyDrawableItem("업데이트 기록") {navController.navigate("UpdateHistoryPage")}

            LogoutBtn{isLogoutClicked = true} //todo logout 기능 만들기
            // ...other drawer items
        }

    }
    AnimatedVisibility(
        visible = isLogoutClicked,
        enter = fadeIn(animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)),
        exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
    ){
        LogoutBox(
            isCancelClicked = { isLogoutClicked = false },
            isLogoutClicked = { isLogoutClicked = false }
        ) //todo 수정필요
    }
}

@Composable
fun WriteFTB(navController: NavController,viewModel: HomeViewModel) {


    FloatingActionButton(
        modifier = Modifier.padding(end = 25.dp, bottom = 25.dp),
        onClick = {
            navController.navigate("WritingPage")
            viewModel.initializeDetailEssay()
            viewModel.setStorageEssay(EssayApi.EssayItem())
                  },
        shape = RoundedCornerShape(100.dp),
        containerColor = if (isSystemInDarkTheme()) Color.White else Color.Gray
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ftb_edit),
            contentDescription = "edit",
            modifier = Modifier.size(20.dp),
            tint = if (isSystemInDarkTheme()) Color.Black else Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(onClick: () -> Unit,onClickNotification : ()->Unit) {
    TopAppBar(
        title = { }, colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
        navigationIcon = {
            Icon(
                tint = if (isSystemInDarkTheme()) Color.White else Color.Gray,
                imageVector = Icons.Default.DensitySmall, //todo 3줄짜리로 이미지 export필요
                contentDescription = "Menu",
                modifier = Modifier
                    .clickable { onClick() }
                    .padding(start = 20.dp)
                    .size(30.dp)
            )
        },
        actions = {
            Icon(
                Icons.Default.Notifications,
                contentDescription = "Notifications",
                modifier = Modifier
                    .clickable { onClickNotification() }
                    .padding(end = 20.dp)
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
    NavigationBar(containerColor = Color.Transparent) {
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(20.dp)
            .clickable { onClick() }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
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
            Column {
                Row {
                    Text(text = item.nickname!!, color = Color(0xFF616FED))
                    Text(text = " 아무개", color = Color.White)
                }
                Spacer(modifier = Modifier.height(7.dp))
                Text(text = "43일째 링크드 아웃!", color = Color(0xFF686868))
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "go",
                tint = Color(0xFF686868)
            )
        }
    }
}

@Preview
@Composable
fun LineChartExample(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lineData = createLineData()

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(horizontal = 20.dp),
        factory = { context ->
            LineChart(context).apply {
                data = lineData
                this.
                    // X축 설정
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.textColor = android.graphics.Color.BLUE // X축 레이블 텍스트 색상 설정
                xAxis.valueFormatter = IndexAxisValueFormatter(arrayOf("", "5월", "", "", "이번 주"))
                xAxis.granularity = 1f
                xAxis.labelCount = 5
                xAxis.setDrawAxisLine(false)

                // 오른쪽 Y축 비활성화
                axisRight.isEnabled = false
                //왼쪽 y축 비활성화
                axisLeft.isEnabled = false

                // 차트 설명 비활성화
                description.isEnabled = false


                // 애니메이션 추가
                animateX(1000)
            }
        },
        update = { chart ->
            chart.data = lineData
            chart.invalidate() // 차트 갱신
        }
    )
}

fun createLineData(): LineData {
    val entries = listOf(
        Entry(0f, 1f),
        Entry(1f, 5f),
        Entry(2f, 10f),
        Entry(3f, 3f),
        Entry(4f, 7f)
    )

    val dataSet = LineDataSet(entries,"").apply {
        color = android.graphics.Color.BLUE
        circleRadius = 8f
        valueTextColor = android.graphics.Color.BLACK
        setCircleColor(android.graphics.Color.BLUE)
        lineWidth = 4f
        circleHoleColor = android.graphics.Color.BLUE
        setDrawCircles(true)
        setDrawValues(false)

        setDrawIcons(false)

    }


    return LineData(dataSet)
}

//class YAxisValueFormatter(private val lineData: LineData) : com.github.mikephil.charting.formatter.ValueFormatter() {
//    override fun getFormattedValue(value: Float): String {
//        val yValues = lineData.dataSets[0].values.map { it.y }
//        val max = yValues.maxOrNull() ?: 0f
//        return if (value == max || value == yValues.first()) {
//            value.toString()
//        } else {
//            ""
//        }
//    }
//}

@Composable
fun MyLinkedOutBar(){
    val currentDate = LocalDate.now()
    val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))

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

@Composable
fun MyDrawableItem(text : String , onClick: () -> Unit) {
    NavigationDrawerItem(
        modifier = Modifier.height(70.dp),
        label = {
            Text(text = text, color = Color(0xFF797979), fontWeight = FontWeight.SemiBold)
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
            Box(modifier = Modifier
                .size(60.dp, 24.dp)
                .background(Color(0xFF191919), shape = RoundedCornerShape(40)), contentAlignment = Alignment.Center){
                Text(text = "  준비중  ", fontWeight = FontWeight.SemiBold, color = LinkedInColor, fontSize = 12.sp )

            }
        }

    )
}

@Composable
fun LogoutBtn( isLogoutClicked :()-> Unit){
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

@Composable
fun LogoutBox( isCancelClicked: () ->Unit, isLogoutClicked: () -> Unit){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black.copy(0.4f)))
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF121212), shape = RoundedCornerShape(20))
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
