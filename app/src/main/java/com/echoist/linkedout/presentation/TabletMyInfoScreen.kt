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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.Routes
import com.echoist.linkedout.page.settings.BadgeDescriptionBox
import com.echoist.linkedout.page.settings.LinkedOutBadgeGrid
import com.echoist.linkedout.page.settings.MembershipSettingBar
import com.echoist.linkedout.page.settings.ModifyMyProfileBottomSheet
import com.echoist.linkedout.page.settings.MySettings
import com.echoist.linkedout.page.settings.RecentEssayList
import com.echoist.linkedout.page.settings.SelectProfileIconBottomSheet
import com.echoist.linkedout.page.settings.SettingBar
import com.echoist.linkedout.viewModels.MyPageViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabletMyInfoRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    var isApiFinished by remember {
        mutableStateOf(false)
    }

    val badgeList by viewModel.badgeList.collectAsState()

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
                //SelectProfileIconBottomSheet(viewModel)
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
                        viewModel.updateMyInfo(navController)
                    },
                    onClickCancel = {
                        scope.launch {
                            bottomSheetState.hide()
                        }
                    },
                    onClickImageChange = {
                        viewModel.isClickedModifyImage = true
                    }
                )
            }


        },
        sheetPeekHeight = 0.dp
    ) {
        Column(
            modifier = modifier
                .verticalScroll(scrollState)

        ) {
            MySettings(item = viewModel.getMyInfo()) {
                Log.d(ContentValues.TAG, "MyPage: ${viewModel.getMyInfo()}")
                scope.launch {
                    bottomSheetState.expand()
                }
            }
            if (viewModel.isApiFinished) {
                SettingBar("링크드아웃 배지") { navController.navigate(Routes.BadgePage) }
                LinkedOutBadgeGrid(badgeList) {
                    viewModel.badgeBoxItem = it
                    viewModel.isBadgeClicked = true
                }
                SettingBar("최근 본 글") { navController.navigate("RecentViewedEssayPage") }
                RecentEssayList(
                    itemList = viewModel.getRecentViewedEssayList(),
                    onClickEssayItem = { essayId ->
                        // 에세이 디테일로 넘어가는 동작
                    }
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
                BadgeDescriptionBox(viewModel.badgeBoxItem!!) {
                    viewModel.isBadgeClicked = false
                }
            }
        }
    }
}