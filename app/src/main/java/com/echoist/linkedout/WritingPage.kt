package com.echoist.linkedout

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.colintheshots.twain.MarkdownText
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.WritingViewModel

@Composable
fun MarkDownBtn(viewModel: WritingViewModel) {
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

    if (start != end) {

    }

// 이전 텍스트에서 선택된 부분을 대체
    val new_bold = viewModel.content.value.text.replaceRange(start, end, boldText)
    val new_italic = viewModel.content.value.text.replaceRange(start, end, italicText)
    val new_header1 = viewModel.content.value.text.replaceRange(start, end, header1Text)
    val new_header2 = viewModel.content.value.text.replaceRange(start, end, header2Text)


// 변경된 텍스트를 ViewModel에 설정

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


//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@PreviewScreenSizes
@Composable
fun WritingPage(navController: NavController,viewModel: WritingViewModel) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current


    //scaffold에 넣으면 위치변경이 어려워져서 그냥 컬럼에 다 때려박았습니다. . .

    LinkedOutTheme {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.Black)
            .pointerInput(Unit) { //배경 터치 시 키보드 숨김
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
                )
            }
        )
        {
            WritingTopAppBar(navController = navController, viewModel)
            ContentTextField(viewModel = viewModel)
            MarkdownText(
                markdown = viewModel.content.value.text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp),
                color = Color.White
            )
            MarkDownBtn(viewModel)

        }


    }

}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewWritingPage() {
    WritingPage(navController = NavController(LocalContext.current), WritingViewModel())
}

@Composable
fun WritingTopAppBar(navController: NavController, viewModel: WritingViewModel) {
    val textState = viewModel.title
    val focusRequester = remember { FocusRequester() }
    val focusState = viewModel.focusState


    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "취소",
                color = Color(0xFF686868),
                modifier = Modifier
                    .padding(start = 20.dp, top = 15.dp)
                    .clickable {
                        navController.popBackStack()
                    },
                fontSize = 16.sp
            )


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
                                    x = if (focusState.value) -125 else 0,
                                    y = if (focusState.value) 120 else 0
                                )
                            }
                        }
                        .focusRequester(focusRequester = focusRequester)
                        .onFocusChanged { focusState.value = it.isFocused },
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
                        navController.navigate("WritingCompletePage")
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








