package com.echoist.linkedout.page.myLog

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.Routes
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.components.BlankWarningAlert
import com.echoist.linkedout.components.FuncItem
import com.echoist.linkedout.components.FuncItemData
import com.echoist.linkedout.components.HashTagBtn
import com.echoist.linkedout.components.HashTagGroup
import com.echoist.linkedout.components.HashTagTextField
import com.echoist.linkedout.components.LocationBox
import com.echoist.linkedout.components.LocationBtn
import com.echoist.linkedout.components.LocationGroup
import com.echoist.linkedout.components.LocationTextField
import com.echoist.linkedout.components.TextItem
import com.echoist.linkedout.gps.RequestPermissionsUtil
import com.echoist.linkedout.page.login.Keyboard
import com.echoist.linkedout.page.login.keyboardAsState
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.WritingViewModel
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import kotlinx.coroutines.delay

object Token {
    var accessToken: String = "EMPTYTOKEN"
}

@Preview
@Composable
fun PrevWritingPage() {
    val viewModel: WritingViewModel = viewModel()
    WritingPage(
        navController = rememberNavController(),
        viewModel = viewModel
    )

}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WritingPage(
    navController: NavController,
    viewModel: WritingViewModel,
) {

    val isKeyBoardOpened by keyboardAsState()
    val scrollState = rememberScrollState()
    val background = Color.Black

    var isTextSettingSelected by remember { mutableStateOf(false) }
    var isTextBoldSelected by remember { mutableStateOf(false) }
    var isTextUnderLineSelected by remember { mutableStateOf(false) }
    var isTextMiddleLineSelected by remember { mutableStateOf(false) }


    //리치텍스트
    val textState = rememberRichTextState()
    val currentSpanStyle = textState.currentSpanStyle
    val isBold = currentSpanStyle.fontWeight == FontWeight.Bold
    val isItalic = currentSpanStyle.fontStyle == FontStyle.Italic
    val isUnderline = currentSpanStyle.textDecoration == TextDecoration.Underline


    Log.d(TAG, "WritingPage: ${viewModel.readDetailEssay()}")

    LaunchedEffect(key1 = Unit) {
        textState.setHtml(viewModel.content)
    }

    LaunchedEffect(key1 = viewModel.isStored) {
        if (viewModel.isStored){
            delay(2000)
            viewModel.isStored = false
        }
    }

        LinkedOutTheme {
            Box {
                Column(
                    modifier = Modifier
                        .background(background)
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    )
                    {
                        WritingTopAppBar(
                            navController = navController,
                            viewModel,
                            textState
                        ){viewModel.content = textState.toHtml()}


                        ContentTextField(viewModel = viewModel,textState)
                        Log.d(TAG, "WritingTopAppBar: ${textState.toHtml()}")


                        Spacer(modifier = Modifier.height(50.dp))

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Box{
                                GlideImage(model = viewModel.imageUri ?: viewModel.imageUrl , contentDescription = "uri", contentScale = ContentScale.Crop)
                                Log.d(TAG, "WritingPage: ${viewModel.imageUri}, ${viewModel.imageUrl}")
                                if (viewModel.imageUri != null || (viewModel.imageUrl != null && viewModel.imageUrl!!.startsWith("https"))){ //image url 주소가 널이 아니고 https값으로 시작해야 제대로된 Url link
                                    Row( //변경버튼 클릭 시 화면이동
                                        Modifier
                                            .offset(x = 10.dp, y = 10.dp)
                                            .width(57.dp)
                                            .height(32.dp)
                                            .clickable { navController.navigate("CropImagePage") }
                                            .background(color = Color(0xFF616FED)), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                        Text(text = "변경", fontSize = 16.sp, color = Color.White)
                                    }
                                }


                            }
                        }


                    }

                }
                //취소 클릭시 배경 어둡게
                if (viewModel.isCanCelClicked.value)
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.7f)))

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) { //키보드 자리에 들어갈 컴포 넣기
                    Column {
                        AnimatedVisibility(visible = viewModel.isCanCelClicked.value,
                            enter = slideInVertically(
                                initialOffsetY = { 2000 },
                                animationSpec = tween(durationMillis = 500)
                            ),
                            exit = slideOutVertically(
                                targetOffsetY = { 2000 },
                                animationSpec = tween(durationMillis = 500)
                            )
                        ) {
                            WritingCancelCard(viewModel = viewModel, navController = navController){viewModel.isStored = true}
                        }
                        //장소 찍는
                        if (viewModel.longitude != null && viewModel.latitude != null && viewModel.isTextFeatOpened.value) {
                            if (viewModel.isLocationClicked) {
                                Row {
                                    LocationBox(viewModel = viewModel)
                                    Row(
                                        Modifier
                                            .padding(horizontal = 10.dp)
                                            .horizontalScroll(rememberScrollState())) {
                                        viewModel.locationList.forEach {
                                            LocationBtn(viewModel = viewModel, text = it)
                                        }
                                    }
                                }
                            }

                        } else if (isKeyBoardOpened == Keyboard.Closed && !viewModel.isTextFeatOpened.value) {
                            if (viewModel.locationList.isNotEmpty() && viewModel.longitude != null) { //직접 입력한 장소가 존재해야함.
                                LocationGroup(viewModel = viewModel)
                                Spacer(modifier = Modifier.height(4.dp))
                                if (viewModel.hashTagList.isEmpty()){
                                    Spacer(modifier = Modifier.height(80.dp))
                                }
                            }
                        }
                        //해시태그 찍는
                        if (viewModel.hashTagList.isNotEmpty() && viewModel.isTextFeatOpened.value) {
                            if (viewModel.isHashTagClicked) {
                                Row(
                                    Modifier
                                        .horizontalScroll(rememberScrollState())
                                        .padding(horizontal = 20.dp)) {
                                    viewModel.hashTagList.forEach {
                                        HashTagBtn(viewModel = viewModel, text = it)
                                        Spacer(modifier = Modifier.width(6.dp))
                                    }
                                }
                            }

                        } else if (isKeyBoardOpened == Keyboard.Closed && !viewModel.isTextFeatOpened.value)
                            if (viewModel.hashTagList.isNotEmpty()) {
                                Column {
                                    HashTagGroup(viewModel = viewModel)
                                    Spacer(modifier = Modifier.height(80.dp))
                                }
                            }

                        if (isKeyBoardOpened == Keyboard.Opened || viewModel.isTextFeatOpened.value) {


                            if (isTextSettingSelected) {
                                TextSettingsBar(textState)
                            }
                            TextEditBar(viewModel,
                                isTextSettingSelected = isTextSettingSelected,
                                onTextSettingSelected = {
                                    isTextSettingSelected = it
                                },
                                isTextBoldSelected = isTextBoldSelected,
                                onTextBoldSelected =
                                {
                                    isTextBoldSelected = it
                                    if (isTextBoldSelected) textState.addSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
                                    else textState.removeSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
                                    isTextSettingSelected = false
                                },
                                isTextUnderLineSelected = isTextUnderLineSelected,
                                onTextUnderLineSelected =
                                {
                                    isTextUnderLineSelected = it
                                    if (isTextUnderLineSelected) textState.addSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                                    else textState.removeSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                                    isTextSettingSelected = false

                                },
                                isTextMiddleLineSelected = isTextMiddleLineSelected,
                                onTextMiddleLineSelected = {
                                    isTextMiddleLineSelected = it
                                    if (isTextMiddleLineSelected) textState.addSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
                                    else textState.removeSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))

                                    isTextSettingSelected = false
                                }, textState)

                            KeyboardLocationFunc(viewModel, navController,textState)

                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = viewModel.isStored,
                enter = fadeIn(animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)),
                exit = fadeOut(animationSpec = tween(durationMillis = 500, easing = LinearEasing))
            ){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 10.dp)
                        .navigationBarsPadding(),
                    contentAlignment = Alignment.BottomCenter
                ){
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(Color(0xFF212121), shape = RoundedCornerShape(20)), contentAlignment = Alignment.Center){
                        Text(text = "임시 저장이 완료되었습니다.", color = Color.White, fontSize = 16.sp)
                    }
                }
            }

        }
    }




@Composable
fun WritingTopAppBar(
    navController: NavController,
    viewModel: WritingViewModel,
    state: RichTextState,
    isCompleteClicked : ()-> Unit
) {
    val isKeyboardOpen by keyboardAsState() //keyBoard.Opened

    val focusRequester = remember { FocusRequester() }
    val focusState = viewModel.focusState
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var isContentNotEmpty = remember { mutableStateOf(false) }


    LinkedOutTheme {
        Column(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxWidth()
        ) {
            Row(

                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                , verticalAlignment = Alignment.CenterVertically
            ) {
                if (isKeyboardOpen == Keyboard.Opened || viewModel.isTextFeatOpened.value) {

                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "keyboardDown",
                        modifier = Modifier
                            .clickable {
                                focusManager.clearFocus()
                                viewModel.titleFocusState.value = false
                                viewModel.isTextFeatOpened.value = false
                                viewModel.isHashTagClicked = false
                                viewModel.isLocationClicked = false
                                keyboardController?.hide()
                                focusState.value = false

                            },
                        tint = Color.White
                    )
                } else {
                    Text(
                        text = "취소",
                        color = Color(0xFF686868),
                        modifier = Modifier
                            .clickable {
                                viewModel.content = state.toHtml()
                                if (viewModel.title.value.text.isEmpty() && viewModel.content.isEmpty()) {
                                    navController.popBackStack()
                                    viewModel.initialize()
                                } else {
                                    viewModel.isCanCelClicked.value = true
                                    keyboardController?.hide()
                                }

                            },
                        fontSize = 16.sp
                    )
                }

                // 텍스트 필드와 완료 버튼을 수평으로 배치
                Row(
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f) // 텍스트 필드와 완료 버튼을 균등하게 확장 // 오른쪽 여백 추가
                ) {
                    val xdp = animateDpAsState(targetValue = if (viewModel.titleFocusState.value) (-40).dp else 0.dp, label = "").value
                    val ydp = animateDpAsState(targetValue = if (viewModel.titleFocusState.value) 50.dp else 0.dp, label = "").value

                    TextField(
                        modifier = Modifier
                            .offset(x = xdp, y = ydp)
                            .focusRequester(focusRequester = focusRequester)
                            .onFocusChanged {
                                viewModel.titleFocusState.value = it.isFocused
                            },

                        placeholder = {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = if (viewModel.titleFocusState.value) Alignment.CenterStart else Alignment.Center
                            ) {
                                Text(
                                    text = "제목을 입력하세요",
                                    textAlign = if (focusState.value) TextAlign.Start else TextAlign.Center,
                                    color = Color(0xFF686868)
                                )
                            }
                        },
                        value = viewModel.title.value,
                        onValueChange = {
                            if (it.text.length <= 30){
                                viewModel.title.value = it }
                            }
                            ,
                        textStyle = TextStyle(
                            textAlign = if (viewModel.titleFocusState.value) TextAlign.Start else TextAlign.Center,
                            fontSize = if (viewModel.titleFocusState.value) 20.sp else 16.sp
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        maxLines = 2
                    )
                }
                val context = LocalContext.current
                // 완료 버튼을 오른쪽에 배치
                Text(
                    color = Color.White,
                    text = "완료",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .clickable {
                            isCompleteClicked() //타이틀이 공백x 30자 넘지않게. 내용은 미니멈보다 많게
                            if (viewModel.title.value.text.isNotEmpty() && viewModel.content.length >= viewModel.minLength && viewModel.content.length <= viewModel.maxLength && viewModel.title.value.text.length <= 30){
                                Log.d(TAG, "WritingTopAppBar: ${viewModel.content}")
                                navController.navigate("WritingCompletePage")}
                            else {
                                isContentNotEmpty.value = true
                            }
                            if (viewModel.imageUri != null) {
                                viewModel.uploadThumbnail(viewModel.imageUri ?: Uri.EMPTY, context)
                            }
                        }

                )
            }
            MyDivider(viewModel)

        }
        //컨텐츠가 비어있다면 경고
        if (isContentNotEmpty.value) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                BlankWarningAlert(isContentNotEmpty)
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentTextField(viewModel: WritingViewModel,textState: RichTextState) {
    val focusState = viewModel.focusState

    val ydp = animateDpAsState(
        targetValue = if (viewModel.titleFocusState.value) 40.dp else 0.dp,
        label = ""
    ).value

    LinkedOutTheme {
        Column {
            RichTextEditor(
                state = textState,
                modifier = Modifier
                    .imePadding()
                    .offset(x = 0.dp, y = ydp)
                    .onFocusChanged { focusState.value = it.isFocused }
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                placeholder = {
                    Text(
                        text = viewModel.hint,
                        fontSize = 14.sp,
                        color = Color(0xFF686868)
                    )
                },
                colors = RichTextEditorDefaults.richTextEditorColors
                    (containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.White)

            )

            Spacer(modifier = Modifier.height(20.dp))

        }
    }
}

@Composable
fun MyDivider(viewModel: WritingViewModel) {

    val ydp = animateDpAsState(
        targetValue = if (viewModel.titleFocusState.value) 40.dp else 0.dp,
        label = ""
    ).value

    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .offset(x = 0.dp, y = ydp)
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF202020)
        )
    }
}

@Composable
fun WritingCancelCard(viewModel: WritingViewModel, navController: NavController,isStoreClicked : ()->Unit) {

    LinkedOutTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF191919))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 24.dp, bottom = 24.dp),
                        text = "지금 취소하면 모든 내용이 삭제됩니다.",
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color(0xFF202020)
                    )
                    Text(
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 20.dp)
                            .clickable {
                                viewModel.initialize()
                                navController.navigate("${Routes.Home}/200")
                            },
                        fontSize = 16.sp,
                        text = "작성취소",
                        textAlign = TextAlign.Center,
                        color = Color(0xFFE43446)
                    )
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color(0xFF202020)
                    )
                    Text(
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 20.dp)
                            .clickable {
                                isStoreClicked()
                                val tagList = mutableListOf<EssayApi.Tag>()

                                viewModel.hashTagList.forEach {
                                    tagList.add(EssayApi.Tag(1, it))
                                }

                                viewModel.updateOrInsertEssay(
                                    EssayApi.EssayItem(
                                        title = viewModel.title.value.text,
                                        content = viewModel.content,
                                        longitude = viewModel.longitude,
                                        latitude = viewModel.latitude,
                                        createdDate = viewModel.getCurrentDate(),
                                        tags = tagList,
                                        essayPrimaryId = viewModel.essayPrimaryId ?: 0,
                                        thumbnail = viewModel.imageUrl

                                    )


                                )
                                navController.popBackStack()
                                viewModel.initialize()
                            },
                        fontSize = 16.sp,
                        text = "임시저장",
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.clickable { viewModel.isCanCelClicked.value = false },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF191919))
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        fontSize = 16.sp,
                        text = "돌아가기",
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )

                }
            }
            Spacer(modifier = Modifier.height(35.dp))

        }
    }

}

@Composable
fun KeyboardLocationFunc(viewModel: WritingViewModel, navController: NavController,state: RichTextState) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val requestPermissionsUtil = RequestPermissionsUtil(LocalContext.current, viewModel)

    val funcItems = listOf(
//        FuncItemData("인용구", R.drawable.keyboard_quote) {},
//        FuncItemData("구분선", R.drawable.keyboard_divider) {
//            val cursorPosition = viewModel.content.selection.start
//            val newText = viewModel.content.text.substring(0, cursorPosition) +
//                    "\n---\n" +
//                    viewModel.content.text.substring(cursorPosition)
//            viewModel.content = TextFieldValue(
//                text = newText,
//                selection = TextRange(cursorPosition + 5)
//            )
//        },
        FuncItemData("위치", R.drawable.keyboard_location) {
            viewModel.isHashTagClicked = false
            viewModel.isLocationClicked = !viewModel.isLocationClicked
            requestPermissionsUtil.RequestLocation()
            if (requestPermissionsUtil.isLocationPermitted()) {
                requestPermissionsUtil.RequestLocationUpdates()
            } else {
                requestPermissionsUtil.RequestLocationUpdates()
                Log.d("MainActivity", "위치 권한 거부")
            }
        },
        FuncItemData("감정 해시태그", R.drawable.keyboard_hashtag) {
            viewModel.isLocationClicked = false
            viewModel.isHashTagClicked = !viewModel.isHashTagClicked
            Log.d("tagtag", "tag")
        },

        FuncItemData("이미지", R.drawable.keyboard_image) {
            viewModel.content = state.toHtml()
            navController.navigate("CropImagePage") },

        FuncItemData("쓰다 만 글", R.drawable.keyboard_storage) { navController.navigate("TemporaryStoragePage") },

    )

    val density = LocalDensity.current
    val keyboardHeightDp = remember { mutableStateOf(283.dp) }
    var hasCalculatedKeyboardHeight by remember { mutableStateOf(false) }

    val view = LocalView.current

    LaunchedEffect(view) {
        if (!hasCalculatedKeyboardHeight) {
            // Get the root view insets
            val insets = ViewCompat.getRootWindowInsets(view)
            // Get the IME insets
            val imeHeightPx = insets?.getInsets(WindowInsetsCompat.Type.ime())?.bottom ?: 0

            if (imeHeightPx > 0) {
                // Convert pixels to dp
                keyboardHeightDp.value = with(density) { imeHeightPx.toDp() }
                hasCalculatedKeyboardHeight = true
                Log.d("TAG", "Keyboard height in dp: ${keyboardHeightDp.value}")
            }
        }
    }

    Box(
        modifier = Modifier
            .background(Color(0xFF313131))
            .fillMaxWidth()
            .height(keyboardHeightDp.value)  // Use the remembered dp value
            .padding(horizontal = 16.dp)
            .padding(top = 40.dp),
        //contentAlignment = Alignment.Center
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(60.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            items(funcItems) {
                FuncItem(it.text, it.icon, it.clickable)
            }
        }
    }
}
@Composable
fun TextEditBar(
    viewModel: WritingViewModel,
    isTextSettingSelected: Boolean,
    onTextSettingSelected: (Boolean) -> Unit,
    isTextBoldSelected: Boolean,
    onTextBoldSelected: (Boolean) -> Unit,
    isTextUnderLineSelected: Boolean,
    onTextUnderLineSelected: (Boolean) -> Unit,
    isTextMiddleLineSelected: Boolean,
    onTextMiddleLineSelected: (Boolean) -> Unit,
    textState: RichTextState
) {

    var isOpened by remember { mutableStateOf(true) }

    val insets = ViewCompat.getRootWindowInsets(LocalView.current)
    val imeHeightPx = insets?.getInsets(WindowInsetsCompat.Type.ime())?.bottom ?: 0
    val density = LocalDensity.current

    val imeHeightDp = with(density) { imeHeightPx.toDp() }

    var isAnimationComplete by remember { mutableStateOf(false) }

    // LaunchedEffect를 통해 애니메이션이 완료된 후 상태를 업데이트
    LaunchedEffect(isOpened) {
        if (!isOpened) {
            delay(600) // 애니메이션의 지속 시간과 일치
            isAnimationComplete = true
        } else {
            isAnimationComplete = false
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    var isKeyboardAppeared by remember { mutableStateOf(true) }
    val requestPermissionsUtil = RequestPermissionsUtil(LocalContext.current, viewModel)

    LinkedOutTheme {
        AnimatedVisibility(
            visible = isOpened,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth }, // 화면 오른쪽에서 시작
                animationSpec = tween(durationMillis = 600) // 애니메이션 속도
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth }, // 화면 오른쪽으로 나감
                animationSpec = tween(durationMillis = 600) // 애니메이션 속도
            )
        ) {
            Row(
                modifier = Modifier
                    .background(Color(0xFF1D1D1D))
                    .fillMaxWidth()
                    .height(55.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (viewModel.isHashTagClicked && !viewModel.isLocationClicked) {
                    Spacer(modifier = Modifier.width(20.dp))
                    TextItem(icon = R.drawable.keyboard_hashtag, LinkedInColor) {}
                    HashTagTextField(viewModel = viewModel)
                } else if (viewModel.isLocationClicked && !viewModel.isHashTagClicked) {
                    Spacer(modifier = Modifier.width(15.dp))
                    Icon(
                        tint = Color(0xFF616FED),
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "location",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(end = 14.dp)
                            .clickable { requestPermissionsUtil.RequestLocationUpdates() }
                    )
                    LocationTextField(viewModel = viewModel)
                } else {
                    Spacer(modifier = Modifier.width(20.dp))

                    TextItem(
                        icon = R.drawable.editor_thick,
                        if (!isTextSettingSelected) Color.White else LinkedInColor
                    ) {
                        onTextSettingSelected(!isTextSettingSelected)
                    }

                    TextItem(
                        icon = R.drawable.editbar_bold,
                        if (!isTextBoldSelected) Color.White else LinkedInColor
                    ) {
                        onTextBoldSelected(!isTextBoldSelected)
                    }

                    TextItem(
                        icon = R.drawable.editbar_underline,
                        if (!isTextUnderLineSelected) Color.White else LinkedInColor
                    ) {
                        onTextUnderLineSelected(!isTextUnderLineSelected)
                    }

                    TextItem(
                        icon = R.drawable.editbar_middleline,
                        if (!isTextMiddleLineSelected) Color.White else LinkedInColor
                    ) {
                        onTextMiddleLineSelected(!isTextMiddleLineSelected)
                    }

                    TextItem(
                        icon = R.drawable.editbar_more,
                        if (viewModel.isTextFeatOpened.value) LinkedInColor else Color.White
                    ) {
                        viewModel.content = textState.toHtml()
                        isKeyboardAppeared = !isKeyboardAppeared
                        if (isKeyboardAppeared) keyboardController?.show() else keyboardController?.hide()
                        viewModel.isTextFeatOpened.value = !viewModel.isTextFeatOpened.value
                    }

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "저장", color = Color.White, modifier = Modifier.clickable {
                                val tagList = mutableListOf<EssayApi.Tag>()
                                viewModel.hashTagList.forEach {
                                    tagList.add(EssayApi.Tag(1, it))
                                }

                                viewModel.updateOrInsertEssay(
                                    EssayApi.EssayItem(
                                        title = viewModel.title.value.text,
                                        content = viewModel.content,
                                        longitude = viewModel.longitude,
                                        latitude = viewModel.latitude,
                                        createdDate = viewModel.getCurrentDate(),
                                        tags = tagList,
                                        essayPrimaryId = viewModel.essayPrimaryId ?: 0
                                    )
                                )

                                keyboardController!!.hide()
                                viewModel.isStored = true
                            })
                            Spacer(modifier = Modifier.width(15.dp))
                            VerticalDivider(
                                modifier = Modifier
                                    .height(34.dp)
                                    .padding(end = 12.dp), thickness = 1.dp
                            )
                            TextItem(
                                icon = R.drawable.editbar_next,
                                if (!isTextSettingSelected) Color.White else LinkedInColor
                            ) { isOpened = false }
                        }
                    }
                }
            }
        }

        if (isAnimationComplete) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(end = 8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.editbar_reopen),
                    contentDescription = "",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(36.dp).clickable { isOpened = true }
                )
            }
        }
    }
}

@Composable
fun TextSettingsBar(textState: RichTextState) {

    var selectedFontSize by remember { mutableIntStateOf(textState.currentSpanStyle.fontSize.value.toInt()) }
    val fontSizeList = listOf(13,14,15,16,17,18,19,20,22,24)

    LinkedOutTheme {
        Row(
            Modifier
                .horizontalScroll(rememberScrollState())
                .fillMaxWidth()
                .height(50.dp)
                .background(color = Color(0xFF313131)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(24.dp))
            fontSizeList.forEach { size ->
                Text(
                    text = size.toString(),
                    modifier = Modifier.clickable { selectedFontSize = size
                        textState.addSpanStyle(SpanStyle(fontSize = selectedFontSize.sp))},
                    color = if (selectedFontSize == size) LinkedInColor else Color.White
                )
                Spacer(modifier = Modifier.width(32.dp))
            }
        }
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Preview
@Composable
fun test(){
    var imageSize by remember { mutableStateOf(IntSize.Zero) }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box{
            GlideImage(model = R.drawable.background_logo_btn, contentDescription = "uri") //todo 위치 조절 제대로하기

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd){
                Row(
                    Modifier
                        .offset(x = -(10).dp, y = 10.dp)
                        .width(50.dp)
                        .height(27.dp)
                        .background(
                            color = Color(0xFF616FED),
                            shape = RoundedCornerShape(20)
                        ), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Text(text = "변경", fontSize = 12.sp, color = Color.White)
                }
            }


        }
    }

}

