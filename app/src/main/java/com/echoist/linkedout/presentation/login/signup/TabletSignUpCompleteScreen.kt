package com.echoist.linkedout.presentation.login.signup

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.presentation.home.HomeViewModel
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import kotlinx.coroutines.delay

@Composable
fun TabletSignUpCompleteRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToHome: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    val configuration = LocalConfiguration.current

    LaunchedEffect(key1 = true) {
        viewModel.requestMyInfo()
        delay(3000)
        isLoading = false
        navigateToHome()
    }
    val myInfo by viewModel.getMyInfo().collectAsState()

    TabletSignUpCompleteScreen(
        isLoading = isLoading,
        nickName = myInfo.nickname,
        horizontalPadding = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 350 else 100,
    )
}

@Composable
internal fun TabletSignUpCompleteScreen(
    isLoading: Boolean,
    nickName: String?,
    horizontalPadding: Int
) {
    LinkedOutTheme {
        Scaffold {
            Box(
                Modifier
                    .padding(it)
                    .padding(horizontal = horizontalPadding.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                SignUpCompletionContent(nickName = nickName)
                if (isLoading) {
                    LoadingIndicator()
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = LinkedInColor)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SignUpCompletionContent(nickName: String?) {
    Column(
        Modifier.padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        nickName?.let {
            SignUpCompletionMessage(nickName = it)
        }
        Spacer(modifier = Modifier.height(56.dp))
        GlideImage(
            model = R.drawable.login_table,
            modifier = Modifier.size(246.dp, 266.dp),
            contentDescription = "Login Table"
        )
        Spacer(modifier = Modifier.height(87.dp))
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 20.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        GlideImage(
            model = R.drawable.rightsidetext,
            contentDescription = "Right Side Text",
            Modifier
                .padding(bottom = 80.dp)
                .size(240.dp, 90.dp)
        )
    }
}

@Composable
fun SignUpCompletionMessage(nickName: String) {
    Text(
        text = "Welcome, ${nickName}님!",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )
}