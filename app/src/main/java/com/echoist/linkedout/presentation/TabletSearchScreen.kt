package com.echoist.linkedout.presentation

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.echoist.linkedout.TYPE_RECOMMEND
import com.echoist.linkedout.page.community.SearchList
import com.echoist.linkedout.page.community.SearchingBar
import com.echoist.linkedout.page.community.SearchingChips
import com.echoist.linkedout.page.community.SearchingPager
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.SearchingViewModel
import com.echoist.linkedout.viewModels.UiState
import kotlinx.coroutines.launch

@Composable
fun TabletSearchScreen(
    navController: NavController,
    viewModel: SearchingViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState { 2 }
    val searchUiState by viewModel.uiState.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        viewModel.clearUiState()
        searchQuery = ""
    }

    Column {
        val keyboardController = LocalSoftwareKeyboardController.current

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "back",
                tint = Color.White,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        keyboardController?.hide()
                        navController.popBackStack()
                    }
            )
            TextField(
                shape = RoundedCornerShape(42),
                modifier = Modifier
                    .weight(1f)
                    .height(55.dp),
                value = viewModel.searchingText,
                onValueChange = {
                    viewModel.searchingText = it
                },
                placeholder = {
                    Text(
                        text = "검색어를 입력하세요",
                        color = Color(0xFF686868),
                        fontSize = 16.sp
                    )
                },
                singleLine = true,
                trailingIcon = {
                    if (viewModel.searchingText.isNotEmpty()) Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "cancel",
                        modifier = Modifier.clickable {
                            viewModel.searchingText = ""
                        }
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { //검색 누를시 검색
                        viewModel.readSearchingEssays(viewModel.searchingText)
                    }
                ),
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

        SearchingChips(pagerState = pagerState)
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (searchUiState) {
                is UiState.Idle -> {}

                is UiState.Loading -> {
                    CircularProgressIndicator()
                }

                is UiState.Success -> {
                    SearchList(items = (searchUiState as UiState.Success).essays) {
                        viewModel.readDetailEssay(it.id!!, navController, TYPE_RECOMMEND)
                        viewModel.detailEssayBackStack.push(it)
                    }
                }

                is UiState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "알 수 없는 에러가 발생했습니다.\n 잠시 후 다시 시도해 주세요.",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Button(onClick = {
                            viewModel.readSearchingEssays(searchQuery)
                        }) {
                            Text(text = "재시도")
                        }
                    }
                }
            }
        }
    }
}