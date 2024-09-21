package com.echoist.linkedout.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.page.settings.MultiSelectDeleteList
import com.echoist.linkedout.page.settings.WithdrawalWarningBox
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.viewModels.MyPageViewModel
import com.echoist.linkedout.viewModels.UserInfoViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TabletDeleteAccountRoute(
    contentPadding: PaddingValues,
    viewModel: MyPageViewModel = hiltViewModel(),
    userInfoViewModel: UserInfoViewModel = hiltViewModel(),
    onWithdrawalSuccess: () -> Unit
) {
    val scrollState = rememberScrollState()

    var isWithdrawalClicked by remember { mutableStateOf(false) }
    val reasonList = listOf(
        "사용 빈도가 낮아서", "콘텐츠(글)의 질이 기대에 못 미쳐서", "앱의 기능 및 서비스가 불만족스러워서",
        "앱 사용 중에 자꾸 문제가 생겨서(버그, 오류 등)", "다른 서비스가 더 좋아서", "기타 문제"
    )
    val selectedItems = remember { mutableStateListOf<String>() }

    val onItemSelected: (String) -> Unit = { selectedItem ->
        if (selectedItems.contains(selectedItem)) {
            selectedItems.remove(selectedItem)
        } else {
            selectedItems.add(selectedItem)
        }
    }

    val withdrawalSuccess by viewModel.isWithdrawalSuccess.collectAsState()

    LaunchedEffect(withdrawalSuccess) {
        if (withdrawalSuccess) {
            onWithdrawalSuccess()
        }
    }

    Box(
        Modifier
            .padding(contentPadding)
            .fillMaxSize(), contentAlignment = Alignment.TopCenter
    ) {
        if (viewModel.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = LinkedInColor)
            }
        }

        Column(
            Modifier
                .fillMaxWidth(0.6f)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(42.dp))
            Text(text = "탈퇴 시 유의사항", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(10.dp))

            GlideImage(
                model = R.drawable.box_warn,
                contentDescription = "deleteWarning",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(274.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "탈퇴 사유", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "탈퇴하는 이유를 말씀해주세요. 링크드아웃 서비스 개선에 큰 도움이 될 것입니다. 이용해주셔서 감사합니다!",
                fontSize = 16.sp,
                color = Color(0xFF5D5D5D)
            )

            MultiSelectDeleteList(reasonList, selectedItems, onItemSelected)

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    isWithdrawalClicked = true
                    userInfoViewModel.logout()
                },
                enabled = !selectedItems.isEmpty(),
                shape = RoundedCornerShape(20),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp)
                    .height(61.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE43446),
                    disabledContainerColor = Color(0xFF868686),

                    )
            ) {
                Text(text = "탈퇴하기", color = Color.Black)
            }
        }

        AnimatedVisibility(
            visible = isWithdrawalClicked,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(0.7f)),
                contentAlignment = Alignment.BottomCenter
            ) {
                WithdrawalWarningBox(
                    isCancelClicked = { isWithdrawalClicked = false },
                    isWithdrawalClicked = {
                        viewModel.requestWithdrawal(selectedItems.toList())
                    }
                )
            }
        }
    }
}