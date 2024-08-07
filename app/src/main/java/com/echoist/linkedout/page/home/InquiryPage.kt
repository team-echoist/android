package com.echoist.linkedout.page.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.page.settings.CustomOutlinedTextField
import com.echoist.linkedout.page.settings.SettingTopAppBar
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.SupportViewModel

@Composable
fun InquiryPage(
    navController : NavController,
    viewModel: SupportViewModel = hiltViewModel()
)
{

    val inquiryOptions = listOf("기술 지원 관련", "계정 및 결제", "콘텐츠 관련", "기능 요청 및 제안", "기타")
    var selectedItem by remember { mutableStateOf("") }


    var inquiryTitle by remember { mutableStateOf("") }
    var isErrTitle by remember { mutableStateOf(true)}

    var inquiryContent by remember { mutableStateOf("")}
    var isErrContent by remember { mutableStateOf(true)}



    LinkedOutTheme {
        Scaffold(topBar = {
            SettingTopAppBar("1:1 문의하기",navController)

        }) {

            if (viewModel.isLoading){
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator(color = LinkedInColor)
                }
            }
            Column {
                Column(
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(it)
                        .weight(9f)
                        .padding(horizontal = 10.dp)
                ) {
                    Spacer(modifier = Modifier.height(42.dp))

                        Text(
                            text = "* 문의 유형",
                            fontSize = 18.sp
                        )


                    Spacer(modifier = Modifier.height(12.dp))
                    SingleSelectInquiryList(inquiryOptions,selectedItem) { it -> selectedItem = it }
                    Spacer(modifier = Modifier.height(25.dp)) // 상단 내용과 하단 버튼 사이의 간격
                    Text(
                        text = "* 제목",
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    CustomOutlinedTextField(
                        inquiryTitle,{
                        inquiryTitle = it
                            isErrTitle = inquiryTitle.isEmpty()
                        },"제목을 입력해주세요.",isErrTitle)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "* 문의 내용",
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    CustomOutlinedTextField(
                        inquiryContent,
                        {
                            inquiryContent = it
                            isErrContent = inquiryContent.isEmpty()
                        },
                        "접수된 문의를 순차적으로 답변 드리고 있습니다. 문의 내용을 상세히 기재해 주실수록 정확한 답변이 가능합니다.\n",
                        isErrContent, modifier = Modifier.height(200.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                Row (modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .height(61.dp)){
                    Button(
                        modifier = Modifier.weight(1f).height(60.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF868686)),
                        shape = RoundedCornerShape(10),
                        onClick = { navController.popBackStack() }
                    ) {
                        Text(text = "취소", color = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        modifier = Modifier.weight(1f).height(60.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LinkedInColor, disabledContainerColor = Color.Gray),
                        shape = RoundedCornerShape(10),
                        enabled = !isErrTitle && !isErrContent,
                        onClick = { viewModel.writeInquiry(inquiryTitle,inquiryContent,selectedItem,navController) }
                    ) {
                        Text(text = "등록", color = Color.Black)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                
            }



        }
    }
}

@Composable
fun InquiryItem(
    inquiryItem: String,
    isSelected: Boolean,
    selectedColor : Color,
    onItemSelected: (Boolean) -> Unit
) {
    val color = if (isSelected) selectedColor else Color(0xFF252525)

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onItemSelected(!isSelected) }) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    tint = color,
                    contentDescription = null
                )
            }
            Text(text = inquiryItem, color = Color.White)

//            if (inquiryItem == "기술 지원\n관련") {
//                Spacer(modifier = Modifier.width(10.dp))
//                InquiryInfoBox()
//
//
//            }
        }
    }
}

@Composable
fun SingleSelectInquiryList(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp) // 수평 간격 추가
    ) {
        items(items) { item ->
            val isSelected = item == selectedItem

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemSelected(if (selectedItem == item) "" else item)
                    }
            ) {
                Row (verticalAlignment = Alignment.CenterVertically){
                    InquiryItem(
                        inquiryItem = item,
                        isSelected = isSelected,
                        selectedColor = LinkedInColor,
                        onItemSelected = { selected ->
                            onItemSelected(if (selected) item else "")
                        }
                    )

                }
            }
        }
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Preview
@Composable
fun InquiryInfoBox(){

    var isClicked by remember { mutableStateOf(false) }

    Column {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "info",
            modifier = Modifier
                .size(24.dp)
                .clickable { isClicked = !isClicked }, // 아이콘 크기 설정
            tint = Color.Gray // 아이콘 색상 설정
        )
        if (isClicked){
            Box(modifier = Modifier
                .size(145.dp, 161.dp)
                .padding(end = 15.dp)){
                GlideImage(
                    model = R.drawable.inquirybox,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 26.dp)
                        .padding(horizontal = 10.dp)) {
                    Text(
                        text = "• 앱 사용법\n• 버그 신고\n• 업데이트 문제\n• 로그인 문제\n• 동기화 문제 등",
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight(400),
                            color = Color(0xFF909090),

                            )
                    )
                }
            }
        }

    }

}