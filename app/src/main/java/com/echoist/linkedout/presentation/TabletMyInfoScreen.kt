package com.echoist.linkedout.presentation

import android.content.ContentValues
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberStandardBottomSheetState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.page.home.MyBottomNavigation
import com.echoist.linkedout.page.settings.BadgeDescriptionBox
import com.echoist.linkedout.page.settings.LinkedOutBadgeGrid
import com.echoist.linkedout.page.settings.MembershipSettingBar
import com.echoist.linkedout.page.settings.ModifyMyProfileBottomSheet
import com.echoist.linkedout.page.settings.MySettings
import com.echoist.linkedout.page.settings.RecentEssayList
import com.echoist.linkedout.page.settings.SelectProfileIconBottomSheet
import com.echoist.linkedout.page.settings.SettingBar
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.SettingsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabletMyInfoRoute(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    var isApiFinished by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = isApiFinished) {
        viewModel.requestMyInfo()
        viewModel.readSimpleBadgeList()
        viewModel.getMyInfo()
        viewModel.readRecentEssays()
        isApiFinished = false
    }

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val bottomSheetState =
        rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)
    val scaffoldState = androidx.compose.material3.rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    BottomSheetScaffold(
        sheetContainerColor = Color(0xFF111111),
        scaffoldState = scaffoldState,
        sheetContent = {
            //이미지 수정시
            AnimatedVisibility(
                visible = viewModel.isClickedModifyImage,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearEasing
                    )
                ),
                exit = fadeOut(
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearEasing
                    )
                )
            ) {
                Log.d(ContentValues.TAG, "MyPage: ${viewModel.newProfile}")
                SelectProfileIconBottomSheet(viewModel)
            }
            //기본

            AnimatedVisibility(
                visible = !viewModel.isClickedModifyImage,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearEasing
                    )
                ),
                exit = fadeOut(
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearEasing
                    )
                )
            ) {
                ModifyMyProfileBottomSheet(
                    onClickComplete = {
                        viewModel.updateMyInfo(viewModel.newProfile, navController)
                    },
                    onClickCancel = {
                        scope.launch {
                            bottomSheetState.hide()

                        }
                    }, viewModel
                )
            }


        },
        sheetPeekHeight = 0.dp
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(scrollState)

        ) {
            Text(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                text = "MY",
                color = Color.White,
                modifier = Modifier
                    .padding(start = 20.dp, top = 10.dp)
                    .safeDrawingPadding()
            )
            MySettings(item = viewModel.getMyInfo()) {
                Log.d(ContentValues.TAG, "MyPage: ${viewModel.getMyInfo()}")
                scope.launch {
                    bottomSheetState.expand()
                }
            }
            if (viewModel.isApiFinished) {
                SettingBar("링크드아웃 배지") { viewModel.readDetailBadgeList(navController) }
                LinkedOutBadgeGrid(viewModel)
                SettingBar("최근 본 글") { navController.navigate("RecentViewedEssayPage") }
                RecentEssayList(
                    itemList = viewModel.getRecentViewedEssayList(),
                    navController
                )
                MembershipSettingBar("멤버십 관리") {}
                SettingBar("계정 관리") { navController.navigate("AccountPage") }
            }
        }

        if (viewModel.isBadgeClicked) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                BadgeDescriptionBox(viewModel.badgeBoxItem!!, viewModel)
            }
        }
    }
}