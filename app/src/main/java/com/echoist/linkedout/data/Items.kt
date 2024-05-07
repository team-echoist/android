package com.echoist.linkedout.data

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echoist.linkedout.R
import com.echoist.linkedout.viewModels.WritingViewModel

class FuncItemData(val text : String, var icon: ImageVector, var clickable: () -> Unit )


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun prevItem(){
    FuncItem("인용구",Icons.Default.Done){}
}

@Composable
fun FuncItem(text : String, icon: ImageVector,clickable: () -> Unit){
    val color =  if (isSystemInDarkTheme()) Color.White else Color.Black

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            text,
            Modifier
                .size(26.dp)
                .clickable {
                    clickable()
                    Log.d("tagtag", "tag22")
                },
            color
        )
        Spacer(modifier = Modifier.height(23.dp))
        Text(text = text, color = color,
            fontSize = 12.sp,
            textAlign = TextAlign.Center)


    }
}
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun prevtextItem(){
    TextItem(R.drawable.social_googlebtn){}
}

@Composable
fun TextItem(icon : Int, clickable : () -> Unit){
    val color =  if (isSystemInDarkTheme()) Color.White else Color.Black

    Icon(
        painter = painterResource(id = icon),
        contentDescription = "icon",
        tint = color,
        modifier = Modifier
            .size(30.dp)
            .padding(end = 14.dp)
            .clickable { clickable() }
    )
}


@Composable
fun HashTagTextField(viewModel: WritingViewModel){

    TextField(
        value = viewModel.hashTagText,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = if (isSystemInDarkTheme()) Color.Transparent else Color.Black,
            unfocusedContainerColor = if (isSystemInDarkTheme()) Color.Transparent else Color.Black


        ),
        onValueChange = {
            if (it.isNotEmpty() && (it.last() == ' ' || it.last() == '\n')) {
                viewModel.hashTagList.add(viewModel.hashTagText.trim())
                viewModel.hashTagText = ""
                // 입력값의 마지막 문자가 스페이스바 혹은 엔터인 경우 처리
                // 이 부분에서 원하는 동작을 수행하십시오.
            }
            else viewModel.hashTagText = it
        }
    )

}
@Preview
@Composable
fun prevbtn(){
    HashTagBtn(WritingViewModel(),"")
}

@Composable
fun HashTagBtn(viewModel: WritingViewModel,text: String){
    val hashTag by remember {
        mutableStateOf("")
    }
    Button(onClick = {
        viewModel.hashTagList.remove(text)
    }) {
        Text(text = text, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(2.dp))
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "",
            modifier = Modifier.size(16.dp)
        )
    }
}
