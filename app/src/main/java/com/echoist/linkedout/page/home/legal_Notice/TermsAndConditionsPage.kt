package com.echoist.linkedout.page.home.legal_Notice

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.echoist.linkedout.TERMS_POLICY_URL
import com.echoist.linkedout.page.settings.SettingTopAppBar
import com.echoist.linkedout.ui.theme.LinkedOutTheme

//이용약관 페이지
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun TermsAndConditionsPage(navController : NavController){
    LinkedOutTheme {
        Scaffold(topBar = {
            SettingTopAppBar("이용 약관",navController)
        }) {

            Column(
                Modifier
                    .padding(it)
                    .navigationBarsPadding()) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            webViewClient = WebViewClient()
                            settings.javaScriptEnabled = true
                        }
                    },
                    update = { webView ->
                        webView.loadUrl(TERMS_POLICY_URL)
                    }
                )
            }
        }
    }
}