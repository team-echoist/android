package com.echoist.linkedout.presentation.mobile.home.legal_Notice

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.echoist.linkedout.presentation.util.PRIVACY_POLICY_URL
import com.echoist.linkedout.presentation.mobile.settings.SettingTopAppBar

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PrivacyPolicyPage(navController : NavController){
    
        Scaffold(topBar = {
            SettingTopAppBar("개인정보처리방침",navController)
        }) {
            Box(modifier = Modifier.padding(it)){

            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                    }
                },
                update = { webView ->
                    webView.loadUrl(PRIVACY_POLICY_URL)
                }
            )

            }
        }
    }
