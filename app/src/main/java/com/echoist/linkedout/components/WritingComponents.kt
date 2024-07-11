package com.echoist.linkedout.components

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DensitySmall
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import com.echoist.linkedout.viewModels.WritingViewModel

class FuncItemData(val text : String, var icon: Int, var clickable: () -> Unit )

@Composable
fun BlankWarningAlert(dialogState: MutableState<Boolean>){
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


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun prevItem(){
    Column {

        FuncItem("인용구",R.drawable.keyboard_hashtag) {}
    }

}
@Composable
fun FuncItem(text : String, icon: Int, clickable: () -> Unit){
    val color =  if (isSystemInDarkTheme()) Color.White else Color.Black

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = icon),
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
@Composable
fun TextItem(icon : Int, color: Color, clickable : () -> Unit){


    Icon(
        painter = painterResource(id = icon),
        contentDescription = "icon",
        tint = color,
        modifier = Modifier
            .size(40.dp)
            .padding(end = 14.dp)
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
                focusedContainerColor = if (isSystemInDarkTheme()) Color.Transparent else Color.Black,
                unfocusedContainerColor = if (isSystemInDarkTheme()) Color.Transparent else Color.Black


            ),
            placeholder = {
                Text(text = "장소를 입력하고 줄을 띄워주세요", color = Color.Gray, fontSize = 12.sp)
                          },
            onValueChange = { it ->
                if (it.isNotEmpty() && (it.last() == '\n')) {
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
        Text(text =  "${viewModel.longitude} ${viewModel.latitude}", fontSize = 14.sp)
    }

}
@Composable
fun LocationBtn(viewModel: WritingViewModel,text: String){
    Button(modifier = Modifier.padding(bottom = 15.dp),
        onClick = {
            viewModel.locationList.remove(text)
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
fun HashTagTextField(viewModel: WritingViewModel) {
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
        onValueChange = {//태그개수 4개 제한
            if (it.isNotEmpty() && (it.last() == ' ' || it.last() == '\n')) {
                val trimmedText = it.trim()
                if (trimmedText.isNotBlank() && viewModel.hashTagList.size < 4) {
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
    Box(modifier = Modifier.size(350.dp,50.dp)){
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
                        .padding(start = 74.dp)
                        .width(220.dp)
                        .horizontalScroll(scrollState),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    viewModel.locationList.forEach {
                        Text(text = it)
                        Spacer(modifier = Modifier.width(13.dp))
                    }
                }
                Text( modifier = Modifier
                    .padding(start = 74.dp)
                    .width(220.dp),
                    text = "${viewModel.longitude} ${viewModel.latitude}",
                    fontSize = 12.sp,
                    color = Color.Gray)

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
    Button(modifier = Modifier.padding(bottom = 15.dp),
        onClick = {
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
        Image( painter = painterResource(id = R.drawable.group_hashtag),
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

    var imageUri: Uri? by remember { mutableStateOf(null) }
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

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                },
                title = { Text("Crop Image") },
                actions = {
                    IconButton(
                        onClick = {
                            val cropOptions = CropImageContractOptions(
                                null,
                                CropImageOptions(
                                    imageSourceIncludeCamera = false,
                                    minCropResultWidth = 1500,
                                    minCropResultHeight = 700,
                                    maxCropResultWidth = 1500,
                                    maxCropResultHeight = 700,
                                    activityMenuIconColor = android.graphics.Color.RED,
                                    cropMenuCropButtonIcon = R.drawable.keyboard_hashtag,
                                    cropperLabelText = "안녕하십니까?",
                                    cropperLabelTextColor = android.graphics.Color.RED,
                                    toolbarTitleColor = android.graphics.Color.RED,
                                    activityBackgroundColor = android.graphics.Color.BLUE,
                                    cropMenuCropButtonTitle = "김김김"
                                )
                            )
                            imageCropLauncher.launch(cropOptions)
                        }
                    ) {
                        Icon(
                            Icons.Filled.Image,
                            contentDescription = "Background from gallery"
                        )
                    }

                    IconButton(
                        onClick = {
                            val cropOptions = CropImageContractOptions(
                                null,
                                CropImageOptions(
                                    imageSourceIncludeGallery = false,
                                    minCropResultWidth = 2000,
                                    minCropResultHeight = 700,
                                    maxCropResultWidth = 2000,
                                    maxCropResultHeight = 700,
                                    cropperLabelText = "자르기234"
                                )
                            )
                            imageCropLauncher.launch(cropOptions)
                        }
                    ) {
                        Icon(
                            Icons.Filled.CameraEnhance,
                            contentDescription = "Background from camera"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            imageUri?.let { uri ->
                GlideImage(model = uri, contentDescription = "",modifier = Modifier.fillMaxSize())
                viewModel.imageUri = uri
                Log.d(TAG, "CropImagePage: $uri")
                
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Button(onClick = {
            // Pass the imageUri to ViewModel or use it as needed
            navController.navigate("WritingPage")
        }) {
            Text(text = "완료")
        }
    }
}


//임시저장 개수 아이콘
@Composable
fun StoryCountIcon(count : Int){
    val color = if (isSystemInDarkTheme()) Color.White else Color.Black
    val circleColor = if (isSystemInDarkTheme()) Color.Black else Color.White

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

