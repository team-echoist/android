package com.echoist.linkedout.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.data.BottomNavItem
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.launch


@Preview
@Composable
fun PrevHomePage() {
    HomePage(navController = rememberNavController(), accessToken = "")
}

@Composable
fun HomePage(navController: NavController, accessToken: String) {

    val userItem by remember {
        mutableStateOf(
            UserInfo(
                id = 1,
                nickname = "구루브",
                profileImage = "http",
                password = "1234",
                gender = "male",
                birthDate = "0725"
            )
        )
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()



    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RectangleShape,
                drawerContainerColor = Color(0xE6141414)
            ) {
                MyProfile(item = userItem)
                HorizontalDivider(thickness = 6.dp, color = Color(0xFF191919))
                LineChartExample()
                HorizontalDivider(thickness = 6.dp, color = Color(0xFF191919))
                MyDrawableItem("상점") {}
                HorizontalDivider(thickness = 6.dp, color = Color(0xFF191919))
                MyDrawableItem("공지사항") {}
                HorizontalDivider(thickness = 1.dp, color = Color(0xFF191919))
                MyDrawableItem("로그인 정보") {}


                // ...other drawer items
            }
        }
    ) {
        LinkedOutTheme {
            Scaffold(
                topBar = {
                    CustomTopAppBar {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                },
                bottomBar = { MyBottomNavigation(navController) },
                floatingActionButton = { WriteFTB(navController, accessToken) },
                content = {
                    Column(modifier = Modifier.padding(it)) {

                    }
                }
            )
        }
    }
}


@Composable
fun WriteFTB(navController: NavController, accessToken: String) {


    FloatingActionButton(
        modifier = Modifier.padding(end = 25.dp, bottom = 25.dp),
        onClick = { navController.navigate("WritingPage/$accessToken") },
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
fun CustomTopAppBar(onClick: () -> Unit) {
    TopAppBar(
        title = { }, colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
        navigationIcon = {
            Icon(
                tint = if (isSystemInDarkTheme()) Color.White else Color.Gray,
                imageVector = Icons.Default.Menu,
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
                    .clickable {/* Todo 알림 표시 클릭 시 작동 기능 구현 필요 */ }
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
fun MyProfile(item: UserInfo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(168.dp)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
            GlideImage(
                model = R.drawable.bottom_nav_4,
                contentDescription = "profileImage",
                Modifier.size(80.dp)
            )
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
            .padding(start = 20.dp, end = 20.dp),
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

                // 왼쪽 Y축 설정
                axisLeft.apply {
                    // 최소값 설정
                    setDrawAxisLine(false)
                    setDrawGridLines(false) // 그리드 라인 비활성화
                    // 레이블 활성화
                    textColor = android.graphics.Color.WHITE // 레이블 색상 설정
                    textSize = 6f         // 레이블 텍스트 크기 설정

                }


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

    val dataSet = LineDataSet(entries, "Label").apply {
        color = android.graphics.Color.BLUE
        circleRadius = 10f
        valueTextColor = android.graphics.Color.BLACK
        setCircleColor(android.graphics.Color.BLUE)
        lineWidth = 4f
        circleHoleColor = android.graphics.Color.BLUE
        setDrawCircles(true)
        setDrawValues(false)

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
fun MyDrawableItem(text : String , onClick: () -> Unit) {
    NavigationDrawerItem(
        modifier = Modifier.height(80.dp),
        label = {
            Text(text = text, color = Color.White, fontWeight = FontWeight.SemiBold)
        },
        selected = false,
        onClick = { onClick() },
        badge = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "forward"
            )
        }
    )
}
