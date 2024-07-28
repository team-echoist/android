package com.echoist.linkedout.page.myLog

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
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
import com.colintheshots.twain.MarkdownText
import com.echoist.linkedout.R
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WritingPage(
    navController: NavController,
    viewModel: WritingViewModel,
) {

    val isKeyBoardOpened by keyboardAsState()
    val scrollState = rememberScrollState()
    val background = if (isSystemInDarkTheme()) Color.Black else Color.White

    var isTextSettingSelected by remember { mutableStateOf(false) }
    var isTextAlignSelected by remember { mutableStateOf(false) }
    var isTextUnderLineSelected by remember { mutableStateOf(false) }

    Log.d(TAG, "WritingPage: ${viewModel.readDetailEssay()}")

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
                    WritingTopAppBar(navController = navController, viewModel)


                    ContentTextField(viewModel = viewModel)
                    MarkdownText(
                        markdown = viewModel.content.text,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, top = 80.dp),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(50.dp))

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Box{
                                GlideImage(model = viewModel.imageUri ?: viewModel.imageUrl , contentDescription = "uri") //todo 위치 조절 제대로하기
                                Log.d(TAG, "WritingPage: ${viewModel.imageUri}, ${viewModel.imageUrl}")
                                if (viewModel.imageUri != null || (viewModel.imageUrl != null && viewModel.imageUrl!!.startsWith("https"))){ //image url 주소가 널이 아니고 https값으로 시작해야 제대로된 Url link
                                    Row( //변경버튼 클릭 시 화면이동
                                        Modifier
                                            .offset(x = 10.dp, y = 10.dp)
                                            .width(50.dp)
                                            .height(27.dp)
                                            .clickable { navController.navigate("CropImagePage") }
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

            }
            //취소 클릭시 배경 어둡게
            if (viewModel.isCanCelClicked.value)
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(0.7f)))

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
                        WritingCancelCard(viewModel = viewModel, navController = navController)
                    }
                    //장소 찍는
                    if (viewModel.longitude != null && viewModel.latitude != null && viewModel.isTextFeatOpened.value) {
                        if (viewModel.isLocationClicked) {
                            Row {
                                LocationBox(viewModel = viewModel)
                                Spacer(modifier = Modifier.width(2.dp))
                                Row(
                                    Modifier
                                        .padding(horizontal = 20.dp)
                                        .horizontalScroll(rememberScrollState())) {
                                    viewModel.locationList.forEach {
                                        LocationBtn(viewModel = viewModel, text = it)
                                    }
                                }
                            }
                        }

                    } else if (isKeyBoardOpened == Keyboard.Closed && !viewModel.isTextFeatOpened.value) {
                        if (viewModel.locationList.isNotEmpty() || viewModel.longitude != null) {
                            LocationGroup(viewModel = viewModel)
                            Spacer(modifier = Modifier.height(10.dp))
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
                            TextSettingsBar(viewModel)
                        }
                        if (isTextAlignSelected) {
                            Row {
                                Spacer(modifier = Modifier.width(40.dp))
                                TextAlignBar()

                            }
                        }
                        TextEditBar(viewModel,
                            isTextSettingSelected, {
                                isTextSettingSelected = it
                                isTextAlignSelected = false
                                isTextUnderLineSelected = false
                            },
                            isTextAlignSelected,
                            {
                                isTextAlignSelected = it
                                isTextSettingSelected = false
                                isTextUnderLineSelected = false
                            },
                            isTextUnderLineSelected,
                            {
                                isTextUnderLineSelected = it
                                isTextAlignSelected = false
                                isTextSettingSelected = false

                                if (viewModel.content.selection.start != viewModel.content.selection.end) {
                                    val cursorPosition = viewModel.content.selection.start
                                    val endPosition = viewModel.content.selection.end
                                    val newText =
                                        viewModel.content.text.substring(0, cursorPosition) +
                                                "<u>" + viewModel.content.text.substring(
                                            cursorPosition,
                                            endPosition
                                        ) + "</u>" +
                                                viewModel.content.text.substring(
                                                    endPosition,
                                                    viewModel.content.text.length
                                                )


                                    viewModel.content = TextFieldValue(
                                        text = newText,
                                    )
                                }


                            })

                        KeyboardLocationFunc(viewModel, navController)

                    }
                }
                }
            }
        }
    }


@Composable
fun WritingTopAppBar(
    navController: NavController,
    viewModel: WritingViewModel
) {
    Log.d(TAG, "WritingTopAppBar: ${viewModel.title}")
    val isKeyboardOpen by keyboardAsState() //keyBoard.Opened

    val focusRequester = remember { FocusRequester() }
    val focusState = viewModel.focusState
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var isContentNotEmpty = remember { mutableStateOf(false) }


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

                            if (viewModel.title.value.text.isEmpty() && viewModel.content.text.isEmpty()) {
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
                    onValueChange = { viewModel.title.value = it },
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
            val scope = rememberCoroutineScope()
            // 완료 버튼을 오른쪽에 배치
            Text(
                color = Color.White,
                text = "완료",
                fontSize = 16.sp,
                modifier = Modifier
                    .clickable {
                        if (viewModel.title.value.text.isNotEmpty() && viewModel.content.text.length >= viewModel.minLength)
                            navController.navigate("WritingCompletePage")
                        if (viewModel.imageUri != null) {
                            viewModel.uploadThumbnail(viewModel.imageUri ?: Uri.EMPTY, context)
                        } else {
                            isContentNotEmpty.value = true
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

@Composable
fun ContentTextField(viewModel: WritingViewModel) {
    val focusState = viewModel.focusState

    val ydp = animateDpAsState(
        targetValue = if (viewModel.titleFocusState.value) 40.dp else 0.dp,
        label = ""
    ).value

    Column {
        TextField(

            modifier = Modifier
                .offset(x = 0.dp, y = ydp)
                .onFocusChanged { focusState.value = it.isFocused }
                .fillMaxWidth()
                .padding(5.dp),
            value = viewModel.content,
            onValueChange = {
                if (viewModel.maxLength >= it.text.length)
                    viewModel.content = it
            },
            placeholder = {
                Text(
                    text = viewModel.hint,
                    color = Color(0xFF686868)
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "${viewModel.content.text.length} / ${viewModel.maxLength}",
            color = Color.Gray,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
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
fun WritingCancelCard(viewModel: WritingViewModel, navController: NavController) {

    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
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
                            navController.navigate("HOME")
                        },
                    fontSize = 16.sp,
                    text = "작성취소",
                    textAlign = TextAlign.Center,
                    color = Color.Red
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

                            val tagList = mutableListOf<EssayApi.Tag>()

                            viewModel.hashTagList.forEach {
                                tagList.add(EssayApi.Tag(1, it))
                            }

                            viewModel.updateOrInsertEssay(
                                EssayApi.EssayItem(
                                    title = viewModel.title.value.text,
                                    content = viewModel.content.text,
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

@Composable
fun KeyboardLocationFunc(viewModel: WritingViewModel, navController: NavController) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val requestPermissionsUtil = RequestPermissionsUtil(LocalContext.current, viewModel)

    val funcItems = listOf(
        FuncItemData("인용구", R.drawable.keyboard_quote) {},
        FuncItemData("구분선", R.drawable.keyboard_divider) {
            val cursorPosition = viewModel.content.selection.start
            val newText = viewModel.content.text.substring(0, cursorPosition) +
                    "\n---\n" +
                    viewModel.content.text.substring(cursorPosition)
            viewModel.content = TextFieldValue(
                text = newText,
                selection = TextRange(cursorPosition + 5)
            )
        },
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
        FuncItemData("쓰다 만 글", R.drawable.keyboard_storage) { navController.navigate("TemporaryStoragePage") },
        FuncItemData("감정 해시태그", R.drawable.keyboard_hashtag) {
            viewModel.isLocationClicked = false
            viewModel.isHashTagClicked = !viewModel.isHashTagClicked
            Log.d("tagtag", "tag")
        },
        FuncItemData("맞춤법 검사", R.drawable.keyboard_spelling) {},
        FuncItemData("이미지", R.drawable.keyboard_img) { navController.navigate("CropImagePage") },
        FuncItemData("이미지", R.drawable.pw_eye) {},
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
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
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
    isTextAlignSelected: Boolean,
    onTextAlignSelected: (Boolean) -> Unit,
    isTextUnderLineSelected: Boolean,
    onTextUnderLineSelected: (Boolean) -> Unit,
) {

    val insets = ViewCompat.getRootWindowInsets(LocalView.current)
    val imeHeightPx = insets?.getInsets(WindowInsetsCompat.Type.ime())?.bottom ?: 0
    val density = LocalDensity.current

    // Convert pixels to dp
    val imeHeightDp = with(density) { imeHeightPx.toDp() }

    Log.d("TAG", "Keyboard height in dp: $imeHeightDp")

    val keyboardController = LocalSoftwareKeyboardController.current
    var isKeyboardApeared by remember { mutableStateOf(true) }
    val requestPermissionsUtil = RequestPermissionsUtil(LocalContext.current, viewModel)
    Row(
        modifier = Modifier
            .background(Color(0xFF1D1D1D))
            .fillMaxWidth()
            //.imePadding()
            .height(60.dp),
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
            Icon(
                painter = painterResource(id = R.drawable.editbar_thick),
                contentDescription = "icon",
                tint = if (isSystemInDarkTheme()) {
                    if (!isTextSettingSelected) Color.White else LinkedInColor
                } else {
                    if (!isTextSettingSelected) Color.Black else
                        LinkedInColor
                },
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 14.dp)
                    .clickable { onTextSettingSelected(!isTextSettingSelected) }
            )

            //글 정렬

            TextItem(
                icon = R.drawable.editbar_alignment, if (isSystemInDarkTheme()) {
                    if (!isTextAlignSelected) Color.White else LinkedInColor
                } else {
                    if (!isTextAlignSelected) Color.Black else
                        LinkedInColor
                }
            ) {
                onTextAlignSelected(!isTextAlignSelected)
            }


            //밑줄
            TextItem(
                icon = R.drawable.editbar_underline, if (isSystemInDarkTheme()) {
                    if (!isTextUnderLineSelected) Color.White else LinkedInColor
                } else {
                    if (!isTextUnderLineSelected) Color.Black else
                        LinkedInColor
                }
            ) {
                onTextUnderLineSelected(!isTextUnderLineSelected)
            }
            Icon(
                painter = painterResource(id = R.drawable.editbar_more),
                contentDescription = "icon",
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 14.dp)
                    .clickable {
                        isKeyboardApeared = !isKeyboardApeared
                        if (isKeyboardApeared) keyboardController?.show() else keyboardController?.hide()
                        viewModel.isTextFeatOpened.value = true
                    },
                tint = Color.Unspecified
            )
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "저장", color = Color.White, modifier = Modifier.clickable {
                        //room db에 저장
                        val tagList = mutableListOf<EssayApi.Tag>()
                        viewModel.hashTagList.forEach {
                            tagList.add(EssayApi.Tag(1,it))
                        }

                        viewModel.updateOrInsertEssay(
                            EssayApi.EssayItem(
                                title = viewModel.title.value.text,
                                content = viewModel.content.text,
                                longitude = viewModel.longitude,
                                latitude = viewModel.latitude,
                                createdDate = viewModel.getCurrentDate(),
                                tags = tagList,
                                essayPrimaryId = viewModel.essayPrimaryId ?: 0

                            )
                        )
                    })
                    Spacer(modifier = Modifier.width(15.dp))
                    VerticalDivider(
                        modifier = Modifier
                            .height(34.dp)
                            .padding(end = 12.dp), thickness = 1.dp
                    )
                    TextItem(
                        icon = R.drawable.editbar_next, if (isSystemInDarkTheme()) {
                            if (!isTextSettingSelected) Color.White else LinkedInColor
                        } else {
                            if (!isTextSettingSelected) Color.Black else
                                LinkedInColor
                        }
                    ) {}

                }
            }
        }
    }
}

@Composable
fun TextSettingsBar(viewModel: WritingViewModel) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = Color(0xFF313131)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = "기본고딕", color = Color.White, modifier = Modifier.clickable { })
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = "나눔명조", color = Color.White, modifier = Modifier.clickable { })
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = "16", color = Color.White, modifier = Modifier.clickable { })
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = "B",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.clickable {
                if (viewModel.content.selection.start != viewModel.content.selection.end) {
                    val cursorPosition = viewModel.content.selection.start
                    val endPosition = viewModel.content.selection.end
                    val newText = viewModel.content.text.substring(0, cursorPosition) +
                            "<b>" + viewModel.content.text.substring(
                        cursorPosition,
                        endPosition
                    ) + "</b>" +
                            viewModel.content.text.substring(
                                endPosition,
                                viewModel.content.text.length
                            )


                    viewModel.content = TextFieldValue(
                        text = newText,
                    )
                }
            })
    }
}

@Preview
@Composable
fun TextAlignBar() {
    Column(
        Modifier
            .size(44.dp, 150.dp)
            .background(color = Color(0xFF1D1D1D)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.textalignleft),
            tint = Color.White,
            contentDescription = "",
            modifier = Modifier
                .weight(
                    1f
                )
                .size(20.dp)
                .clickable { }
        )
        Icon(
            painter = painterResource(id = R.drawable.textalighright),
            tint = Color.White,
            contentDescription = "",
            modifier = Modifier
                .weight(
                    1f
                )
                .size(20.dp)
                .clickable { }
        )

        Icon(
            painter = painterResource(id = R.drawable.textalignmiddle),
            tint = Color.White,
            contentDescription = "",
            modifier = Modifier
                .weight(
                    1f
                )
                .size(20.dp)
                .clickable { }
        )

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
            GlideImage(model = R.drawable.alarm_pending, contentDescription = "uri") //todo 위치 조절 제대로하기

            Row(
                Modifier
                    .offset(x = 20.dp, y = 20.dp)
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

