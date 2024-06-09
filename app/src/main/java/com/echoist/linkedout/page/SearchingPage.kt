package com.echoist.linkedout.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.components.EssayListItem
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.SearchingViewModel
import kotlinx.coroutines.launch


@Composable
fun SearchingPage(drawerState: DrawerState,navController: NavController){
    val pagerState = rememberPagerState { 2 }

    val scope = rememberCoroutineScope()

    val searchingViewModel : SearchingViewModel = hiltViewModel()
    LinkedOutTheme {

        Scaffold(
            topBar = {
                Column {
                    SearchingBar(
                        viewModel = searchingViewModel,
                        onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        },
                        drawerState = drawerState
                    )
                    SearchingChips(pagerState = pagerState)
                }
            },
        ) {

            //todo 검색할 리스트 뷰모델에서 재정의필요.
            Column(Modifier.padding(it)) {
                Spacer(modifier = Modifier.height(20.dp))
                SearchingPager(pagerState = pagerState,searchingViewModel = searchingViewModel,navController = navController)
            }
        }
    }



}

@Composable
fun SearchingPager(pagerState: PagerState,searchingViewModel: SearchingViewModel,navController: NavController){
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0-> LazyColumn() {
                items(items = searchingViewModel.previousEssayList) { it -> //랜덤리스트 말고 수정할것. 그사람의 리스트로
                    EssayListItem(
                        item = it,
                        viewModel = searchingViewModel,
                        navController = navController
                    )
                }
            }
            1-> LazyColumn() {
                items(items = searchingViewModel.previousEssayList) { it -> //랜덤리스트 말고 수정할것. 그사람의 리스트로
                    EssayListItem(
                        item = it,
                        viewModel = searchingViewModel,
                        navController = navController)
                }
            }
        }
    }
}

@Composable
fun SearchingBar(viewModel: SearchingViewModel, onClick: () -> Unit, drawerState: DrawerState){

    val keyboardController = LocalSoftwareKeyboardController.current
    if (drawerState.isOpen) keyboardController?.show() else keyboardController?.hide()

    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp), verticalAlignment = Alignment.CenterVertically){
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
            contentDescription = "back",
            tint = Color.White,
            modifier = Modifier.clickable {
                keyboardController?.hide()
                onClick()
            }
        )
        Spacer(modifier = Modifier.width(10.dp))
        TextField(
            shape = RoundedCornerShape(30),
            modifier = Modifier
                .weight(1f)
                .height(55.dp),
            value = viewModel.searchingText,
            onValueChange = {
                viewModel.searchingText = it
            },
            placeholder = { Text(text = "검색", color = Color(0xFF686868)) },
            singleLine = true,
            trailingIcon = { if (viewModel.searchingText.isNotEmpty()) Icon(
                imageVector = Icons.Default.Cancel,
                contentDescription = "cancel",
                modifier = Modifier.clickable {
                    viewModel.searchingText = ""
                }
            )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF222222),
                unfocusedContainerColor = Color(0xFF222222),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
    }
}


@Composable
fun SearchingChips(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    val color = Color.White

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(26.dp)
    ) {

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 27.dp), thickness = 1.dp, color = Color(0xFF686868)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 17.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(60.dp, 40.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    color = color,
                    fontSize = 14.sp,
                    text = "글",// 색상을 먼저 적용합니다
                    modifier = Modifier.clickable {

                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0)
                        }

                    }
                )

                Spacer(modifier = Modifier.height(4.dp))
                if (pagerState.currentPage == 0) {
                    HorizontalDivider(
                        modifier = Modifier
                            .width(65.dp),
                        color = color,
                        thickness = 3.dp
                    )
                }

            }
            Column(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(60.dp, 40.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    color = color,
                    fontSize = 14.sp,
                    text = "사용자",// 색상을 먼저 적용합니다
                    modifier = Modifier.clickable {

                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }

                    } // Modifier.clickable을 마지막에 적용합니다
                )

                Spacer(modifier = Modifier.height(4.dp))
                if (pagerState.currentPage == 1) {
                    HorizontalDivider(
                        modifier = Modifier
                            .width(65.dp),
                        color = color,
                        thickness = 3.dp
                    )
                }
            }

        }

    }
}


