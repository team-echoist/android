package com.echoist.linkedout.page.home.legal_Notice

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.echoist.linkedout.page.settings.SettingTopAppBar
import com.echoist.linkedout.ui.theme.LinkedOutTheme

@Composable
fun TermsAndConditionsPage(navController : NavController){
    LinkedOutTheme {
        Scaffold(topBar = {
            SettingTopAppBar("이용 약관",navController)
        }) {

            Column(
                Modifier
                    .padding(it)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()) {
                Text(text = "### Linkedout 이용약관\n" +
                        "\n" +
                        "#### 제1장 총칙\n" +
                        "\n" +
                        "**제1조(목적)**  \n" +
                        "이 약관은 `Linkedout`(이하 '회사'라 합니다)가 제공하는 디지털 콘텐츠 및 서비스(이하 '서비스'라 합니다)의 이용과 관련하여 회사와 이용자의 권리, 의무 및 책임사항을 규정함을 목적으로 합니다.\n" +
                        "\n" +
                        "**제2조(정의)**  \n" +
                        "1. \"서비스\"라 함은 회사가 제공하는 디지털 콘텐츠 및 관련 제반 서비스를 말합니다.\n" +
                        "2. \"이용자\"라 함은 본 약관에 따라 회사가 제공하는 서비스를 받는 회원 및 비회원을 말합니다.\n" +
                        "3. \"회원\"이라 함은 회사와 서비스 이용계약을 체결하고, 회원 아이디(ID)를 부여받은 이용자를 말합니다.\n" +
                        "4. \"비회원\"이라 함은 회원에 가입하지 않고 회사가 제공하는 서비스를 이용하는 자를 말합니다.\n" +
                        "5. \"콘텐츠\"라 함은 부호, 문자, 도형, 색채, 음성, 음향, 이미지 및 영상 등(이들의 복합체를 포함)으로 이루어진 자료 또는 정보로서 그 보존 및 이용의 효용을 높일 수 있도록 디지털 형태로 제작 또는 처리된 것을 말합니다.\n" +
                        "6. \"인앱결제\"라 함은 모바일 앱 내에서 디지털 콘텐츠나 서비스를 구매하는 행위를 말합니다.\n" +
                        "\n" +
                        "**제3조(약관의 효력 및 변경)**  \n" +
                        "1. 본 약관은 서비스를 이용하는 모든 이용자에게 적용됩니다.\n" +
                        "2. 회사는 관련 법령을 위배하지 않는 범위 내에서 본 약관을 개정할 수 있습니다.\n" +
                        "3. 변경된 약관은 적용일자 및 변경사유를 명시하여 서비스 내 또는 관련 웹사이트 초기화면에 그 적용일자 7일 전부터 공지합니다.\n" +
                        "4. 이용자에게 불리한 약관의 변경일 경우 적용일자 30일 전부터 서비스 내 공지사항 및 이메일을 통해 통지합니다.\n" +
                        "5. 이용자가 개정 약관의 적용에 동의하지 않는 경우 이용자는 회사에 그 의사를 통지함으로써 서비스 이용계약을 해지할 수 있습니다. 이용자가 거부의사를 표시하지 않고 서비스를 계속 이용할 경우 변경된 약관에 동의한 것으로 간주합니다.\n" +
                        "6. 본 약관은 이용자가 약관에 동의한 날로부터 이용계약의 해지 시까지 적용함을 원칙으로 하지만, 본 약관의 일부 조항은 이용계약의 해지 후에도 유효하게 적용될 수 있습니다.\n" +
                        "\n" +
                        "**제4조(약관의 해석)**  \n" +
                        "1. 회사는 본 약관을 성실히 해석하며, 이용자에게 불리하게 해석하지 않습니다.\n" +
                        "2. 본 약관에서 정하지 않은 사항은 회사 운영정책 및 규칙, 관련 법령에 따릅니다. 본 약관과 세부지침의 내용이 충돌할 경우 세부지침을 우선합니다.\n" +
                        "\n" +
                        "**제5조(개별 약정의 우선적용)**  \n" +
                        "회사가 별도로 정한 개별 약정이 본 약관의 내용과 상충하는 경우, 개별 약정이 우선합니다.\n" +
                        "\n" +
                        "**제6조(회원 정보 변경)**  \n" +
                        "회원은 회사에 제공한 정보 중 변경이 필요한 사항이 있을 경우, 온라인으로 수정하거나 회사에 문의하여 변경할 수 있습니다.\n" +
                        "\n" +
                        "#### 제2장 개인정보보호\n" +
                        "\n" +
                        "**제7조(개인정보의 보호)**  \n" +
                        "1. 회사는 관련 법령에 따라 이용자의 개인정보를 보호하며, 개인정보의 처리 및 보호와 관련된 세부 사항은 회사의 개인정보처리방침에 따릅니다.\n" +
                        "2. 회사는 이용자의 개인정보를 제3자에게 제공하지 않습니다.\n" +
                        "3. 이용자는 개인정보의 열람, 정정, 처리정지 등 권리를 행사할 수 있으며, 그 절차는 개인정보처리방침에 명시된 절차에 따릅니다.\n" +
                        "\n" +
                        "**제8조(회원탈퇴 및 자격상실)**  \n" +
                        "1. 회원은 언제든지 탈퇴를 요청할 수 있으며, 회사는 즉시 탈퇴를 처리합니다. 탈퇴 요청 시 30일의 유예기간을 제공하며, 유예기간을 사용하지 않고 즉시 탈퇴를 선택할 수도 있습니다.\n" +
                        "2. 회원탈퇴가 진행되면 사용자가 생성한 모든 창작물 또는 데이터는 일괄 삭제됩니다.\n" +
                        "\n" +
                        "**제9조(아이디 및 비밀번호 관리)**  \n" +
                        "1. 회원은 아이디와 비밀번호를 선량한 관리자의 주의의무로 관리하며, 제3자에게 이용을 허락할 수 없습니다.\n" +
                        "2. 회원은 아이디 및 비밀번호가 도용되거나 제3자가 사용하고 있음을 인지한 경우 즉시 회사에 통지하고 회사의 안내에 따라야 합니다.\n" +
                        "3. 회사는 회원이 제3항을 위반하여 발생한 불이익에 대해 책임을 지지 않습니다.\n" +
                        "\n" +
                        "#### 제3장 서비스 제공 및 이용\n" +
                        "\n" +
                        "**제10조(인앱결제 및 환불)**  \n" +
                        "1. 사용자가 구입한 구독 서비스, 디지털 아이템 및 재화는 구글 플레이 스토어 또는 애플 앱 스토어의 정책에 따라 환불이 진행됩니다.\n" +
                        "2. 회사의 모든 결제는 인앱결제로 이루어지며, 인앱결제는 구글 플레이 스토어 또는 애플 앱 스토어의 정책에 따라 환불됩니다. \n" +
                        "3. 회사는 인앱결제 환불에 대한 직접적인 권한이 없으며, 환불 요청은 각 플랫폼의 고객지원팀을 통해 진행됩니다.\n" +
                        "4. 스토어 플랫폼 정책에 따라 48시간 이내에 환불이 가능하지만, 회사는 소비자보호법에 의해 7일 이내에 회사로 환불을 신청할 수 있습니다.\n" +
                        "5. 구독 서비스의 경우, 구독 취소 시 구독 기간 동안 사용이 가능하며, 환불 요청 시 소비자보호법에 의해 7일 이내에 회사에 신청해야 하며 사용일만큼 환불 금액에서 차감됩니다.\n" +
                        "\n" +
                        "\n" +
                        "**제11조(지적재산권)**  \n" +
                        "1. 회사가 제공하는 모든 서비스와 콘텐츠에 대한 저작권 및 기타 지식재산권은 회사에 귀속됩니다. 이용자는 회사의 사전 승낙 없이 저작물을 복제, 배포, 출판할 수 없습니다.\n" +
                        "2. 사용자는 서비스 내에서 사진, 글, 정보, 영상, 또는 회사에 대한 의견이나 제안 등 콘텐츠를 게시할 수 있으며, 이러한 게시물에 대한 저작권을 포함한 지적재산권은 권리자가 계속해서 보유합니다.\n" +
                        "3. 사용자가 서비스 내에서 작성한 게시물은 회사가 서비스를 운영, 개선, 홍보하고 새로운 서비스를 개발하기 위한 범위 내에서 사용, 저장, 수정, 복제 등의 방식으로 이용할 수 있는 라이선스를 회사에게 제공합니다. 이 라이선스는 회사가 서비스 운영과 관련된 범위 내에서 사용자의 게시물을 활용할 수 있도록 합니다. 회사는 사용자가 제공한 콘텐츠에 접근하거나 이를 삭제하는 방법을 제공할 수 있으며, 일부 서비스의 특성 및 콘텐츠의 성질 등에 따라 게시물의 삭제가 불가능할 수도 있습니다.\n" +
                        "4. 사용자가 작성한 게시물이 정보통신망 이용촉진 및 정보보호 등에 관한 법률, 저작권법 등 관련 법령에 위반되는 내용을 포함하는 경우, 권리자는 회사에 관련 법령이 정한 절차에 따라 해당 게시물의 게시 중단 및 삭제 등을 요청할 수 있습니다.\n" +
                        "5. 회사는 권리자의 요청이 없는 경우라도 권리침해가 인정될 만한 사유가 있거나 기타 회사의 정책 및 관련 법령에 위반되는 경우에는 관련 법령에 따라 해당 게시물에 대해 임시조치 등을 취할 수 있습니다. 위와 관련된 세부 절차는 정보통신망법 및 저작권법이 규정한 범위 내에서 회사가 정한 권리침해 신고 절차에 따릅니다.\n" +
                        "\n" +
                        "**제12조(서비스의 이용 및 게시물 관리)**  \n" +
                        "1. 사용자는 서비스를 이용함에 있어 다음 각 호의 행위를 해서는 안 됩니다.\n" +
                        "   - 타인의 저작권, 상표권 등 지적재산권을 침해하는 행위\n" +
                        "   - 타인의 명예를 훼손하거나 프라이버시를 침해하는 행위\n" +
                        "   - 공공질서 및 미풍양속에 반하는 정보를 유포하는 행위\n" +
                        "   - 서비스의 안정적 운영을 방해하거나, 서버 및 네트워크에 부하를 초래하는 행위\n" +
                        "   - 관련 법령에 위반되는 행위\n" +
                        "   - 기타 회사가 불쾌감을 줄 수 있다고 판단되는 행위\n" +
                        "2. 회사는 사용자가 작성한 게시물이 제1항의 각 호에 해당한다고 판단되는 경우 사전 통지 없이 삭제할 수 있으며, 필요한 경우 해당 사용자에게 서비스 이용 제한 조치를 취할 수 있습니다.\n" +
                        "3. 사용자가 작성한 게시물에 대한 권리와 책임은 사용자 본인에게 있으며, 사용자는 언제든지 자신의 게시물을 삭제할 수 있습니다.\n" +
                        "\n" +
                        "#### 제4장 기타\n" +
                        "\n" +
                        "**제13조(청소년 보호)**  \n" +
                        "1. 회사는 모든 연령대가 자유롭게 이용할 수 있는 공간으로서, 유해 정보로부터 청소년을 보호하고 안전한 인터넷 사용을 돕기 위해 정보통신망법에서 정한 청소년 보호정책을 별도로 시행합니다.\n" +
                        "2. 청소년 보호정책에 따라 유해 정보에 대한 접근 제한 및 필터링을 시행하며, 청소년 보호를 위한 관리자 교육 및 유해 콘텐츠의 신고 처리 절차를 운영합니다.\n" +
                        "\n" +
                        "**제14조(면책사항)**  \n" +
                        "1. 회사는 관련 법령상 허용되는 한도 내에서 서비스와 관련하여 본 약관에 명시되지 않은 어떠한 구체적인 사항에 대한 약정이나 보증을 하지 않습니다.\n" +
                        "2. 회사는 이용자가 작성하는 등의 방법으로 서비스에 게재된 정보, 자료, 사실의 신뢰도, 정확성 등에 대해서 보증하지 않으며 회사의 과실 없이 발생된 이용자의 손해에 대해 책임을 부담하지 않습니다.\n" +
                        "\n" +
                        "**제15조(분쟁 해결 및 재판 관할)**  \n" +
                        "1. 회사와 이용자 간의 분쟁이 발생한 경우, 양 당사자는 성실하게 협의하여 분쟁을 해결합니다.\n" +
                        "2. 협의로 분쟁이 해결되지 않을 경우, 회사와 이용자는 관련 법령에 따른 분쟁 조정 기관에 조정을 신청하거나, 법원의 판결에 따릅니다.\n" +
                        "3. 본 약관에 따른 분쟁은 대한민국 법을 준거법으로 하며, 소송은 서울중앙지방법원을 관할 법원으로 합니다.")

            }
        }
    }
}