package com.echoist.linkedout.presentation

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.page.community.SearchingBar
import com.echoist.linkedout.page.community.SearchingChips
import com.echoist.linkedout.page.community.SearchingPager
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.SearchingViewModel
import kotlinx.coroutines.launch

@Composable
fun TabletSearchScreen(
    navController: NavController,
    viewModel: SearchingViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()
    val searchingViewModel: SearchingViewModel = hiltViewModel()

    Column {
        val keyboardController = LocalSoftwareKeyboardController.current
        //if (drawerState.isOpen) keyboardController?.show() else keyboardController?.hide()

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
                        Log.d(ContentValues.TAG, "SearchingBar: ${viewModel.searchingText}")
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
        SearchingPager(
            pagerState = pagerState,
            searchingViewModel = searchingViewModel,
            navController = navController
        )
    }
}