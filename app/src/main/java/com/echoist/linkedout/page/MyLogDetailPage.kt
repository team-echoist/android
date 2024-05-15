package com.echoist.linkedout.page

import MyLogView1Model
import MyLogViewModel
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.components.LastEssayPager
import com.echoist.linkedout.ui.theme.LinkedOutTheme

@Preview
@Composable
fun pre(){
    MyLogDetailPage(navController = NavController(LocalContext.current), viewModel = MyLogViewModel())
}
@Composable
fun MyLogDetailPage(navController : NavController,viewModel: MyLogView1Model){
    val scrollState = rememberScrollState()

    LinkedOutTheme {
        Scaffold(
            topBar = {
                DetailTopAppBar(navController = navController,viewModel)
            },
            content = {
                Box(
                    Modifier
                        .padding(it)
                        .fillMaxSize()
                , contentAlignment = Alignment.TopCenter) {
                    Column(Modifier.verticalScroll(scrollState)) {
                        DetailEssay(viewModel = viewModel)
                        LastEssayPager(viewModel = viewModel, navController = navController)
                    }
                    //수정 옵션
                    AnimatedVisibility(
                        visible = viewModel.isActionClicked,
                        enter = fadeIn(animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)),
                        exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(end = 23.dp),
                            contentAlignment = Alignment.TopEnd
                        ){
                            ModifyOption()
                        }
                    }

                }
            }
        )
    }

}

@Composable
fun ModifyOption(){
    Surface(modifier = Modifier.size(180.dp,250.dp), shape = RoundedCornerShape(2)){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                Icon(painter = painterResource(id = R.drawable.text_minus), contentDescription = "minus")
                Spacer(modifier = Modifier.width(30.dp))
                Text(text = "가")
                Spacer(modifier = Modifier.width(30.dp))
                Icon(painter = painterResource(id = R.drawable.text_plus), contentDescription = "minus")

            }
            HorizontalDivider()
            OptionItem(text = "수정",Color.White)
            HorizontalDivider()
            OptionItem(text = "발행",Color.White)
            HorizontalDivider()
            OptionItem(text = "Linked-out",Color.White)
            HorizontalDivider()
            OptionItem(text = "삭제",Color.Red)

        }
    }
}
@Composable
fun OptionItem(text : String,color: Color){
    Box(modifier =Modifier.size(180.dp,44.dp)){
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 20.dp,
                end = 20.dp
            ), contentAlignment = Alignment.CenterStart){
            Text(text = text, fontSize = 16.sp,color=color)
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 20.dp,
                end = 20.dp
            ), contentAlignment = Alignment.CenterEnd){
            Icon(tint = color,painter = painterResource(id = R.drawable.ftb_edit), contentDescription = "", modifier = Modifier.size(20.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopAppBar(navController: NavController,viewModel: MyLogView1Model){

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = { },
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "arrow back",
                tint = if (isSystemInDarkTheme()) Color(0xFF727070) else Color.Gray,
                modifier = Modifier

                    .padding(start = 20.dp)
                    .clickable {
                        if (viewModel.detailEssayBackStack.isNotEmpty()) {
                            viewModel.detailEssayBackStack.pop()
                            Log.d(TAG, "pushpushpop: ${viewModel.detailEssayBackStack}")
                            if (viewModel.detailEssayBackStack.isNotEmpty()) {
                                viewModel.detailEssay = viewModel.detailEssayBackStack.peek()
                            }
                        }

                        navController.popBackStack()
                        viewModel.isActionClicked = false


                    } //뒤로가기
            )
        },
        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.more),
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 20.dp)
                    .clickable {
                        viewModel.isActionClicked = !viewModel.isActionClicked
                    },
            )
        })



}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailEssay(viewModel: MyLogView1Model){
    val essay = viewModel.detailEssay
    Box{
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)) {
            Spacer(modifier = Modifier.height(170.dp))
            Text(text = essay.title, fontSize = 24.sp, modifier = Modifier)
            Spacer(modifier = Modifier.height(40.dp))
            Text(text = essay.content, fontSize = 16.sp, modifier = Modifier, color = Color(0xFFB4B4B4))
            Spacer(modifier = Modifier.height(46.dp))
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd){
                Column {
                    Text(text = "구루브", fontSize = 12.sp, textAlign = TextAlign.End, color = Color(0xFF686868))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = essay.createdDate, fontSize = 12.sp, textAlign = TextAlign.End, color = Color(0xFF686868))

                }
            }

        }
        if (essay.thumbnail != null){
            GlideImage(model = essay.thumbnail, contentDescription = "", modifier = Modifier
                .fillMaxWidth()
                .height(220.dp))
        }
    }

}










