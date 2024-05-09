package com.echoist.linkedout

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.colintheshots.twain.MarkdownText
import com.echoist.linkedout.data.FuncItem
import com.echoist.linkedout.data.FuncItemData
import com.echoist.linkedout.data.HashTagBtn
import com.echoist.linkedout.data.HashTagTextField
import com.echoist.linkedout.data.TextItem
import com.echoist.linkedout.gps.RequestPermissionsUtil
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.WritingViewModel
object Token {
    var accessToken: String by mutableStateOf("token")
}
@Composable
fun MarkDownBtn(viewModel: WritingViewModel) {
    //todo 코드정리와 마크다운 어디까지 구현해서 쓸지 생각필요.
    val selection = viewModel.content.value.selection
    // 선택된 텍스트의 시작 위치
    val start = selection.start
    // 선택된 텍스트의 끝 위치
    val end = selection.end
    val selectText = viewModel.content.value.getSelectedText() //selected text
    // 선택된 텍스트 앞에 '*'을 붙임
    val boldText = if (start != end) "**$selectText**" else selectText
    val italicText = if (start != end) "*$selectText*" else selectText
    val header1Text = if (start != end) "# $selectText" else selectText
    val header2Text = if (start != end) "## $selectText" else selectText


// 이전 텍스트에서 선택된 부분을 대체
    val new_bold = viewModel.content.value.text.replaceRange(start, end, boldText)
    val new_italic = viewModel.content.value.text.replaceRange(start, end, italicText)
    val new_header1 = viewModel.content.value.text.replaceRange(start, end, header1Text)
    val new_header2 = viewModel.content.value.text.replaceRange(start, end, header2Text)


// 변경된 텍스트를 ViewModel에 설정

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFF1D1D1D)), horizontalArrangement = Arrangement.Center ) {
        Button(onClick = { viewModel.content.value = TextFieldValue(new_bold) }) {
            Text(text = "bold", color = Color.White)
        }
        Button(onClick = { viewModel.content.value = TextFieldValue(new_italic) }) {
            Text(text = "italic", color = Color.White)
        }
        Button(onClick = { viewModel.content.value = TextFieldValue(new_header1) }) {
            Text(text = "header1", color = Color.White)
        }
        Button(onClick = { viewModel.content.value = TextFieldValue(new_header2) }) {
            Text(text = "header2", color = Color.White)
        }
    }

}

@Preview
@Composable
fun PrevWritingPage(){
    WritingPage(navController = rememberNavController(), viewModel = WritingViewModel(), accessToken = "")

}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@PreviewScreenSizes
@Composable
fun WritingPage(
    navController: NavController,
    viewModel: WritingViewModel,
    accessToken: String
) {

    viewModel.accessToken = accessToken

    val isKeyBoardOpened by keyboardAsState()

    val scrollState = rememberScrollState()
    val focusState = viewModel.focusState

    val background = if (isSystemInDarkTheme()) Color.Black else Color.White


    Log.d("tokentoken",accessToken)

    LinkedOutTheme {
        Box {
            Column(modifier = Modifier
                .background(background)
                .fillMaxSize()) {
                Column(modifier = Modifier
                    .verticalScroll(scrollState)
                )
                {
                    WritingTopAppBar(navController = navController, viewModel)
                    ContentTextField(viewModel = viewModel)
                    MarkdownText(
                        markdown = viewModel.content.value.text,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp)
                            .layout { measurable, constraints ->
                                val placeable = measurable.measure(constraints)
                                layout(placeable.width, placeable.height) {
                                    placeable.place(
                                        x = 0,
                                        y = if (focusState.value) 100 else 0
                                    )
                                }
                            },
                        color = Color.White
                    )
                }

            }
            Box(modifier = Modifier
                .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter){ //키보드 자리에 들어갈 컴포 넣기
                Column {
                    AnimatedVisibility(visible = viewModel.isCanCelClicked.value) {
                        WritingCancelCard(viewModel = viewModel, navController = navController)

                    }
                    if (viewModel.hashTagList.isNotEmpty()) {
                        Row {
                            viewModel.hashTagList.forEach {
                                HashTagBtn(viewModel = viewModel, text = it)
                            }
                        }
                    }
                    if (isKeyBoardOpened == Keyboard.Opened ||  viewModel.isTextFeatOpened.value){

                        TextEditBar(viewModel)
                        KeyboardLocationFunc(viewModel)

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
    val isKeyboardOpen by keyboardAsState() //keyBoard.Opened
    val textState = viewModel.title
    val focusRequester = remember { FocusRequester() }
    val focusState = viewModel.focusState
    val keyboardController = LocalSoftwareKeyboardController.current


    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (isKeyboardOpen == Keyboard.Opened || viewModel.isTextFeatOpened.value){
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "keyboardDown",
                    modifier = Modifier
                        .padding(start = 20.dp, top = 15.dp)
                        .clickable {
                            viewModel.isTextFeatOpened.value = false
                            viewModel.isHashTagClicked = false
                            keyboardController?.hide()
                            focusState.value = false
                        },
                    tint = Color.White
                )
            }
            else{
                Text(
                    text = "취소",
                    color = Color(0xFF686868),
                    modifier = Modifier
                        .padding(start = 20.dp, top = 15.dp)
                        .clickable {
                            viewModel.isCanCelClicked.value = true
                            keyboardController?.hide()
                        },
                    fontSize = 16.sp
                )
            }

            // 텍스트 필드와 완료 버튼을 수평으로 배치
            Row(
                modifier = Modifier
                    .weight(1f) // 텍스트 필드와 완료 버튼을 균등하게 확장 // 오른쪽 여백 추가
            ) {
                TextField(
                    modifier = Modifier
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            layout(placeable.width, placeable.height) {
                                placeable.place(
                                    x = if (focusState.value) -115 else 0,
                                    y = if (focusState.value) 120 else 0
                                )
                            }
                        }
                        .focusRequester(focusRequester = focusRequester)
                        .onFocusChanged {
                            focusState.value = it.isFocused
                        },
                    placeholder = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = if (focusState.value) Alignment.CenterStart else Alignment.Center
                        ) {
                            Text(
                                text = "제목을 입력하세요",
                                textAlign = if (focusState.value) TextAlign.Start else TextAlign.Center,
                                color = Color(0xFF686868)
                            )
                        }
                    },
                    value = textState.value,
                    onValueChange = { textState.value = it },
                    textStyle = TextStyle(
                        textAlign = if (focusState.value) TextAlign.Start else TextAlign.Center,
                        fontSize = if (focusState.value) 20.sp else 16.sp
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

            // 완료 버튼을 오른쪽에 배치
            Text(
                color = Color.White,
                text = "완료",
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(end = 20.dp, top = 15.dp)
                    .clickable {
                        navController.navigate("WritingCompletePage/${viewModel.accessToken}")
                        /* todo 서버로 제목과 내용 보내는 기능 필요합니다.
                        *   사진 넣는 방식도 구현 필요합니다.  */
                    }

            )

        }
        MyDivider(viewModel)

    }

}

@Composable
fun ContentTextField(viewModel: WritingViewModel) {
    val content = viewModel.content
    val focusState = viewModel.focusState

    TextField(
        modifier = Modifier
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(placeable.width, placeable.height) {
                    placeable.place(
                        x = 0,
                        y = if (focusState.value) 100 else 0
                    )
                }
            }
            .onFocusChanged { focusState.value = it.isFocused }
            .fillMaxWidth()
            .padding(5.dp),
        value = content.value,
        onValueChange = {
            content.value = it
        },
        label = {
            Text(
                text = "내용을 입력하세요",
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

}

@Composable
fun MyDivider(viewModel: WritingViewModel) {
    val focusState = viewModel.focusState

    HorizontalDivider(thickness = 1.dp, modifier = Modifier
        .padding(
            start = 20.dp,
            end = 20.dp
        )
        .layout { measurable, constraints ->
            val placeable = measurable.measure(constraints)
            layout(placeable.width, placeable.height) {
                placeable.place(
                    x = 0,
                    y = if (focusState.value) 100 else 0
                )
            }
        })
}

@Composable
fun WritingCancelCard(viewModel: WritingViewModel, navController: NavController) {
    Column(
        modifier = Modifier.padding(start = 20.dp, end = 20.dp),
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
                            navController.navigate("HOME/${viewModel.accessToken}")
                            viewModel.isCanCelClicked.value = false
                            viewModel.isTextFeatOpened.value = false
                            viewModel.hashTagList.clear()
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
                        .clickable { /*todo 임시저장 기능구현필요 */ },
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
fun KeyboardLocationFunc(viewModel: WritingViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val requestPermissionsUtil = RequestPermissionsUtil(LocalContext.current,viewModel)

    val funcItems = listOf(
        FuncItemData("인용구", R.drawable.pw_eye) {},
        FuncItemData("인용구", R.drawable.pw_eye) {},
        FuncItemData("위치", R.drawable.keyboard_location) {
            requestPermissionsUtil.RequestLocation()
            if (requestPermissionsUtil.isLocationPermitted()) {
                requestPermissionsUtil.RequestLocationUpdates()

            }
            else{
                requestPermissionsUtil.RequestLocationUpdates()
                Log.d("MainActivity", "위치 권한 거부")
            }

        },
        FuncItemData("쓰다 만 글", R.drawable.pw_eye) {},
        FuncItemData("감정 해시태그", R.drawable.keyboard_hashtag) {
            viewModel.isHashTagClicked = !viewModel.isHashTagClicked
            Log.d("tagtag", "tag")
        },
        FuncItemData("맞춤법 검사", R.drawable.pw_eye) {},
        FuncItemData("이미지", R.drawable.keyboard_img) {},
        FuncItemData("이미지", R.drawable.pw_eye) {},
    )
    Box(
        modifier = Modifier
            .background(Color(0xFF313131))
            .fillMaxWidth()
            .height(283.dp)
            .padding(horizontal = 16.dp), // 수평 방향으로 패딩 추가
        contentAlignment = Alignment.Center
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(60.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            items(funcItems) {
                FuncItem(it.text, it.icon,it.clickable)
            }
        }
    }
}
@Composable
fun TextEditBar(viewModel: WritingViewModel){
    val keyboardController = LocalSoftwareKeyboardController.current
    var isKeyboardApeared by remember { mutableStateOf(true) }
    
    Row(
        modifier = Modifier
            .background(Color(0xFF1D1D1D))
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (viewModel.isHashTagClicked){
            Spacer(modifier = Modifier.width(20.dp))
            TextItem(icon = R.drawable.keyboard_hashtag){}
            HashTagTextField(viewModel = viewModel)
        }
        else{

            Spacer(modifier = Modifier.width(20.dp))
            TextItem(icon = R.drawable.ring){}
            TextItem(icon = R.drawable.ring){}
            TextItem(icon = R.drawable.ring){}
            Icon(
                painter = painterResource(id = R.drawable.edit_bar_more),
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
            Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd){
                Row {
                    TextItem(icon = R.drawable.ring){}
                    VerticalDivider(modifier = Modifier
                        .height(34.dp)
                        .padding(end = 12.dp), thickness = 1.dp)
                    TextItem(icon = R.drawable.ring){}

                }
            }
        }


    }
}









