package com.echoist.linkedout.presentation.login.agreeofprovisions

import android.content.res.Configuration
import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.echoist.linkedout.R
import com.echoist.linkedout.presentation.TabletDrawableTopBar
import com.echoist.linkedout.presentation.login.signup.AgreementText
import com.echoist.linkedout.presentation.login.signup.SignUpViewModel
import com.echoist.linkedout.presentation.util.LOCATION_POLICY_URL
import com.echoist.linkedout.presentation.util.PRIVACY_POLICY_URL
import com.echoist.linkedout.presentation.util.Routes
import com.echoist.linkedout.presentation.util.TERMS_POLICY_URL
import com.echoist.linkedout.ui.theme.LinkedInColor

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TabletAgreeOfProvisionScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel(),
    onBackPress: () -> Unit
) {

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp // 화면의 높이를 DP 단위로 가져옴

    var webViewUrl by remember { mutableStateOf(PRIVACY_POLICY_URL) }
    var isClicked by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val isAgreeOfProvisions by viewModel.isAgreeOfProvisions.collectAsState()

    val isPortrait =
        configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    LaunchedEffect(isAgreeOfProvisions) {
        if (isAgreeOfProvisions) {
            navController.navigate(Routes.SignUpComplete)
            viewModel.onNavigated()
        }
    }

    Column {
        TabletDrawableTopBar(title = "", isBack = true) {
            onBackPress()
        }
        Box(
            Modifier
                .fillMaxSize()
                .padding(
                    top = if (isPortrait) 150.dp else 0.dp,
                    start = if (isPortrait) 150.dp else 350.dp,
                    end = if (isPortrait) 150.dp else 350.dp
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.background_logo_340),
                contentDescription = "background",
                modifier = Modifier
                    .size(350.dp)
                    .align(Alignment.TopEnd)
            )
            Column {
                Text(
                    text = "이용약관 동의",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 16.dp),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "회원 서비스 이용을 위해 회원가입을 해주세요.",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 16.dp, bottom = 32.dp),
                    color = Color.White
                )

                // Agreement options
                val agreementList = listOf(
                    viewModel.agreement_location,
                    viewModel.agreement_service,
                    viewModel.agreement_serviceAlert,
                    viewModel.agreement_teen,
                    viewModel.agreement_marketing,
                    viewModel.agreement_collection
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "check",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(30.dp)
                            .clickable {
                                // 리스트 중에 하나라도 true가 있으면 전부 true로, 아니면 전부 false로 설정
                                val shouldBeTrue = agreementList.any { !it }

                                viewModel.agreement_location = shouldBeTrue
                                viewModel.agreement_service = shouldBeTrue
                                viewModel.agreement_serviceAlert = shouldBeTrue
                                viewModel.agreement_teen = shouldBeTrue
                                viewModel.agreement_marketing = shouldBeTrue
                                viewModel.agreement_collection = shouldBeTrue
                            },
                        tint = if (agreementList.all { it }) {
                            LinkedInColor
                        } else Color(0xFF919191)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "전체 동의")
                }

                // Agreement items
                AgreementTextWithInfo(
                    text = "(필수)  서비스  이용약관  동의",
                    {
                        viewModel.agreement_service =
                            !viewModel.agreement_service
                    },
                    if (viewModel.agreement_service) LinkedInColor else Color(0xFF919191),
                    {
                        isClicked = true
                        webViewUrl = TERMS_POLICY_URL
                    }
                )
                AgreementTextWithInfo(
                    text = "(필수)  개인정보  수집  및  이용  동의",
                    {
                        viewModel.agreement_collection =
                            !viewModel.agreement_collection
                    },
                    if (viewModel.agreement_collection) LinkedInColor else Color(0xFF919191),
                    {
                        isClicked = true
                        webViewUrl = PRIVACY_POLICY_URL
                    }
                )
                AgreementText(
                    text = "(필수)   만  14세  이상입니다",
                    { viewModel.agreement_teen = !viewModel.agreement_teen },
                    if (viewModel.agreement_teen) LinkedInColor else Color(0xFF919191)
                )
                AgreementTextWithInfo(
                    text = "(선택)   위치 기반 서비스 이용 약관 동의",
                    {
                        viewModel.agreement_location =
                            !viewModel.agreement_location
                    },
                    if (viewModel.agreement_location) LinkedInColor else Color(0xFF919191),
                    {
                        isClicked = true
                        webViewUrl = LOCATION_POLICY_URL
                    }
                )
                AgreementText(
                    text = "(선택)   마케팅  정보  수신  동의",
                    {
                        viewModel.agreement_marketing =
                            !viewModel.agreement_marketing
                    },
                    if (viewModel.agreement_marketing) LinkedInColor else Color(0xFF919191)
                )
                AgreementText(
                    text = "(선택)   서비스 알림  수신  동의",
                    {
                        viewModel.agreement_serviceAlert =
                            !viewModel.agreement_serviceAlert
                    },
                    if (viewModel.agreement_serviceAlert) LinkedInColor else Color(
                        0xFF919191
                    )
                )

                Button(
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LinkedInColor,
                        disabledContainerColor = Color(0xFF868686)
                    ),
                    enabled = viewModel.agreement_service && viewModel.agreement_collection && viewModel.agreement_teen,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(start = 20.dp, end = 20.dp, top = 43.dp),
                    onClick = {
                        viewModel.setAgreementOfSignUp(
                            viewModel.agreement_location,
                            viewModel.agreement_marketing,
                            viewModel.agreement_serviceAlert,
                            context
                        )
                    }
                ) {
                    Text(text = "확인", color = Color.Black)
                }
            }
        }
    }

    // WebView and loading animation
    AnimatedVisibility(
        visible = isClicked,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 300)
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = 300)
        ) + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent, shape = RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((0.95 * screenHeight).dp)
                    .navigationBarsPadding()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
            ) {
                // Loading spinner
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = LinkedInColor
                        )
                    }
                }
                // WebView
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(
                                    view: WebView?,
                                    url: String?,
                                    favicon: Bitmap?
                                ) {
                                    super.onPageStarted(view, url, favicon)
                                    isLoading =
                                        true // Show loading indicator when page starts loading
                                }

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    isLoading =
                                        false // Hide loading indicator when page finishes loading
                                }
                            }
                            settings.javaScriptEnabled = true
                        }
                    },
                    update = { webView ->
                        webView.loadUrl(webViewUrl)
                    }
                )
                // Close button
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Black,
                        modifier = Modifier
                            .padding(end = 10.dp, top = 10.dp)
                            .clickable { isClicked = false }
                    )
                }
            }
        }
    }
}