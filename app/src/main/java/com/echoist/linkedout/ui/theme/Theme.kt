package com.echoist.linkedout.ui.theme

import android.os.Build
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

private val DarkColorScheme = darkColorScheme(
    primary = LinkedInColor,
    secondary = LinkedInColor,
    tertiary = Color.Black,
    background = Color.Black, // 검정색 배경
    surface = Color.Black, // 검정색 표면
    onPrimary = Color.White, // 주 색상에 대한 텍스트 색상
    onSecondary = Color.White, // 부 색상에 대한 텍스트 색상
    onTertiary = Color.White, // 3차 색상에 대한 텍스트 색상
    onBackground = Color.White, // 배경에 대한 텍스트 색상
    onSurface = Color.White // 표면에 대한 텍스트 색상
)

private val LightColorScheme = lightColorScheme(
    primary = LinkedInColor,
    secondary = LinkedInColor,
    tertiary = Pink40,
    background = Color.White, // 흰색 배경
    surface = Color.White, // 흰색 표면
    onPrimary = Color.Black, // 주 색상에 대한 텍스트 색상
    onSecondary = Color.Black, // 부 색상에 대한 텍스트 색상
    onTertiary = Color.Black, // 3차 색상에 대한 텍스트 색상
    onBackground = Color.Black, // 배경에 대한 텍스트 색상
    onSurface = Color.Black // 표면에 대한 텍스트 색상
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkedOutTheme(
    darkTheme: Boolean = true, //isSystemInDarkTheme(), //이 값 대신 SharedPreferencesUtil 을 사용
    // Dynamic color is available on Android 12+
    navController: NavController = rememberNavController(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    //val context = LocalContext.current
    //val dark : Boolean = SharedPreferencesUtil.getDisplayInfo(context) == DARK_MODE

    // 현재 백스택 상태를 관찰하여 상태 변경 시 리컴포지션을 트리거
    val backStackEntry = navController.currentBackStackEntryAsState().value
    // 백스택에서 바로 뒤의 항목 가져오기
    val previousBackStackEntry = backStackEntry?.let {
        navController.previousBackStackEntry
    }
    // 이전 목적지의 라우트 확인
    val previousRoute = previousBackStackEntry?.destination?.route

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
//    // 최상위에서 백키 동작 관리
//    BackHandler {
//        if (navController.previousBackStackEntry == null) {
//            Log.d("BackHandler", "백 스택이 비어 있습니다!")
//            // 추가적으로 원하는 동작 수행
//        } else {
//            navController.popBackStack() // 백 스택이 있다면 일반적으로 뒤로 가기
//        }
//    }
    CompositionLocalProvider(
        LocalRippleConfiguration provides null // 리플 효과 제거
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = TypographyPretendard,
            content = content
        )
    }

}