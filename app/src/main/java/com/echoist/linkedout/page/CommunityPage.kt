package com.echoist.linkedout.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import me.saket.extendedspans.ExtendedSpans
import me.saket.extendedspans.RoundedCornerSpanPainter
import me.saket.extendedspans.drawBehind

@OptIn(ExperimentalLayoutApi::class, ExperimentalWearMaterialApi::class)
@Preview
@Composable
fun RandomSentences(){
    Scaffold {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray))

        Box(modifier = Modifier.padding(it))
        val annotatedString = buildAnnotatedString {
            withStyle(style = ParagraphStyle(lineHeight = 40.sp)) { // 원하는 줄 간격 설정
                withStyle(style = SpanStyle(
                    color = Color.White,
                    background = Color.Black,
                    fontSize = 14.sp,
                )) {
                    append("이원영은 초파리를 좋아했다.       ")
                }


                withStyle(style = SpanStyle(
                    color = Color.Black,
                    background = Color.White,
                    fontSize = 14.sp,
                )) {
                    append("빗소리가 커서 괜찮지 않냐고 물었지만 사실 너무 커서 무서웠다. 무서웠지만? 무서웠어        ")
                }


                withStyle(style = SpanStyle(
                    color = Color.White,
                    background = Color.Black,
                    fontSize = 14.sp,
                )) {
                    append("살짝 열린 창문 사이로 몇분 전 내가 비탈이 보였다.        ")
                }


                withStyle(style = SpanStyle(
                    color = Color.Black,
                    background = Color.White,
                    fontSize = 14.sp,
                )) {
                    append(" 빗소리가 커서 괜찮지 않냐 너무 커서 무서웠다.         ")
                }
            }
        }

        //BasicText(text = annotatedString, style = MaterialTheme.typography.bodyLarge)


        val extendedSpans = remember {
            ExtendedSpans(
                RoundedCornerSpanPainter(
                    cornerRadius = 20.sp,
                    topMargin = 0.sp,
                    bottomMargin = 0.sp,
                    padding = RoundedCornerSpanPainter.TextPaddingValues(
                        horizontal = 14.sp,
                        vertical = 0.sp
                    )
                )
            )
        }

        Box(
            modifier = Modifier
                .background(Color.LightGray)
        )
                //todo 새로고침 api 요청 후 ui 갱신

        {
            Text(
                modifier = Modifier
                    .drawBehind(extendedSpans)
                    .padding(start = 10.dp, end = 2.dp),
                text = remember("text") {
                    extendedSpans.extend(annotatedString)
                },
                onTextLayout = { result ->
                    extendedSpans.onTextLayout(result)
                }
            )
        }






    }



}
