package com.echoist.linkedout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
        KakaoSdk.init(this,BuildConfig.kakao_native_app_key)
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
fun GoogleLoginBtn( navController: NavController) {
    val viewModel : SocialLoginViewModel = viewModel()

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        viewModel.handleGoogleSignInResult(result.data, navController)
    }


        Button(
            onClick = {
                viewModel.signInWithGoogle(launcher,context)
            }
        ) {
            Text(text = "Google Sign In")
        }
        if (viewModel.googleLoginstate.value) {
            LoginSuccessDialog("google 로그인성공",viewModel.googleLoginstate)
        }
}



@Composable
fun KakaoLoginBtn(navController: NavController){
    val viewModel : SocialLoginViewModel = viewModel()
    val context = LocalContext.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { viewModel.handleKaKaoLogin(context) }) {
            Text(text = "Kakao Sign in")
        }
        Button(onClick = { viewModel.handleKaKaoLogout() }) {
            Text(text = "Kakao Sign out")
        }


    }
    if (viewModel.kakaoLoginstate.value) {
        LoginSuccessDialog("kakao 로그인성공",viewModel.kakaoLoginstate)
    }

}
@Composable
fun NaverLoginBtn(navController: NavController){
    val viewModel : SocialLoginViewModel = viewModel()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.handleNaverLoginResult(result)
    }
    viewModel.initializeNaverLogin(context)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { NaverIdLoginSDK.authenticate(context, launcher) }) {
            Text(text = "naver Sign in")
        }


    }
    if (viewModel.naverLoginstate.value) {
        LoginSuccessDialog("naver 로그인성공",viewModel.naverLoginstate)
    }

}

@Composable
fun Greeting( modifier: Modifier = Modifier) {
    val viewModel : SocialLoginViewModel = viewModel()
    Scaffold {
        Box(modifier= Modifier
            .padding(it)
            .fillMaxSize(),
            contentAlignment = Alignment.Center) {
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
        Scaffold (
            content = {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    GoogleLoginBtn(navController = navController)
                    KakaoLoginBtn(navController = navController)
                    NaverLoginBtn(navController = navController)

                }
            }
        )
    }
}
