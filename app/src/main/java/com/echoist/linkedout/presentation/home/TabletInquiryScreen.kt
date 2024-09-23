package com.echoist.linkedout.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echoist.linkedout.R
import com.echoist.linkedout.presentation.TabletDrawableTopBar
import com.echoist.linkedout.presentation.userInfo.CustomOutlinedTextField
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TabletInquiryScreen(onCloseClick: () -> Unit) {
    val inquiryTypes = listOf("기술 지원 관련", "계정 및 결제", "콘텐츠 관련", "기능 요청 및 제안", "기타")
    var selectedType by remember { mutableStateOf(inquiryTypes[0]) }
    var isErrTitle by remember { mutableStateOf(true) }
    var isErrContent by remember { mutableStateOf(true) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    LinkedOutTheme {
        Scaffold(topBar = {
            TabletDrawableTopBar(title = "1:1 문의하기", true) {
                onCloseClick()
            }
        }) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Text(text = "* 문의 유형", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    inquiryTypes.forEachIndexed { idx, type ->
                        Row(
                            modifier = Modifier
                                .selectable(
                                    selected = (type == selectedType),
                                    onClick = { selectedType = type }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (type == selectedType),
                                onClick = { selectedType = type }
                            )
                            Text(text = type, fontSize = 16.sp)
                        }
                        if (type == "기술 지원 관련") {
                            val builder = rememberBalloonBuilder {
                                setArrowSize(15)
                                setArrowPosition(0.1f)
                                setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                                setWidth(BalloonSizeSpec.WRAP)
                                setHeight(BalloonSizeSpec.WRAP)
                                setPadding(12)
                                setMarginHorizontal(12)
                                setCornerRadius(8f)
                                setBackgroundColorResource(R.color.black)
                            }

                            Balloon(
                                builder = builder,
                                balloonContent = {
                                    Text(
                                        text = "•기술 지원 관련이란? \n  • 앱 사용법\n  • 버그 신고\n  • 업데이트 문제\n  • 로그인 문제\n  • 동기화 문제 등",
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            lineHeight = 24.sp,
                                            fontWeight = FontWeight(400),
                                            color = Color.White
                                        )
                                    )
                                }
                            ) { balloonWindow ->
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "info",
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .padding(12.dp)
                                        .clickable {
                                            balloonWindow.showAsDropDown()
                                        },
                                    tint = Color.Gray,
                                )
                            }
                        }
                        if (idx < inquiryTypes.size - 1) {
                            Spacer(modifier = Modifier.width(20.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(45.dp))
                Text(text = "* 제목", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
                CustomOutlinedTextField(
                    title, {
                        title = it
                        isErrTitle = title.isEmpty()
                    }, "제목을 입력해주세요.", isErrTitle
                )

                Spacer(modifier = Modifier.height(22.dp))

                Text(text = "* 문의 내용", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
                CustomOutlinedTextField(
                    content,
                    {
                        content = it
                        isErrContent = content.isEmpty()
                    },
                    "접수된 문의를 순차적으로 답변 드리고 있습니다. 문의 내용을 상세히 기재해 주실수록 정확한 답변이 가능합니다.\n",
                    isErrContent, modifier = Modifier.height(200.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .fillMaxWidth()
                        .height(61.dp)
                ) {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF868686)),
                        shape = RoundedCornerShape(10),
                        onClick = { onCloseClick() }
                    ) {
                        Text(text = "취소", color = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LinkedInColor,
                            disabledContainerColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(10),
                        enabled = !isErrTitle && !isErrContent,
                        onClick = { }
                    ) {
                        Text(text = "등록", color = Color.Black)
                    }
                }
            }
        }
    }
}
