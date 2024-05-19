package com.echoist.linkedout.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun RandomSentences(){
    Scaffold {
        Box(modifier = Modifier.padding(it))
        val annotatedString = buildAnnotatedString {
            append("이원영은 초파리를 좋아했다. ")

            withStyle(style = SpanStyle(color = Color.White, background = Color.Green, fontSize = 18.sp, fontWeight = FontWeight.Bold)) {
                append(" 빗소리가 커서 괜찮지 않냐고 물었지만 사실 너무 커서 무서웠다. ")
            }

            append(" text. And this is ")

            withStyle(style = SpanStyle(color = Color.Black, background = Color.Yellow, fontStyle = FontStyle.Italic)) {
                append("살짝 열린 창문 사이로 몇분 전 내가 힘겹게 올라온 비탈이 보였다.")
            }

            append(" text.")
        }

        BasicText(text = annotatedString, style = MaterialTheme.typography.bodyLarge)

    }



}

@Composable
fun StartSentence(){
    Surface(
        shape = RoundedCornerShape(50),
        color = Color.Blue,
        modifier = Modifier.height(30.dp)
    ) {
        Box(
            modifier = Modifier
                .wrapContentHeight(Alignment.CenterVertically)
                .wrapContentWidth(Alignment.CenterHorizontally)
        ) {
            Text(text = "이원영은 초파리를 좋아했다.",Modifier.padding(start = 10.dp, end = 10.dp))
        }
    }
}