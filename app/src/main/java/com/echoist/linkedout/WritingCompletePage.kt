package com.echoist.linkedout

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.colintheshots.twain.MarkdownText
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.WritingViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview
@Composable
fun PreviewWritingPage2() {
    WritingCompletePage(navController = rememberNavController(), WritingViewModel())
}

@Composable
fun WritingCompletePage(navController: NavController, viewModel: WritingViewModel) {

    val scrollState = rememberScrollState()
    var isBottomSheetOpen = remember {
        mutableStateOf(true)
    }

    LinkedOutTheme {
        Scaffold(topBar = {
            CompleteAppBar(navController = navController)
        }, content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .verticalScroll(scrollState)
            )
            {

                CompleteTitle(viewModel = viewModel)
                CompleteContents(viewModel = viewModel)
                CompleteNickName()
                CompleteDate(viewModel = viewModel)
                if (isBottomSheetOpen.value){
                    BottomSheet(modifier = Modifier,closeSheet = { isBottomSheetOpen.value = false })
                }
                Button(onClick = { isBottomSheetOpen.value = true }) {
                    Text(text = "bottom sheet open")
                }

            }
        }
        )


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompleteAppBar(navController: NavController) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = { },
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "arrow back",
                tint = if (isSystemInDarkTheme()) Color(0xFF727070) else Color.Gray,
                modifier = Modifier

                    .padding(start = 20.dp)
                    .clickable {
                        navController.popBackStack()
                        Log.d("asdfadsf", "adsfadsf")
                    } //뒤로가기
            )
        },
        actions = {
            Text(
                text = "삭제",
                color = Color.Red,
                modifier = Modifier
                    .padding(end = 20.dp)
                    .clickable {
                        /* todo 삭제 구현 필요합니다. */
                    },
                fontSize = 16.sp
            )
        })
}

@Composable
fun CompleteTitle(viewModel: WritingViewModel) {
    Text(
        text = viewModel.title.value.text,
        fontSize = 20.sp,
        color = Color.White,
        modifier = Modifier.padding(start = 25.dp, bottom = 20.dp)
    )
}

@Composable
fun CompleteContents(viewModel: WritingViewModel) {
    MarkdownText(
        markdown = viewModel.content.value.text,
        fontSize = 16.sp,
        color = Color(0xFFB4B4B4),
        modifier = Modifier.padding(start = 25.dp, bottom = 42.dp)
    )
}

@Composable
fun CompleteNickName() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
        Text(
            text = "구루브",  /*todo 닉네임 구현 필요합니다. */
            fontSize = 12.sp,
            color = Color(0xFF686868),
            modifier = Modifier.padding(end = 25.dp, bottom = 8.dp)
        )
    }
}

@Composable
fun CompleteDate(viewModel: WritingViewModel) {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm")
    val formattedDateTime = currentDateTime.format(formatter)
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Text(
            text = formattedDateTime,
            fontSize = 12.sp,
            color = Color(0xFF686868),
            modifier = Modifier.padding(end = 25.dp)
        )
    }
}

//꼭 버튼으로만 다 닫고 올리고 해야하는가?
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(modifier: Modifier,closeSheet : ()->Unit){
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(onDismissRequest = { closeSheet()},sheetState = sheetState) {
        Column(modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(modifier = Modifier.size(211.dp,48.dp), painter = painterResource(id = R.drawable.text_bottomsheet), contentDescription = "고리를 풀어")
        }

    }
}














