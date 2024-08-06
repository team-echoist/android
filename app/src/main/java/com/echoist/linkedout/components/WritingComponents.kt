package com.echoist.linkedout.components

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DensitySmall
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.echoist.linkedout.R
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.WritingViewModel

class FuncItemData(val text : String, var icon: Int, var clickable: () -> Unit )

@Composable
fun BlankWarningAlert(dialogState: MutableState<Boolean>){
    LinkedOutTheme {
        AlertDialog(
            onDismissRequest = { dialogState.value = false },
            confirmButton = {
                Button(onClick = { dialogState.value = false }) {
                    Text(text = "확인")
                }
            },
            text = { Text(text = "제목, 또는 내용이 10자 이하면 완료 할 수 없습니다.") },
        )
    }

}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun prevItem(){
    Column {

        FuncItem("인용구",R.drawable.keyboard_hashtag) {}
    }

}
@Composable
fun FuncItem(text : String, icon: Int, clickable: () -> Unit){
    val color = Color.Unspecified

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = icon),
            text,
            Modifier
                .size(30.dp)
                .clickable {
                    clickable()
                    Log.d("tagtag", "tag22")
                },
            color
        )
        Spacer(modifier = Modifier.height(23.dp))
        Text(text = text, color = Color.White,
            fontSize = 12.sp,
            textAlign = TextAlign.Center)


    }
}
@Composable
fun TextItem(icon : Int, color: Color, clickable : () -> Unit){


    Icon(
        painter = painterResource(id = icon),
        contentDescription = "icon",
        tint = color,
        modifier = Modifier
            .padding(end = 14.dp)
            .size(30.dp)
            .clickable { clickable() }
    )
}
@Composable
fun LocationTextField(viewModel: WritingViewModel){

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart){
        TextField(
            value = viewModel.locationText,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent


            ),
            placeholder = {
                Text(text = "장소를 입력하고 줄을 띄워주세요", color = Color.Gray, fontSize = 14.sp)
                          },
            onValueChange = { it ->
                if (it.isNotEmpty() && (it.last() == '\n') && viewModel.locationList.size < 1) {
                    val trimmedText = it.trim()
                    if (trimmedText.isNotBlank()) {
                        viewModel.locationList.add(trimmedText)
                        viewModel.locationText = ""
                    }
                }
                else viewModel.locationText = it
            }
        )
    }


}
@Composable
fun LocationBox(viewModel: WritingViewModel){
    Button(modifier = Modifier.padding(bottom = 15.dp),
        onClick = {
        }) {
        Text(text =  "${viewModel.longitude} ${viewModel.latitude}", fontSize = 14.sp, color = Color.White)
    }

}
@Composable
fun LocationBtn(viewModel: WritingViewModel,text: String){
    Button(modifier = Modifier.padding(bottom = 15.dp),
        onClick = {
            viewModel.locationList.remove(text)
        }) {
        Text(text = text, fontSize = 14.sp, color = Color.White)
        Spacer(modifier = Modifier.width(2.dp))
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "",
            modifier = Modifier.size(16.dp),
            tint = Color.White
        )
    }
}

@Composable
fun HashTagTextField(viewModel: WritingViewModel) {
    TextField(
        value = viewModel.hashTagText,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
        ),
        onValueChange = {//태그개수 4개 제한
            if (it.isNotEmpty() && (it.last() == ' ' || it.last() == '\n')) {
                val trimmedText = it.trim()
                if (trimmedText.isNotBlank()) {
                    viewModel.hashTagList.add(trimmedText)
                    Log.d(TAG, "HashTagTextField: ${viewModel.hashTagText}")

                    viewModel.hashTagText = ""
                    Log.d(TAG, "HashTagTextField: ${viewModel.hashTagList}")
                }
            } else {
                viewModel.hashTagText = it
            }
        }
    )
}

@Composable
fun LocationGroup(viewModel: WritingViewModel){
    val scrollState = rememberScrollState()
    Box(modifier = Modifier
        .height(55.dp)
        .fillMaxWidth()
        .padding(horizontal = 20.dp)){
        Image( painter = painterResource(id = R.drawable.group_location),
            contentDescription = "hashtagGroup")
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ){
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 86.dp)
                        .width(220.dp)
                        .horizontalScroll(scrollState),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    viewModel.locationList.forEach {
                        Text(text = it, fontSize = 14.sp, color = Color.White)
                        Spacer(modifier = Modifier.width(13.dp))
                    }
                }
                Text( modifier = Modifier
                    .padding(start = 86.dp)
                    .width(220.dp),
                    text = "${viewModel.longitude} ${viewModel.latitude}",
                    fontSize = 12.sp,
                    color = Color(0xFFA8AEE4))

            }


        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterEnd
        ){
            Text(
                fontSize = 16.sp,
                text = "편집",
                color = Color.White,
                modifier = Modifier
                    .padding(end = 11.5.dp)
                    .clickable {
                        viewModel.isLocationClicked = true
                        viewModel.isTextFeatOpened.value = true
                    }
            )
        }
    }

}

@Composable
fun HashTagBtn(viewModel: WritingViewModel,text: String){

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier
        .padding(bottom = 15.dp)
        .clickable { viewModel.hashTagList.remove(text) }
        .background(color = Color(0xFF616FED), shape = RoundedCornerShape(size = 65.dp))
        .padding(start = 8.dp, top = 3.dp, end = 8.dp, bottom = 3.dp)) {
        Text(text = text, fontSize = 14.sp, color = Color.White)
        Spacer(modifier = Modifier.width(2.dp))
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "",
            modifier = Modifier.size(16.dp),
            tint = Color.White
        )
    }
//    Button(modifier = Modifier.padding(bottom = 15.dp),
//        onClick = {
//            viewModel.hashTagList.remove(text)
//        }) {
//        Text(text = text, fontSize = 14.sp, color = Color.White)
//        Spacer(modifier = Modifier.width(2.dp))
//        Icon(
//            imageVector = Icons.Default.Close,
//            contentDescription = "",
//            modifier = Modifier.size(16.dp),
//            tint = Color.White
//        )
//    }
}

@Composable
fun HashTagGroup(viewModel: WritingViewModel){
    val scrollState = rememberScrollState()
    Box(modifier = Modifier
        .height(55.dp)
        .fillMaxWidth()
        .padding(horizontal = 20.dp)){
        Image( painter = painterResource(id = R.drawable.group_hashtag),
            contentDescription = "hashtagGroup")
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ){

            Row(
                modifier = Modifier
                    .padding(start = 86.dp)
                    .width(220.dp)
                    .fillMaxHeight()
                    .horizontalScroll(scrollState),
                verticalAlignment = Alignment.CenterVertically
            ){
                viewModel.hashTagList.forEach {
                    Text(text = "#$it", color = Color.White)
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
                color = Color.White,
                modifier = Modifier
                    .padding(end = 11.5.dp)
                    .clickable {
                        viewModel.isHashTagClicked = true
                        viewModel.isTextFeatOpened.value = true
                    }
            )
        }
    }

}

//사진 자르기
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun CropImagePage(navController: NavController, viewModel: WritingViewModel) {

    var imageUri: Uri? by remember { mutableStateOf(viewModel.imageUri) }
    val context = LocalContext.current as Activity
    val fullWidth = context.resources.displayMetrics.widthPixels
    Log.d("width", fullWidth.toString())

    val imageCropLauncher =
        rememberLauncherForActivityResult(contract = CropImageContract()) { result ->
            if (result.isSuccessful) {
                result.uriContent?.let { uri ->
                    imageUri = uri
                }
            } else {
                // Handle error if cropping fails
                println("ImageCropping error: ${result.error}")
            }
        }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    var isImageExist by remember { mutableStateOf(false) }

    LinkedOutTheme {
        Scaffold(
            topBar = {
                TopAppBar(modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .background(Color.Black),
                    navigationIcon = {
                        Icon(
                            modifier = Modifier
                                .size(30.dp)
                                .clickable { navController.popBackStack() },
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Close"
                        )
                    },
                    title = { Text("  이미지 업로드",fontSize = 24.sp, color = Color.White) },
                    actions = {
                        IconButton(
                            onClick = {
                                val cropOptions = CropImageContractOptions(
                                    null,
                                    CropImageOptions(
                                        imageSourceIncludeCamera = false,
                                        minCropResultWidth = screenWidth *5,
                                        minCropResultHeight = 1000,
                                        maxCropResultWidth = screenWidth *10,
                                        maxCropResultHeight = 1000,
                                        cropperLabelText = "자르기"
                                    )
                                )
                                imageCropLauncher.launch(cropOptions)
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                tint = Color.White,
                                painter = painterResource(id = R.drawable.icon_gallery),

                                contentDescription = "Background from gallery"
                            )
                        }

                        IconButton(
                            onClick = {
                                val cropOptions = CropImageContractOptions(
                                    null,
                                    CropImageOptions(
                                        imageSourceIncludeGallery = false,
                                        minCropResultWidth = screenWidth *5,
                                        minCropResultHeight = 1000,
                                        maxCropResultWidth = screenWidth *10,
                                        maxCropResultHeight = 1000,
                                        cropperLabelText = "자르기"
                                    )
                                )
                                imageCropLauncher.launch(cropOptions)
                            }
                        ) {
                            Icon(

                                painter = painterResource(id = R.drawable.icon_camera),
                                modifier = Modifier.size(30.dp),
                                tint = Color.White,
                                contentDescription = "Background from camera"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                imageUri?.let { uri ->
                    viewModel.imageUri = uri
                    GlideImage(model = viewModel.imageUri, contentDescription = "",modifier = Modifier.fillMaxSize())


                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "close",
                        modifier = Modifier
                            .size(40.dp)
                            .offset(x = 10.dp, y = 10.dp)
                            .clickable {
                                viewModel.imageUri = null
                                viewModel.imageUrl = null
                                imageUri = null
                            }
                    )
                    Log.d(TAG, "CropImagePage: $uri")
                    isImageExist = true
                }
                if (imageUri == null) {
                    isImageExist = false
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 30.dp)
                        , contentAlignment = Alignment.Center){
                        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                tint = Color(0xFF4B4B4B),
                                contentDescription = "image upload",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable
                                    {
                                        val cropOptions = CropImageContractOptions(
                                            null,
                                            CropImageOptions(
                                                imageSourceIncludeCamera = false,
                                                minCropResultWidth = screenWidth * 5,
                                                minCropResultHeight = 1000,
                                                maxCropResultWidth = screenWidth * 10,
                                                maxCropResultHeight = 1000,
                                                cropperLabelText = "자르기"
                                            )
                                        )
                                        imageCropLauncher.launch(cropOptions)
                                    }

                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "사진을 업로드 해주세요" , fontSize = 18.sp, color = Color(0xFF4B4B4B), fontWeight = FontWeight.SemiBold)
                        }
                    }

                }
            }
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(bottom = 30.dp), contentAlignment = Alignment.BottomCenter) {
            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(60.dp), enabled = isImageExist,
                shape = RoundedCornerShape(10),
                colors = ButtonDefaults.buttonColors(containerColor = LinkedInColor, disabledContainerColor = Color.Gray),
                onClick = {
                // Pass the imageUri to ViewModel or use it as needed
                navController.navigate("WritingPage")
            }) {
                Text(text = "완료", color = Color.Black)
            }
        }
    }

}


//임시저장 개수 아이콘
@Composable
fun StoryCountIcon(count : Int){
    val color = Color.White
    val circleColor = Color.White

    Box(modifier = Modifier.size(36.dp)){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart){
            Icon(imageVector = Icons.Default.DensitySmall, contentDescription = "", modifier = Modifier.size(34.dp),color)
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd){
            Surface(shape = CircleShape, modifier = Modifier.size(18.dp), color = circleColor) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text(text = count.toString(),color = color)

                }
            }
        }
    }
}

