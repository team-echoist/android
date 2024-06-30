package com.echoist.linkedout.page.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.echoist.linkedout.page.settings.ModifyBox
import com.echoist.linkedout.page.settings.SettingTopAppBar
import com.echoist.linkedout.ui.theme.LinkedOutTheme

@Composable
fun SupportPage(navController : NavController){
    LinkedOutTheme {
        Scaffold(topBar = {
            SettingTopAppBar("고객지원",navController)
        }) {

            Column(Modifier.padding(it)) {
                ModifyBox("링크드아웃 고개센터") {navController.navigate("LinkedOutSupportPage")}
                ModifyBox("공지사항") {}
                ModifyBox("법적 고지") {}

            }
        }
    }
}