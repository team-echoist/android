package com.echoist.linkedout.data

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
                val trimmedText = it.trim()
                if (trimmedText.isNotBlank()) {
                    viewModel.hashTagList.add(trimmedText)
                    viewModel.hashTagText = ""
                }
            }
            else viewModel.hashTagText = it
        }
    )

}
@Preview
@Composable
fun prev(){
    Column {

        HashTagGroup(WritingViewModel())
        HashTagBtn(viewModel = WritingViewModel(), text = "qjxms")
        HashTagTextField(viewModel = WritingViewModel())
    }
}

@Composable
fun HashTagBtn(viewModel: WritingViewModel,text: String){
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

@Composable
fun HashTagGroup(viewModel: WritingViewModel){
    val scrollState = rememberScrollState()
    Box(modifier = Modifier.size(350.dp,50.dp)){
        Image( painter = painterResource(id = R.drawable.hashtag_group),
            contentDescription = "hashtagGroup")
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ){

            Row(
                modifier = Modifier
                    .padding(start = 74.dp)
                    .width(220.dp)
                    .fillMaxHeight()
                    .horizontalScroll(scrollState),
                verticalAlignment = Alignment.CenterVertically
            ){
                viewModel.hashTagList.forEach {
                    Text(text = "#$it")
                    Spacer(modifier = Modifier.width(13.dp))
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterEnd
        ){
            Text(
                fontSize = 16.sp,
                text = "편집",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(end = 11.5.dp)
                    .clickable { /* todo 해시태그 편집기능 */ }
            )
        }
    }

}
