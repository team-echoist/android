package com.echoist.linkedout.data

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.echoist.linkedout.R
import com.echoist.linkedout.viewModels.WritingViewModel
import java.io.FileNotFoundException

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
    val viewModel = remember { WritingViewModel() }
    Column {
        HashTagBtn(viewModel = viewModel, text = "")
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
@Composable
fun GalleryImagePicker(onImageSelected: (Bitmap) -> Unit) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUri ->
            // 선택한 이미지의 Uri에서 비트맵을 생성합니다.
            val bitmap = uriToBitmap(context, imageUri)
            // 생성된 비트맵을 콜백으로 전달합니다.
            bitmap?.let { onImageSelected(it) }
        }
    }

    Button(onClick = { launcher.launch("image/*") }) {
        Text(text = "갤러리에서 이미지 선택")
    }
}

@Composable
fun ImagePickerScreen() {
    var selectedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GalleryImagePicker { bitmap ->
            selectedImageBitmap = bitmap
        }

        Spacer(modifier = Modifier.height(16.dp))

        selectedImageBitmap?.let { bitmap ->
            // 선택한 이미지를 비트맵으로 표시합니다.
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

// Uri에서 비트맵으로 변환하는 함수
fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {

    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val context = LocalContext.current as Activity
    val fullWidth = context.resources.displayMetrics.widthPixels
    Log.d("width",fullWidth.toString())


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
                navigationIcon = {Icon(imageVector = Icons.Default.Close, contentDescription = "")},
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



