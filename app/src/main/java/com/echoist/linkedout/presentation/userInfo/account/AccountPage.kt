package com.echoist.linkedout.presentation.userInfo.account

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.echoist.linkedout.data.dto.UserInfo
import com.echoist.linkedout.presentation.home.LogoutBox
import com.echoist.linkedout.presentation.userInfo.MyPageViewModel
import com.echoist.linkedout.presentation.util.Routes
import com.echoist.linkedout.presentation.util.navigateWithClearBackStack
import com.echoist.linkedout.ui.theme.LinkedInColor

@Composable
fun AccountPage(
    navController: NavController,
    viewModel: MyPageViewModel = hiltViewModel(),
    userInfoViewModel: UserInfoViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            SettingTopAppBar("계정 관리", navController)
        },
        content = {
            var isLogoutClicked by remember {
                mutableStateOf(false)
            }

            Column(
                Modifier
                    .verticalScroll(scrollState)
                    .padding(it)
            ) {
                Spacer(modifier = Modifier.height(42.dp))

                Text(
                    text = "로그인 정보",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                EmailBox(
                    { navController.navigate("ChangeEmailPage") },
                    viewModel.getMyInfo().email ?: "noEmail"
                )
                ModifyBox("비밀번호 변경") { navController.navigate("ChangePwPage") }



                ModifyBox("로그아웃") { isLogoutClicked = true }
                Spacer(modifier = Modifier.height(20.dp))
                ModifyBox("탈퇴하기") { navController.navigate("AccountWithdrawalPage") }
                Spacer(modifier = Modifier.height(37.dp))
                //Text(text = "소셜", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 20.dp))
//                    Spacer(modifier = Modifier.height(31.dp))
//
//                    SocialLoginBox(R.drawable.social_googlebtn,"구글",viewModel.getMyInfo())
//                    Spacer(modifier = Modifier.height(10.dp))
//                    SocialLoginBox(R.drawable.social_kakaobtn,"카카오톡",viewModel.getMyInfo())
//                    Spacer(modifier = Modifier.height(10.dp))
//                    SocialLoginBox(R.drawable.social_naverbtn,"네이버",viewModel.getMyInfo())
//                    Spacer(modifier = Modifier.height(10.dp))
//                    SocialLoginBox(R.drawable.social_applebtn,"애플",viewModel.getMyInfo())
                Spacer(modifier = Modifier.height(10.dp))


            }

            AnimatedVisibility(
                visible = isLogoutClicked,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    )
                ),
                exit = fadeOut(
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearEasing
                    )
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 10.dp)
                        .background(Color.Black.copy(0.7f)),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    LogoutBox(
                        isCancelClicked = { isLogoutClicked = false },
                        isLogoutClicked = {
                            isLogoutClicked = false
                            userInfoViewModel.logout()

                            navigateWithClearBackStack(navController, Routes.LoginPage)
                        }
                    )
                }
            }
        }
    )
}


@Composable
fun EmailBox(onClick: () -> Unit, email: String) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFF0E0E0E))
        .padding(horizontal = 20.dp)
        .clickable { onClick() }
        .height(70.dp)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "이메일 주소 변경", Modifier.weight(2f))
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = email,
                    fontSize = 12.sp,
                    color = Color(0xFF5D5D5D),
                    modifier = Modifier.weight(2f),
                    maxLines = 1,  // 한 줄로 제한
                    overflow = TextOverflow.Ellipsis
                )  // 넘칠 경우 ... 표시)
                Spacer(modifier = Modifier.width(5.dp))

                Icon(
                    modifier = Modifier
                        .weight(0.3f)
                        .size(20.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "arrowforward"
                )

            }
        }
    }
}

@Composable
fun ModifyBox(text: String, onClick: () -> Unit) {
    Box(modifier = Modifier
        .background(Color(0xFF0E0E0E))
        .padding(horizontal = 20.dp)
        .fillMaxWidth()
        .clickable { onClick() }
        .height(70.dp)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            Text(text = text, fontSize = 16.sp)
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "arrowforward",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingTopAppBar(text: String, navController: NavController) {
    // 현재 백스택 상태를 관찰하여 상태 변경 시 리컴포지션을 트리거
    val backStackEntry = navController.currentBackStackEntryAsState().value
    // 백스택에서 바로 뒤의 항목 가져오기
    val previousBackStackEntry = backStackEntry?.let {
        navController.previousBackStackEntry
    }
    // 이전 목적지의 라우트 확인
    val previousRoute = previousBackStackEntry?.destination?.route
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
        title = {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                color = Color.White
            )
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "arrow back",
                modifier = Modifier
                    .clickable {
                        if (previousRoute == Routes.InquiryScreen) {
                            navController.navigate("${Routes.Home}/200") {
                                // 기존의 모든 백스택을 제거하고 Home을 루트로 설정
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        } else {
                            navController.popBackStack()
                        }
                    }
                    .padding(start = 10.dp)
                    .size(30.dp),
                tint = Color.White
            )

        }
    )
}

@Composable
fun SocialLoginBox(imageResourceId: Int, socialType: String, userItem: UserInfo) {

//    when(userItem.oauthInfo){
//       google머시기 -> todo 유저의 로그인정보에 따른 계정연결과 연결해제 버튼,및 텍스트 색 변경필요
//    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(50.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
            Icon(
                painter = painterResource(id = imageResourceId),
                contentDescription = "naver Login btn",
                modifier = Modifier
                    .size(40.dp)
                    .clickable { },
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(25.dp))
            Text(text = socialType, fontSize = 16.sp)
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.height(36.dp),
                shape = RoundedCornerShape(30),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF191919))
            ) {
                Text(
                    text = "계정 연결",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = LinkedInColor
                )
            }
        }
    }
}