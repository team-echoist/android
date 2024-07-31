package com.echoist.linkedout.page.home.legal_Notice

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.echoist.linkedout.page.settings.SettingTopAppBar
import com.echoist.linkedout.ui.theme.LinkedOutTheme

@Composable
fun PrivacyPolicyPage(navController : NavController){
    LinkedOutTheme {
        Scaffold(topBar = {
            SettingTopAppBar("개인정보처리방침",navController)
        }) {

            Column(
                Modifier
                    .padding(it)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()) {
                Text(text = "개인정보처리방침" )

            }
        }
    }
}