package com.echoist.linkedout

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.LoginSuccessDialog
import com.echoist.linkedout.viewModels.SocialLoginViewModel
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.navercorp.nid.NaverIdLoginSDK

class MainActivity : ComponentActivity() {
    override fun onStart() {
        super.onStart()
        //카카오 sdk 초기화
        KakaoSdk.init(this, BuildConfig.kakao_native_app_key)
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        setContent {
            val keyHash = Utility.getKeyHash(this)
            Log.d("Hash", keyHash)
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "screen1") {
                composable("screen1") {
                    AppPreview(navController = navController)
                }
                composable("screen2") {
                    Greeting()
                }
                composable("screen3") {
                    GoogleLoginBtn(navController)

                }
            }

        }
    }
}

//구글로그인 버튼
@Composable
fun GoogleLoginBtn(navController: NavController) {
    val viewModel: SocialLoginViewModel = viewModel()

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        viewModel.handleGoogleLogin(result.data, navController)
    }
    Icon(
        painter = painterResource(id = R.drawable.googleloginbtn4x),
        contentDescription = "naver Login btn",
        modifier = Modifier
            .size(40.dp)
            .clickable { viewModel.signInWithGoogle(launcher, context) },
        tint = Color.Unspecified
    )

    if (viewModel.googleLoginstate.value) {
        LoginSuccessDialog("google 로그인성공", viewModel.googleLoginstate)
    }
}

@Composable
fun KakaoLoginBtn(navController: NavController) {
    val viewModel: SocialLoginViewModel = viewModel()
    val context = LocalContext.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Icon(
            painter = painterResource(id = R.drawable.kakaologinbtn4x),
            contentDescription = "naver Login btn",
            modifier = Modifier
                .size(40.dp)
                .clickable { viewModel.handleKaKaoLogin(context) },
            tint = Color.Unspecified
        )


    }
    if (viewModel.kakaoLoginstate.value) {
        LoginSuccessDialog("kakao 로그인성공", viewModel.kakaoLoginstate)
    }

}

@Composable
fun NaverLoginBtn(navController: NavController) {
    val viewModel: SocialLoginViewModel = viewModel()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.handleNaverLoginResult(result)
    }
    viewModel.initializeNaverLogin(context)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = R.drawable.naverloginbtn4x),
            contentDescription = "naver Login btn",
            modifier = Modifier
                .size(40.dp)
                .clickable { NaverIdLoginSDK.authenticate(context, launcher) },
            tint = Color.Unspecified
        )


    }
    if (viewModel.naverLoginstate.value) {
        LoginSuccessDialog("naver 로그인성공", viewModel.naverLoginstate)
    }

}

@Composable
fun AppleLoginBtn(navController: NavController) {
    val viewModel: SocialLoginViewModel = viewModel()
    val context = LocalContext.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = R.drawable.appleloginbtn4x),
            contentDescription = "naver Login btn",
            modifier = Modifier
                .size(40.dp)
                .clickable { }, //애플로그인 로직 구현필요
                    tint = Color.Unspecified
                    )


                }
    if (viewModel.naverLoginstate.value) {
        LoginSuccessDialog("naver 로그인성공", viewModel.naverLoginstate)
    }

}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    val viewModel: SocialLoginViewModel = viewModel()
    Scaffold {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Hello ${viewModel.userName}!",
                modifier = modifier
            )
        }

    }

}

@Composable
fun AppPreview(navController: NavController) {

    LinkedOutTheme {
        Scaffold(
            content = {
                Column (modifier = Modifier
                    .padding(it)
                    .fillMaxSize()){

                        SocialLoginBar(navController)

                    }


            }
        )
    }
}

@Composable
fun SocialLoginBar(navController : NavController) {
    Row(
        modifier = Modifier
            .padding(25.dp)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GoogleLoginBtn(navController = navController)
        Spacer(modifier = Modifier.width(25.dp))
        KakaoLoginBtn(navController = navController)
        Spacer(modifier = Modifier.width(25.dp))
        NaverLoginBtn(navController = navController)
        Spacer(modifier = Modifier.width(25.dp))
        AppleLoginBtn(navController = navController)
    }
}

