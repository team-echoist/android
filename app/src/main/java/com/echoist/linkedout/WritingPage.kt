package com.echoist.linkedout

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.echoist.linkedout.ui.theme.LinkedOutTheme

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@PreviewScreenSizes
@Composable
fun WritingPage(){
    LinkedOutTheme {
        Scaffold(
            bottomBar = {  },
            topBar = { },
            content = {
                Column(modifier = Modifier.padding(it)) {

                }
            }
        )
    }

}
@Preview
@Composable
fun preveiw(){
    WritingPageTopAppBar(navController = rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WritingPageTopAppBar(navController: NavController) {
    val textState = remember { mutableStateOf(TextFieldValue()) }
    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = textState.value,
                    onValueChange = { textState.value = it },
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }
                },
            colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
        navigationIcon = {
            Text(text = "취소", color = Color(0xFF686868), modifier = Modifier.padding(start = 20.dp), fontSize = 16.sp)
        },
        actions = {
            Text(text = "완료", color = Color.White, modifier = Modifier.padding(end = 20.dp), fontSize = 16.sp)

        },
    )

}