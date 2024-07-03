package com.echoist.linkedout.page.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.echoist.linkedout.page.settings.SettingTopAppBar
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme

@Composable
fun UpdateHistoryPage(navController : NavController){
    LinkedOutTheme {
        Scaffold(topBar = {
            SettingTopAppBar("업데이트 기록",navController)
        }) {

            Column(
                Modifier
                    .padding(it)
                    .verticalScroll(rememberScrollState())) {
//                if(/*viewModel.updateHistory.isEmpty()todo*/){
//                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
//                        Text(
//                            text = "업데이트 기록이 없습니다.",
//                            style = TextStyle(
//                                fontSize = 16.sp,
//                                lineHeight = 25.6.sp,
//                                fontWeight = FontWeight(400),
//                                color = Color(0xFF888888),
//                            )
//                        )
//                    }
//                }
            
                UpdateHistoryBox()
                UpdateHistoryBox()
                UpdateHistoryBox()

            }
        }
    }
}

@Composable
fun UpdateTitleBox(){
    Box (
        Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(LinkedInColor, shape = RoundedCornerShape(10))){
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(start = 23.dp), contentAlignment = Alignment.CenterStart){
            Text(text = "2024년 업데이트", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(end = 20.dp), contentAlignment = Alignment.CenterEnd){
            Text(text = "2024.09.27", fontSize = 12.sp)
        }
    }
}

@Preview
@Composable
fun UpdateHistoryBox(){

        Column(
            Modifier
                .background(Color(0xFF0E0E0E), shape = RoundedCornerShape(10))
                .fillMaxWidth()) {
            UpdateTitleBox()
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "•  ddd\n•  dd\n•  dd\n•  dd",lineHeight = 22.4.sp, color = Color(0xFF888888), modifier = Modifier.padding(start = 23.dp))
            Spacer(modifier = Modifier.height(20.dp))

        }

}