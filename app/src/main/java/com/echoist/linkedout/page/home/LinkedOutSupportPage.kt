package com.echoist.linkedout.page.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.echoist.linkedout.data.Inquiry
import com.echoist.linkedout.page.settings.SettingTopAppBar
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.SupportViewModel
import kotlinx.coroutines.launch

@Composable
fun LinkedOutSupportPage(
    navController: NavController,
    viewModel: SupportViewModel = hiltViewModel()
) {
    // viewModel을 사용해 데이터를 가져오기
    viewModel.readInquiryList()
    val inquiryList by viewModel.inquiryList.collectAsState()

    // 테마 적용
    LinkedOutTheme {
        Scaffold(
            topBar = {
                SettingTopAppBar("링크드아웃 고객센터", navController)
            }
        ) { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .fillMaxHeight()
            ) {

                // 상단 텍스트와 스페이서
                Text(
                    text = "1:1 문의 내역",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(20.dp))

                // LazyColumn을 담은 Box로 수정

                    LazyColumn(
                        modifier = Modifier.height(400.dp),
                    ) {
                        items(inquiryList) { inquiry ->
                            InquiryBox(inquiry, viewModel)
                        }
                    }


                Spacer(modifier = Modifier.height(20.dp)) // 상단 내용과 하단 버튼 사이의 간격

                // 하단 버튼

            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){
                Button(
                    shape = RoundedCornerShape(10),
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                        .fillMaxWidth()
                        .height(61.dp),
                    onClick = { navController.navigate("InquiryPage") }
                ) {
                    Text(text = "1:1 문의하기", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}




@Composable
fun InquiryBox(inquiry: Inquiry,viewModel: SupportViewModel){
    var isInquiryClicked by remember {
        mutableStateOf(false)
    }

    val processed = if (inquiry.processed!!) " 답변완료" else "답변대기"
    val textColor = if (inquiry.processed) LinkedInColor else Color(0xFFE43446)
    val scope = rememberCoroutineScope()

    var content by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }




    Column {
        Box(
            Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF0E0E0E))
                .height(81.dp)
                .clickable {
                    isInquiryClicked = !isInquiryClicked
                    scope.launch {
                        content = viewModel.readDetailInquiry(inquiry.id!!)!!.content ?: ""
                        answer = viewModel.readDetailInquiry(inquiry.id)!!.answer ?: ""

                    }
                }
                .padding(horizontal = 20.dp)){
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Text(text = inquiry.title, color = Color.White)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = inquiry.createdDate!!,fontSize = 10.sp,color = Color(0xFF4D4D4D))
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd){
                Box(modifier = Modifier
                    .width(77.dp)
                    .height(27.dp)
                    .background(color = Color(0xFF191919), shape = RoundedCornerShape(size = 42.dp))
                    .padding(start = 14.dp, top = 3.dp, end = 14.dp, bottom = 3.dp), contentAlignment = Alignment.Center){
                    Text(text = processed, fontSize = 10.sp, color = textColor, textAlign = TextAlign.Center, modifier = Modifier.fillMaxSize() )

                }
            }
        }
        HorizontalDivider(color = Color(0xFF191919))

        if(content.isNotEmpty()){
            if (isInquiryClicked){
                Box(modifier = Modifier
                    .background(color = Color(0xFF0E0E0E))
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp), contentAlignment = Alignment.CenterStart){
                    Text(color = Color.White, text = content)

                }
        }

            HorizontalDivider(color = Color(0xFF191919))


            if (answer.isNotEmpty()){
                Box(modifier = Modifier
                    .background(color = Color(0xFF0E0E0E))
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(horizontal = 20.dp, vertical = 18.dp)){
                    Row(modifier = Modifier
                        .fillMaxSize()){
                        Icon(imageVector = Icons.Filled.SubdirectoryArrowRight, contentDescription = "", tint = LinkedInColor)
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(color = LinkedInColor, text = answer)

                    }
                }
            }


        }

    }

}