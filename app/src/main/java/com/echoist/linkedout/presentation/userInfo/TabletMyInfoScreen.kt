package com.echoist.linkedout.presentation.userInfo

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.presentation.util.Routes
import com.echoist.linkedout.presentation.util.TYPE_RECOMMEND
import com.echoist.linkedout.presentation.userInfo.BadgeDescriptionBox
import com.echoist.linkedout.presentation.userInfo.LinkedOutBadgeGrid
import com.echoist.linkedout.presentation.userInfo.MembershipSettingBar
import com.echoist.linkedout.presentation.userInfo.ModifyMyProfileBottomSheet
import com.echoist.linkedout.presentation.userInfo.MySettings
import com.echoist.linkedout.presentation.userInfo.RecentEssayList
import com.echoist.linkedout.presentation.userInfo.SelectProfileIconBottomSheet
import com.echoist.linkedout.presentation.userInfo.SettingBar
import com.echoist.linkedout.presentation.community.CommunityViewModel
import com.echoist.linkedout.presentation.essay.EssayViewModel
import com.echoist.linkedout.presentation.userInfo.MyPageViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabletMyInfoRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MyPageViewModel = hiltViewModel(),
    essayViewModel: EssayViewModel = hiltViewModel(),
    communityViewModel: CommunityViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val userInfo by viewModel.userProfile.collectAsState()
    val badgeList by viewModel.badgeList.collectAsState()
    val recentEssayList by essayViewModel.recentEssayList.collectAsState()

    val bottomSheetState =
        rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)
    val scaffoldState = androidx.compose.material3.rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    LaunchedEffect(true) {
        viewModel.requestMyInfo()
        viewModel.readSimpleBadgeList()
        viewModel.getMyInfo()
    }

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
                SelectProfileIconBottomSheet(
                    uploadImage = { uri ->
                        scope.launch {
                            viewModel.uploadImage(uri, context)
                        }
                    },
                    onClickImage = { imageUrl ->
                        viewModel.onImageChange(imageUrl)
                        viewModel.isClickedModifyImage = false
                    },
                    onClickBack = { viewModel.isClickedModifyImage = false }
                )
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
            MySettings(userInfo) {
                scope.launch {
                    bottomSheetState.expand()
                }
            }
            SettingBar("링크드아웃 배지") { navController.navigate(Routes.BadgePage) }
            LinkedOutBadgeGrid(badgeList) {
                viewModel.badgeBoxItem = it
                viewModel.isBadgeClicked = true
            }
            SettingBar("최근 본 글") { navController.navigate(Routes.RecentViewedEssayPage) }
            RecentEssayList(recentEssayList) {
                communityViewModel.readDetailRecentEssay(
                    it,
                    navController,
                    TYPE_RECOMMEND
                )
            }
            MembershipSettingBar("멤버십 관리") {}
            SettingBar("계정 관리") { navController.navigate(Routes.AccountPage) }
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