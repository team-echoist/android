package com.echoist.linkedout.components

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.echoist.linkedout.R
import com.echoist.linkedout.page.CompleteAppBar
import com.echoist.linkedout.page.CompleteContents
import com.echoist.linkedout.page.CompleteDate
import com.echoist.linkedout.page.CompleteNickName
import com.echoist.linkedout.page.CompleteTitle
import com.echoist.linkedout.page.WritingCompletePager
import com.echoist.linkedout.page.WritingDeleteCard
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.WritingViewModel

class FuncItemData(val text : String, var icon: Int, var clickable: () -> Unit )


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun prevItem(){
    Column {

        FuncItem("인용구",R.drawable.keyboard_hashtag) {}
        TextItem(R.drawable.social_googlebtn){}
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
            placeholder = { Text(text = "장소 이름을 입력해주세요.", color = Color.Gray, fontSize = 12.sp)},
            onValueChange = {
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
        Text(text =  "${viewModel.longitude} ${viewModel.latitute}", fontSize = 14.sp)
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
                    text = "${viewModel.longitude} ${viewModel.latitute}",
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
                    .clickable { /* todo 해시태그 편집기능 */ }
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
                    .clickable { /* todo 해시태그 편집기능 */ }
            )
        }
    }

}

//사진 자르기
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {

    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val context = LocalContext.current as Activity
    val fullWidth = context.resources.displayMetrics.widthPixels
    Log.d("width", fullWidth.toString())


    val imageCropLauncher =
        rememberLauncherForActivityResult(contract = CropImageContract()) { result ->
            if (result.isSuccessful) {
                result.uriContent?.let {

                    //getBitmap method is deprecated in Android SDK 29 or above so we need to do this check here
                    bitmap = if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images
                            .Media.getBitmap(context.contentResolver, it)
                    } else {
                        val source = ImageDecoder
                            .createSource(context.contentResolver, it)
                        ImageDecoder.decodeBitmap(source)
                    }
                }

            } else {
                //If something went wrong you can handle the error here
                println("ImageCropping error: ${result.error}")
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = ""
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
                                    maxCropResultHeight = 700, //todo 이 값 정해야할듯..
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
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun BottomSheetContent(isExpanded: Boolean, onCloseSheet: () -> Unit) {
    if (isExpanded) {
        Column {
            Text(text = "Expanded Bottom Sheet Content")
            // Add other composables as needed
        }
    } else {
        Column {
            Text(text = "Collapsed Bottom Sheet Content")
            // Add other composables as needed
            Button(onClick = onCloseSheet) {
                Text(text = "Close Bottom Sheet")
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDemo() {
    val scaffoldState = rememberBottomSheetScaffoldState()
    var isExpanded by remember { mutableStateOf(false) }


}

@Preview
@Composable
fun PreviewBottomSheetDemo() {
    CompletePage(navController = rememberNavController(), viewModel = WritingViewModel(), accessToken = "")
}







