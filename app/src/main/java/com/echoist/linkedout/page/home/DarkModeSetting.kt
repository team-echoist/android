package com.echoist.linkedout.page.home

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.DARK_MODE
import com.echoist.linkedout.LIGHT_MODE
import com.echoist.linkedout.R
import com.echoist.linkedout.SharedPreferencesUtil
import com.echoist.linkedout.page.settings.SettingTopAppBar
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme

@Composable
fun DarkModeSettingPage(navController: NavController){

    val context = LocalContext.current
    // Destructuring Declaration을 통해 두 값을 나누어 사용 selectedMode를 설정하는 함수로 사용가능
    val (selectedMode, setSelectedMode) = remember { mutableStateOf<String?>(SharedPreferencesUtil.getDisplayInfo(context)) }


    LinkedOutTheme {
        Scaffold(
            topBar = {
                SettingTopAppBar("화면",navController)
            },
            content = {
                Column(Modifier.padding(it)) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp), contentAlignment = Alignment.Center){

                        ModeItem(
                            modeText = "라이트 모드",
                            modeImg = R.drawable.mode_light,
                            isSelected = selectedMode == LIGHT_MODE,
                            onItemSelected = {
                                setSelectedMode("라이트 모드")
                                SharedPreferencesUtil.saveDisplayInfo(context, LIGHT_MODE) //선택 시 shared에 저장
                                             },
                            modifier = Modifier.padding(end = 140.dp)
                        )
                        ModeItem(
                            modeText = "다크 모드",
                            modeImg = R.drawable.mode_dark,
                            isSelected = selectedMode == DARK_MODE,
                            onItemSelected = {
                                setSelectedMode("다크 모드")
                                SharedPreferencesUtil.saveDisplayInfo(context, DARK_MODE)
                                             },
                            modifier = Modifier.padding(start = 140.dp)

                        )

                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ModeItem(
    modeText: String,
    modeImg: Int,
    isSelected: Boolean,
    onItemSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (isSelected) LinkedInColor else Color(0xFF252525)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable { onItemSelected() }
            .padding(16.dp)
    ) {
        GlideImage(model = modeImg, contentDescription = "modeImg")
        Spacer(modifier = Modifier.height(22.dp))
        Icon(
            imageVector = Icons.Default.CheckCircle,
            tint = color,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = modeText, fontSize = 16.sp)
    }
}

@Preview
@Composable
fun WebViewExample() {
    val htmlData = """
        <!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>개인정보처리방침</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 0;
      padding: 0;
      display: flex;
      justify-content: center;
      align-items: center;
      background-color: #f7f7f7;
    }

    .container {
      width: 80%;
      margin: 20px;
      padding: 20px;
      background-color: #fff;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    }

    h1, h2, h3 {
      color: #333;
    }

    p {
      line-height: 1.6;
      color: #666;
    }

    .icon {
      width: 24px;
      height: 24px;
      display: inline-block;
      background-color: #ddd;
      margin-right: 10px;
    }

    ul {
      list-style: none;
      padding: 0;
    }

    li {
      margin-bottom: 10px;
    }

    a {
      color: #007bff;
      text-decoration: none;
    }

    a:hover {
      text-decoration: underline;
    }

    .nav {
      display: flex;
      justify-content: space-between;
      flex-wrap: wrap;
      margin-bottom: 20px;
      padding: 10px;
      border: 1px solid #ddd;
    }

    .nav a {
      margin-right: 15px;
      margin-bottom: 10px;
    }

    .labels {
      display: flex;
      justify-content: space-around;
      margin: 20px 0;
    }

    .label-item {
      text-align: center;
      flex: 1;
      margin: 0 10px;
    }

    .label-item img {
      width: 80px;
      height: 80px;
      object-fit: contain;
      margin-bottom: 10px;
    }

    .separator {
      border-top: 1px solid rgba(0, 0, 0, 0.1);
      margin: 20px 0;
    }

    .info-list {
      margin-left: 20px;
      margin-bottom: 20px;
    }

    .info-item {
      margin-bottom: 5px;
    }

    .info-box {
      display: flex;
      justify-content: space-between;
      border: 1px solid #ddd;
      padding: 20px;
      margin-top: 20px;
    }

    .info-box-item {
      text-align: center;
      flex: 1;
      margin: 0 10px;
      font-size: 14px;
    }

    .info-box-item img {
      width: 80px;
      height: 80px;
      object-fit: contain;
      margin-bottom: 10px;
    }

    .info-box-item + .info-box-item {
      border-left: 1px solid rgba(0, 0, 0, 0.1);
      padding-left: 20px;
    }
  </style>
</head>
<body>
<div class="container">
  <h2 id="section1">1. 링크드아웃 개인정보 처리방침</h2>
  <p>
    “개인정보 처리방침”이란 이용자가 안심하고 서비스를 이용할 수 있도록 회사가 준수해야 할 지침을 의미하며, 링크드아웃은
    개인정보처리자가 준수하여야 하는 대한민국의 관계 법령 및 개인정보보호 규정, 가이드라인을 준수하여 개인정보 처리방침을 제공합니다.
  </p>
  <p>링크드아웃은 이용자의 동의를 기반으로 개인정보를 수집·이용 및 제공하고 있습니다. 이용자의 권리(개인정보 자기결정권)를 적극적으로 보장하기 위해 개인정보 처리방침을 알기 쉽게 제공할 수 있도록 다양한 노력을
    기울이고 있으며, 이러한 노력의 일환으로 링크드아웃의 주요 개인정보 처리 관련 정보를 라벨링으로 제공합니다.</p>
  <br>
  <div class="labels">
    <div class="label-item">
      <img src="./img/type/개인정보.png" alt="">
      <div>개인정보</div>
    </div>
    <div class="label-item">
      <img src="./img/process/처리목적.png" alt="">
      <div>처리목적</div>
    </div>
    <div class="label-item">
      <img src="./img/process/제3자제공.png" alt="">
      <div>제3자 제공</div>
    </div>
    <div class="label-item">
      <img src="./img/process/처리위탁.png" alt="">
      <div>처리위탁</div>
    </div>
    <div class="label-item">
      <img src="./img/duty/정보주체의%20권리의무.png" alt="">
      <div>정보주체의 권리의무</div>
    </div>
    <div class="label-item">
      <img src="./img/duty/고충처리부서.png" alt="">
      <div>고충처리 부서</div>
    </div>
  </div>

  <br>
  <div class="separator"></div>
  <br>

  <h2 id="section2">2. 개인정보 수집</h2>
  <h4>서비스 제공을 위한 필요 최소한의 개인정보를 수집합니다.</h4>
  <p>회원 가입 시 또는 서비스 이용 과정에서 홈페이지 또는 개별 어플리케이션이나 프로그램 등을 통해 서비스 제공을 위해 필요 최소한의 개인정보를 수집하고
    있습니다. 서비스 제공을 위해 반드시 필요한 최소한의 정보를 필수항목으로, 그 외 특화된 서비스를 제공하기 위해 추가 수집하는 정보는 선택항목으로 동의를 받고
    있으며, 선택항목에 동의하지 않은 경우에도 서비스 이용 제한은 없습니다.</p>
  <br>
  <div class="info-list">
    <p><strong>[링크드아웃 계정 가입 시]</strong></p>
    <p class="info-item">[필수] 이메일, 비밀번호, 이름(닉네임), 프로필사진, 친구목록, 서비스 이용내역, 서비스 내 구매 및
      결제 내역</p>
    <p class="info-item">[선택] 생년월일, 성별, 배송지정보(수령인명, 배송지주소, 전화번호)</p>
  </div>
  <br>
  <div class="info-list">
    <p><strong>[고객상담 시]</strong></p>
    <p class="info-item">고객센터로 문의 및 상담 시 상담 처리를 위한 추가적인 정보를 수집할 수 있습니다.</p>
  </div>
  <br>
  <h4>개인정보를 수집하는 방법은 아래와 같습니다.</h4>
  <p>개인정보를 수집하는 경우에는 원칙적으로 사전에 이용자에게 해당 사실을 알리고 동의를 구하고 있으며, 아래와 같은 방법을 통해 개인정보를 수집합니다.</p>
  <div class="info-list">
    <p class="info-item"> • 회원가입 및 서비스 이용 과정에서 이용자가 개인정보 수집에 대해 동의를 하고 직접 정보를 입력하는 경우</p>
    <p class="info-item"> • 고객센터를 통한 상담 과정에서 웹페이지, 메일, 팩스, 전화 등</p>
  </div>
  <br>
  <h4>서비스 이용 과정에서 단말기정보, IP주소, 쿠키, 서비스 이용 내역 등의 정보가 자동으로 생성되어 수집될 수 있습니다.</h4>
  <p>서비스 이용 내역이란 서비스 이용 과정에서 자동화된 방법으로 생성되거나 이용자가 입력한 정보가 송수신되면서 링크드아웃 서버에 자동으로 기록 및 수집될 수 있는 정보를 의미합니다. 이와 같은 정보는 다른
    정보와의 결합 여부, 처리하는 방식 등에 따라 개인정보에 해당할 수 있고 개인정보에 해당하지 않을 수도 있습니다.</p>
  <p>서비스 이용 내역에는 이용자가 입력 및 공유한 콘텐츠, 이용자가 입력한 검색어, 방문 및 접속기록, 서비스 부정이용 기록, 위치정보 등이 포함될 수 있습니다. 링크드아웃은 서비스 이용기록 등의 정보를 서비스
    제공 목적으로 처리할 수 있으며 필요한 경우에는 이용자의 추가 동의 등을 받고 이용할 수 있습니다.</p>
  <div class="info-box">
    <div class="info-box-item">
      <img src="./img/type/개인정보.png" alt="">
      <div>개인정보</div>
      <p>링크드아웃 계정 가입 시 서비스 제공을 위해 필요한 최소한의 개인정보를 수집하고 있습니다. 서비스 이용 시 특화된 기능 이용을 위해 이용자의 동의를 받고 추가적인 개인정보를 수집할 수
        있습니다.</p>
    </div>
    <div class="info-box-item">
      <img src="./img/process/자동화수집.png" alt="">
      <div>자동화 수집</div>
      <p>링크드아웃 서비스 이용 과정에서 단말기정보, IP주소, 쿠키, 방문일시, 부정이용기록, 서비스 이용 기록 등의 정보가 자동으로 생성되어 수집될 수 있습니다.</p>
    </div>
  </div>

  <h2 id="section3">3. 개인정보 이용</h2>
  <p>회원관리, 서비스 제공·개선, 신규 서비스 개발 등을 위해 이용합니다.</p>
  <ul>
    <li><span class="icon"></span> 회원 식별/가입의사 확인, 본인/연령 확인</li>
    <li><span class="icon"></span> 14세 미만 아동의 개인정보 수집 시 법정 대리인 동의여부 확인, 법정 대리인 권리행사 시 본인 확인</li>
    <li><span class="icon"></span> 이용자간 메시지 전송, 친구등록 및 친구추천 기능의 제공</li>
  </ul>

  <h2 id="section4">4. 개인정보 제공</h2>
  <p>링크드아웃은 이용자의 별도 동의가 있거나 법령에 규정된 경우를 제외하고는 이용자의 개인정보를 제3자에게 제공하지 않습니다.</p>
  <ul>
    <li><span class="icon"></span> 링크드아웃은 이용자의 사전 동의 없이 개인정보를 제3자에게 제공하지 않습니다.</li>
    <li><span class="icon"></span> 다만, 이용자가 링크드아웃 계정 로그인 서비스를 이용하거나 외부 제휴사 등의 서비스를 이용하는 경우 필요한 범위 내에서 이용자의 동의를 얻은 후에
      개인정보를 제3자에게 제공하고 있습니다.
    </li>
  </ul>

  <h2 id="section5">5. 개인정보 파기</h2>
  <p>수집 및 이용목적이 달성된 경우 수집한 개인정보는 지체없이 파기합니다.</p>

  <h2 id="section6">6. 개인위치정보의 처리</h2>
  <p>링크드아웃은 위치정보의 보호 및 이용 등에 관한 법률에 따라 아래와 같이 개인위치정보를 처리합니다.</p>

  <h2 id="section7">7. 이용자 및 법정대리인의 권리와 행사 방법</h2>
  <p>이용자는 자신의 개인정보 처리에 관하여 아래와 같은 권리를 가질 수 있습니다.</p>
  <ul>
    <li><span class="icon"></span> 개인정보 열람(조회)을 요구할 권리</li>
    <li><span class="icon"></span> 개인정보 정정을 요구할 권리</li>
    <li><span class="icon"></span> 개인정보 처리정지를 요구할 권리</li>
    <li><span class="icon"></span> 개인정보 삭제요구 및 동의철회/탈퇴를 요구할 권리</li>
  </ul>

  <h2 id="section8">8. 개인정보 자동 수집 장치에 관한 사항</h2>
  <p>웹기반 서비스 제공을 위하여 쿠키를 설치·운영할 수 있습니다.</p>

  <h2 id="section9">9. 개인정보의 안전성 확보 조치에 관한 사항</h2>
  <p>링크드아웃은 이용자의 개인정보 보호를 위해 아래의 노력을 합니다.</p>
  <ul>
    <li><span class="icon"></span> 이용자의 개인정보를 암호화하고 있습니다.</li>
    <li><span class="icon"></span> 해킹이나 컴퓨터 바이러스로부터 보호하기 위하여 노력하고 있습니다.</li>
    <li><span class="icon"></span> 개인정보에 접근할 수 있는 사람을 최소화하고 있습니다.</li>
    <li><span class="icon"></span> 개인정보취급자에게 이용자의 개인정보 보호에 대해 정기적인 교육을 실시하고 있습니다.</li>
    <li><span class="icon"></span> 개인정보가 포함된 서류, 보조저장매체 등을 잠금장치가 있는 안전한 장소에 보관하고 있습니다.</li>
  </ul>

  <h2 id="section10">10. 개인정보 보호책임자 및 고충처리 부서</h2>
  <p>링크드아웃은 이용자의 개인정보 관련 문의사항 및 불만 처리 등을 위하여 개인정보 보호책임자 및 고충처리 부서를 지정하고 있습니다.</p>
  <p>책임자: 김연지 (개인정보 보호책임자/DPO/위치정보관리책임자)</p>
  <p>소속: 개인정보보호부서</p>

  <h2 id="section11">11. 개정 전 고지의무 등 안내</h2>
  <p>법령이나 서비스의 변경사항을 반영하기 위한 목적 등으로 개인정보 처리방침을 수정할 수 있습니다.</p>
</div>
</body>
</html>
    """.trimIndent()

    AndroidView(factory = { context ->
        WebView(context).apply {
            webViewClient = WebViewClient()  // 페이지 내에서 링크 클릭 시 WebView 안에서 열리도록 함
            loadDataWithBaseURL("https://www.naver.com", "htmlData", "text/html", "UTF-8", null)
        }
    })
}

@Preview
@Composable
fun WebViewExa2mple() {
    AndroidView(factory = { context ->
        WebView(context).apply {
            webViewClient = WebViewClient()  // 페이지 내에서 링크 클릭 시 WebView 안에서 열리도록 설정
            loadUrl("https://www.naver.com")
        }
    }, update = { webView ->
        webView.loadUrl("https://www.naver.com")
    })
}