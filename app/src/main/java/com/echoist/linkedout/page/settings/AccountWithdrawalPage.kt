package com.echoist.linkedout.page.settings

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.page.community.ReportTextField
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.SettingsViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AccountWithdrawalPage(navController : NavController) {

    val scrollState = rememberScrollState()
    val viewModel: SettingsViewModel = hiltViewModel()

    var isWithdrawalClicked by remember { mutableStateOf(false) }
    val reasonList = listOf(
        "사용 빈도가 낮아서", "콘텐츠(글)의 질이 기대에 못 미쳐서", "앱의 기능 및 서비스가 불만족스러워서",
        "앱 사용 중에 자꾸 문제가 생겨서(버그, 오류 등)", "다른 서비스가 더 좋아서","기타 문제"
    )
    val selectedItems = remember { mutableStateListOf<String>() }

    LinkedOutTheme {
        Scaffold(
            topBar = {
                SettingTopAppBar("탈퇴하기",navController)
            },
            content = {
                if (viewModel.isLoading){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        CircularProgressIndicator(color = LinkedInColor)
                    }
                }
                Column(
                    Modifier
                        .verticalScroll(scrollState)
                        .padding(it)
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(42.dp))
                    Text(text = "탈퇴 시 유의사항", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(10.dp))

                    GlideImage(model = R.drawable.box_warn, contentDescription = "deleteWarning", modifier = Modifier
                        .fillMaxWidth()
                        .height(274.dp))
                    Spacer(modifier = Modifier.height(32.dp))

                    Text(text = "탈퇴 사유", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "탈퇴하는 이유를 말씀해주세요. 링크드아웃 서비스 개선에 큰 도움이 될 것입니다. 이용해주셔서 감사합니다!", fontSize = 16.sp, color = Color(0xFF5D5D5D))



                    val onItemSelected: (String) -> Unit = { selectedItem ->
                        if (selectedItems.contains(selectedItem)) {
                            selectedItems.remove(selectedItem) // 이미 선택된 항목이면 제거
                        } else {
                            selectedItems.add(selectedItem) // 선택되지 않은 항목이면 추가
                        }
                    }


                    MultiSelectDeleteList(reasonList,selectedItems,onItemSelected)
                    Spacer(modifier = Modifier.height(32.dp))

                    var pw by remember { mutableStateOf("") }
                    val isError by remember { mutableStateOf(false) }

                    Text(text = "현재 비밀번호", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(12.dp))

                    CustomOutlinedTextField( //todo 비밀번호 에러 만들것?
                        pw,
                        { newText ->
                            pw = newText
                        },
                        isError = isError,
                        hint = "비밀번호"
                    )
                    if (isError) {
                        Text(text = "* 비밀번호를 정확하게 입력해주세요.", color = Color.Red, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row (verticalAlignment = Alignment.CenterVertically){
                        Text(text = "비밀번호를 잊으셨나요? ", fontSize = 12.sp, color = Color(0xFF5D5D5D))
                        Text(
                            text = "비밀번호 재설정",
                            fontSize = 12.sp,
                            color = Color(0xFF5D5D5D),
                            modifier = Modifier.clickable { navController.navigate("ResetPwPageWithEmail") },
                            style = TextStyle(textDecoration = TextDecoration.Underline)
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))

                    val enabled = !(isError || pw.isEmpty() || selectedItems.isEmpty())

                    Button(
                        onClick = { /* todo 탈퇴기능 구현 */
                                  isWithdrawalClicked = true},
                        enabled =  enabled,
                        shape = RoundedCornerShape(20),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(61.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE43446),
                            disabledContainerColor = Color(0xFF868686),

                            )
                    ) {
                        Text(text = "탈퇴하기")
                    }


                }
                AnimatedVisibility(
                    visible = isWithdrawalClicked,
                    enter = fadeIn(animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(0.7f)),
                        contentAlignment = Alignment.BottomCenter
                    ){

                        WithdrawalWarningBox(
                            isCancelClicked = { isWithdrawalClicked = false },
                            isWithdrawalClicked = {
                                Log.d(TAG, "AccountWithdrawalPage: $selectedItems")
                                viewModel.requestWithdrawal(selectedItems.toList(),navController)}
                        ) //todo 수정필요 api 탈퇴기능구현
                    }
                }



            }
        )
    }
}

@Composable
fun MultiSelectDeleteList(
    items: List<String>,
    selectedItem: MutableList<String>,
    onItemSelected: (String) -> Unit
) {
    Column {
        items.forEach { item ->
            val isSelected = selectedItem.contains(item)
            val borderColor = if (isSelected) Color.Red else Color.Transparent

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, borderColor, shape = RoundedCornerShape(10))
                    .clickable {
                        onItemSelected(item)
                    }
            ) {
                WithdrawalItem(
                    reportItem = item,
                    isSelected = isSelected,
                    selectedColor = Color(0xFFE43446),
                    onItemSelected = { selected ->
                        if (selected) {
                            onItemSelected(item)
                        } else {
                            // Deselecting is optional, depending on your logic.
                            // If deselecting is not needed, remove this block.
                            onItemSelected("") // Deselecting the item
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(2.dp))


        }

        }
    }

@Composable
fun WithdrawalItem(
    reportItem: String,
    isSelected: Boolean,
    selectedColor : Color,
    onItemSelected: (Boolean) -> Unit
) {
    val color = if (isSelected) selectedColor else Color(0xFF252525)

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onItemSelected(!isSelected) }) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    tint = color,
                    contentDescription = null
                )
            }
            Text(text = reportItem, color = Color.White)
        }
        if (reportItem == "기타 문제" && isSelected) {
            ReportTextField("탈퇴 사유를 작성해주세요.",selectedColor)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}


@Composable
fun WithdrawalWarningBox( isCancelClicked: () ->Unit, isWithdrawalClicked: () -> Unit){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black.copy(0.7f)))
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF121212), shape = RoundedCornerShape(20))
            .height(243.dp), contentAlignment = Alignment.Center){
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "정말 회원 탈퇴를 신청하시겠습니까?", color = Color.White, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(48.dp))
                Row {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(
                                0xFF868686
                            )
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(61.dp),
                        onClick = { isCancelClicked() },
                        shape = RoundedCornerShape(20)
                    ) {
                        Text(text = "취소", color = Color.Black,fontWeight = FontWeight.SemiBold)

                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE43446)),
                        modifier = Modifier
                            .weight(1f)
                            .height(61.dp),
                        onClick = { isWithdrawalClicked()
                                  },
                        shape = RoundedCornerShape(20)
                    ) {
                        Text(text = "확인", color = Color.Black, fontWeight = FontWeight.SemiBold)

                    }
                }

            }
        }
    }
}