package com.echoist.linkedout.page.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.SignUpViewModel

@Composable
fun AgreeOfProvisionsPage(navController: NavController, viewModel: SignUpViewModel) {
    LinkedOutTheme {
        Scaffold { it ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {

                Spacer(modifier = Modifier.height(20.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "arrowback",
                    tint = if (isSystemInDarkTheme()) {
                        Color.White

                    } else Color.Gray,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(30.dp)
                        .clickable { navController.popBackStack() } //뒤로가기
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "이용약관 동의",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 16.dp),
                    color = if (isSystemInDarkTheme()) Color.White else Color.Gray
                )
                Text(
                    text = "회원 서비스 이용을 위해 회원가입을 해주세요.",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 16.dp, bottom = 32.dp),
                    color = if (isSystemInDarkTheme()) Color(0xFF919191) else Color.Gray
                )
                val agreementList = listOf(
                    viewModel.agreement_location,
                    viewModel.agreement_service,
                    viewModel.agreement_serviceAlert,
                    viewModel.agreement_teen,
                    viewModel.agreement_marketing,
                    viewModel.agreement_collection
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "check",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(25.dp)
                            .clickable {
                                // 리스트 중에 하나라도 true가 있으면 전부 true로, 아니면 전부 false로 설정
                                val shouldBeTrue = agreementList.any { !it }

                                viewModel.agreement_location = shouldBeTrue
                                viewModel.agreement_service = shouldBeTrue
                                viewModel.agreement_serviceAlert = shouldBeTrue
                                viewModel.agreement_teen = shouldBeTrue
                                viewModel.agreement_marketing = shouldBeTrue
                                viewModel.agreement_collection = shouldBeTrue
                            },
                        tint = if (agreementList.all { it }) {
                            LinkedInColor
                        } else Color(0xFF919191)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "전체 동의")
                }
                AgreementText(
                    text = "(필수)  서비스  이용약관  동의",
                    {
                        viewModel.agreement_service =
                            !viewModel.agreement_service
                    }, if (viewModel.agreement_service) LinkedInColor else Color(0xFF919191)
                )
                AgreementText(
                    text = "(필수)  개인정보  수집  및  이용  동의",
                    {
                        viewModel.agreement_collection =
                            !viewModel.agreement_collection
                    }, if (viewModel.agreement_collection) LinkedInColor else Color(0xFF919191)
                )
                AgreementText(
                    text = "(필수)   만  14세  이상입니다",
                    { viewModel.agreement_teen = !viewModel.agreement_teen },
                    if (viewModel.agreement_teen) LinkedInColor else Color(0xFF919191)
                )
                AgreementText(
                    text = "(선택)   위치 기반 서비스 이용 약관 동의",
                    {
                        viewModel.agreement_location =
                            !viewModel.agreement_location
                    }, if (viewModel.agreement_location) LinkedInColor else Color(0xFF919191)
                )
                AgreementText(
                    text = "(선택)   마케팅  정보  수신  동의",
                    {
                        viewModel.agreement_marketing =
                            !viewModel.agreement_marketing
                    }, if (viewModel.agreement_marketing) LinkedInColor else Color(0xFF919191)
                )
                AgreementText(
                    text = "(선택)   서비스 알림  수신  동의",
                    {
                        viewModel.agreement_serviceAlert =
                            !viewModel.agreement_serviceAlert
                    }, if (viewModel.agreement_serviceAlert) LinkedInColor else Color(0xFF919191)
                )

                Button(
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LinkedInColor,
                        disabledContainerColor = Color(0xFF868686)
                    ),
                    enabled = viewModel.agreement_service && viewModel.agreement_collection && viewModel.agreement_teen,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(start = 20.dp, end = 20.dp, top = 43.dp),
                    onClick = {
                        viewModel.setAgreementOfSignUp(
                            viewModel.agreement_location,
                            viewModel.agreement_marketing,
                            viewModel.agreement_serviceAlert,
                            navController //동의여부 서버에 보내고 첫 회원가입 로그인
                        )
                    }
                ) {
                    Text(text = "확인", color = Color.Black)
                }
            }
        }
    }

}