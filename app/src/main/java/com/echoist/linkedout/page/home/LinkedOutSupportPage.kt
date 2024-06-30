package com.echoist.linkedout.page.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material.icons.filled.SubdirectoryArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.data.UserInfo
import com.echoist.linkedout.page.settings.SettingTopAppBar
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.HomeViewModel

@Composable
fun LinkedOutSupportPage(
    navController : NavController,
    viewModel: HomeViewModel = hiltViewModel())
{
    LinkedOutTheme {
        Scaffold(topBar = {
            SettingTopAppBar("링크드아웃 고객센터",navController)
        }) {

            Column {
                Column(
                    Modifier
                        .padding(it)
                        .weight(9f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "1:1 문의 내역",
                        modifier = Modifier.padding(horizontal = 20.dp),
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    InquiryBox()
                    InquiryBox()
                    InquiryBox()
                    InquiryBox()
                }

                Spacer(modifier = Modifier.height(20.dp)) // 상단 내용과 하단 버튼 사이의 간격

                Button(
                    shape = RoundedCornerShape(10),
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(61.dp),
                    onClick = { /* TODO 문의창 이동 */ }
                ) {
                    Text(text = "1:1 문의하기", color = Color.Black)
                }
            }



        }
    }
}

data class Inquiry(
    val id: String,
    val title: String,
    val content: String? = null,
    val createdDate: String,
    val processed: Boolean,
    val user: UserInfo
)

@Preview
@Composable
fun InquiryBox(/*inquiry: Inquiry*/){
    var isInquiryClicked by remember {
        mutableStateOf(false)
    }

    Column {
        Box(
            Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF0E0E0E))
                .height(81.dp)
                .clickable { isInquiryClicked = !isInquiryClicked }
                .padding(horizontal = 20.dp)){
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Text(text = "제목 어떤게 있는가?", color = Color.White)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "2024.07.01 문의",fontSize = 10.sp,color = Color(0xFF4D4D4D))
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd){
                Box(modifier = Modifier
                    .width(77.dp)
                    .height(27.dp)
                    .background(color = Color(0xFF191919), shape = RoundedCornerShape(size = 42.dp))
                    .padding(start = 14.dp, top = 3.dp, end = 14.dp, bottom = 3.dp), contentAlignment = Alignment.Center){
                    Text(text = "답변완료", fontSize = 10.sp, color = LinkedInColor)

                }
            }
        }
        HorizontalDivider(color = Color(0xFF191919))
        //if(inquiry.content != null)
        if (isInquiryClicked){
            Box(modifier = Modifier
                .background(color = Color(0xFF0E0E0E))
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp), contentAlignment = Alignment.Center){
                Text(color = Color.White, text = "비밀번호를 잊어버려서 다시 새로운 비밀번호를 설정하고 싶은데 방법을 잘 모르겠어요. 어느 페이지로 들어가야 비밀번호를 재설정할 수 있나요?")

            }
            HorizontalDivider(color = Color(0xFF191919))

            //if(inquiry.답변 != null)
            Box(modifier = Modifier
                .background(color = Color(0xFF0E0E0E))
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(horizontal = 20.dp, vertical = 18.dp)){
                Row(modifier = Modifier
                    .fillMaxSize()){
                    Icon(imageVector = Icons.Filled.SubdirectoryArrowRight, contentDescription = "", tint = LinkedInColor)
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(color = LinkedInColor, text = "비밀번호를 잊어버려서 다시 새로운 비밀번호를 설정하고 싶은데 방법을 잘 모르겠어요. 어느 페이지로 들어가야 비밀번호를 재설정할 수 있나요?")

                }
            }

        }

    }

}