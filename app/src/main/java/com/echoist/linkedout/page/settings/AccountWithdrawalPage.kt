package com.echoist.linkedout.page.settings

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.SignUpViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AccountWithdrawalPage(navController : NavController) {

    val scrollState = rememberScrollState()
    val viewModel: SignUpViewModel = hiltViewModel()

    LinkedOutTheme {
        Scaffold(
            topBar = {
                SettingTopAppBar("탈퇴하기",navController)
            },
            content = {
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

                    val reasonList = listOf(
                        "사용 빈도가 낮아서", "콘텐츠(글)의 질이 기대에 못 미쳐서", "앱의 기능 및 서비스가 불만족스러워서",
                        "앱 사용 중에 자꾸 문제가 생겨서(버그, 오류 등)", "다른 서비스가 더 좋아서","기타 문제"
                    )
                    val selectedItems = remember { mutableStateListOf<String>() }

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
                    var isError by remember { mutableStateOf(false) }

                    Text(text = "현재 비밀번호", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

                    CustomOutlinedTextField(
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

                    Row (verticalAlignment = Alignment.CenterVertically){
                        Text(text = "비밀번호를 잊으셨나요? ", fontSize = 12.sp, color = Color(0xFF5D5D5D))
                        Text(
                            text = "비밀번호 재설정",
                            fontSize = 12.sp,
                            color = Color(0xFF5D5D5D),
                            modifier = Modifier.clickable { navController.navigate("ResetPwPage") },
                            style = TextStyle(textDecoration = TextDecoration.Underline)
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))

                    val enabled = !(isError || pw.isEmpty() || selectedItems.isEmpty())

                    Button(
                        onClick = { /* todo 탈퇴기능 구현 */},
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
