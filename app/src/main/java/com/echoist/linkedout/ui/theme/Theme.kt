package com.echoist.linkedout.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

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


@Composable
fun LinkedOutTheme(
    darkTheme: Boolean = true, //isSystemInDarkTheme(), //이 값 대신 SharedPreferencesUtil 을 사용
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    //val context = LocalContext.current
    //val dark : Boolean = SharedPreferencesUtil.getDisplayInfo(context) == DARK_MODE

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = TypographyPretendard,
        content = content
    )
}