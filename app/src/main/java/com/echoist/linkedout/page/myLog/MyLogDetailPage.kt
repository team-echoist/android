package com.echoist.linkedout.page.myLog

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.INSPECT_POPUP_URL
import com.echoist.linkedout.LINKEDOUT_POPUP_URL
import com.echoist.linkedout.PRIVATE_POPUP_URL
import com.echoist.linkedout.PUBLISHED_POPUP_URL
import com.echoist.linkedout.R
import com.echoist.linkedout.components.LastEssayPager
import com.echoist.linkedout.data.Story
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.MyLogViewModel
import com.echoist.linkedout.viewModels.WritingViewModel
import kotlinx.coroutines.delay


@Composable
fun MyLogDetailPage(navController: NavController, viewModel: MyLogViewModel,writingViewModel: WritingViewModel) {
    val scrollState = rememberScrollState()


    LinkedOutTheme {
        Scaffold(
            topBar = {
                DetailTopAppBar(navController = navController, viewModel)
            },
            content = {
                Box(
                    Modifier
                        .padding(it)
                        .fillMaxSize(), contentAlignment = Alignment.TopCenter
                ) {

                    Column(Modifier.verticalScroll(scrollState)) {
                        DetailEssay(viewModel = viewModel)
                        LastEssayPager(viewModel = viewModel, navController = navController)
                    }
                    //수정 옵션
                    AnimatedVisibility(
                        visible = viewModel.isActionClicked,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = FastOutSlowInEasing
                            )
                        ),
                        exit = fadeOut(
                            animationSpec = tween(
                                durationMillis = 200,
                                easing = LinearEasing
                            )
                        )
                    ) {

                        ModifyOption(viewModel, navController = navController,writingViewModel)

                    }

                }
            }
        )
    }


}

@Composable
fun ModifyOption(viewModel: MyLogViewModel, navController: NavController,writingViewModel : WritingViewModel) {

    var isStoryClicked by remember { mutableStateOf(false) }

    if (isStoryClicked){
        StoryModifyBox({
            isStoryClicked = false
            viewModel.isActionClicked = false
            viewModel.deleteEssayInStory(navController)
        },{
            isStoryClicked = false
            viewModel.isActionClicked = false
            viewModel.modifyEssayInStory(navController)
        },
            {
                viewModel.isActionClicked = false
                isStoryClicked = false
            },viewModel)
    }
    if (!isStoryClicked){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 23.dp),
            contentAlignment = Alignment.TopEnd
        ){
            Surface(modifier = Modifier.size(180.dp, 305.dp), shape = RoundedCornerShape(2)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.text_minus),
                            contentDescription = "minus",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable { viewModel.textSizeDown() }
                        )
                        Spacer(modifier = Modifier.width(30.dp))
                        Text(text = "가", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(30.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.text_plus),
                            contentDescription = "minus",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable { viewModel.textSizeUp() }

                        )

                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider()
                    OptionItem(text = "수정", Color.White,{
                        writingViewModel.title.value = TextFieldValue(viewModel.readDetailEssay().title!!)
                        writingViewModel.content = TextFieldValue(viewModel.readDetailEssay().content!!)
                        writingViewModel.hashTagList = viewModel.readDetailEssay().tags!!.map { it.name }.toMutableStateList()
                        writingViewModel.latitude = viewModel.readDetailEssay().latitude
                        writingViewModel.longitude = viewModel.readDetailEssay().longitude
                        writingViewModel.locationText = viewModel.readDetailEssay().location ?: ""
                        writingViewModel.imageUrl = viewModel.readDetailEssay().thumbnail ?: ""


                        navController.navigate("WritingPage")

                                                        },R.drawable.ftb_edit)
                    HorizontalDivider()
                    OptionItem(text = "발행", Color.White,{viewModel.updateEssayToPublished(navController)},R.drawable.option_link)
                    HorizontalDivider()
                    OptionItem(text = "Linked-out", Color.White,{viewModel.updateEssayToLinkedOut(navController)},R.drawable.option_linkedout)
                    HorizontalDivider()
                    OptionItem(text = "스토리 선택", Color(0xFF616FED),
                        {
                            isStoryClicked = true
                        },R.drawable.option_check)
                    HorizontalDivider()
                    OptionItem(text = "삭제", Color.Red,{
                        viewModel.deleteEssay(navController = navController)
                        Log.d(TAG, "ModifyOption: dd")
                    },R.drawable.option_trash)

                }
            }
        }
    }



}

@Composable
fun OptionItem(
    text: String,
    color: Color,
    onClick: () -> Unit,
    iconResource : Int
) {
    Box(
        Modifier
            .size(180.dp, 44.dp)
            .clickable { onClick() }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 20.dp,
                    end = 20.dp
                ), contentAlignment = Alignment.CenterStart
        ) {
            Text(text = text, fontSize = 16.sp, color = color)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 20.dp,
                    end = 20.dp
                ), contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                tint = color,
                painter = painterResource(id = iconResource),
                contentDescription = "",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopAppBar(navController: NavController, viewModel: MyLogViewModel) {

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
fun DetailEssay(viewModel: MyLogViewModel) {
    val essay = viewModel.detailEssay
    Box {
        Column {
            if (essay.thumbnail !=null){
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp), contentAlignment = Alignment.Center){
                    GlideImage(model = essay.thumbnail, contentDescription = "essay Thumbnail", contentScale = ContentScale.FillHeight)

                }
            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(text = essay.title!!, fontSize = viewModel.titleTextSize, modifier = Modifier)
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = essay.content!!,
                    fontSize = viewModel.contentTextSize,
                    modifier = Modifier,
                    color = Color(0xFFB4B4B4)
                )
                Spacer(modifier = Modifier.height(46.dp))
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                    Column {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                            (if (essay.author !=null) essay.author!!.nickname else "")?.let {
                                Text(
                                    text = it,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.End,
                                    color = Color(0xFF686868)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                            Text(
                                text = essay.createdDate ?: "",
                                fontSize = 12.sp,
                                textAlign = TextAlign.End,
                                color = Color(0xFF686868)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (essay.linkedOutGauge != null){
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                                Row {
                                    repeat(essay.linkedOutGauge!!) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ring),
                                            contentDescription = "ring",
                                            modifier = Modifier.size(14.dp),
                                            colorFilter = ColorFilter.tint(Color(0xFF686868))
                                        )
                                        if (it != essay.linkedOutGauge!! - 1) Spacer(
                                            modifier = Modifier.width(
                                                4.dp
                                            )
                                        )
                                    }
                                }
                            }
                        }

                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
                if (essay.tags != null){
                    Row {
                        repeat(essay.tags!!.size){
                            SuggestionChip(
                                onClick = { },
                                label = { Text(essay.tags!![it].name) },
                                shape = RoundedCornerShape(50)
                            )
                            if (it != essay.tags!!.size-1) Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                }
            }


        }


    }

}

@Composable
fun StoryModifyBox(
    isDeleteClicked: () -> Unit,
    isModifyClicked: () -> Unit,
    isBackgroundClicked : ()->Unit,
    viewModel: MyLogViewModel
) {

    val items = viewModel.storyList

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { isBackgroundClicked() }
            .background(Color.Black.copy(0.5f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .background(Color(0xFF121212), shape = RoundedCornerShape(20))
                .align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "이 글을 어떤 스토리로 추가/변경 할까요?",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                SingleSelectableList(items,viewModel)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { isDeleteClicked() },
                        modifier = Modifier
                            .weight(1f)
                            .height(61.dp)
                            .padding(end = 10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF868686)),
                        shape = RoundedCornerShape(20)
                    ) {
                        Text(text = "스토리에서 삭제", color = Color.Black, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = { isModifyClicked() },
                        modifier = Modifier
                            .weight(1f)
                            .height(61.dp)
                            .padding(start = 10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF616FED)),
                        shape = RoundedCornerShape(20)
                    ) {
                        Text(text = "추가/변경", color = Color.Black, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun SingleSelectableList(items: List<Story>,viewModel: MyLogViewModel) {
    // 선택된 항목을 추적하기 위한 상태 변수
    var selectedItem by remember { mutableStateOf(viewModel.findStoryInEssay()) }

    LazyColumn(Modifier.height(250.dp)) {
        items(items) { item ->
            val isSelected = item == selectedItem
            val backgroundColor = Color.Transparent

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clickable {
                        // 같은 항목을 클릭하면 선택을 해제, 그렇지 않으면 항목을 선택
                        selectedItem = if (selectedItem == item) {
                            null
                        } else item
                        if (selectedItem != null) {
                            viewModel.setSelectStory(selectedItem!!)
                        }

                    }
                    .background(backgroundColor)
                    .padding(16.dp)
            ) {

                    Text(text = item.name)
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd){
                    if (isSelected) Icon(painter = painterResource(id = R.drawable.option_check), tint = LinkedInColor, contentDescription = "")
                }

            }
        }
    }
}


@Composable
fun CompletedEssayPage(navController: NavController, viewModel: MyLogViewModel,writingViewModel: WritingViewModel) {
    var hasCalledApi by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        if (!hasCalledApi) {
            viewModel.readMyEssay()
            viewModel.readPublishEssay()
            delay(100)

            hasCalledApi = true
        }
    }

    val scrollState = rememberScrollState()

    if (hasCalledApi){
        LinkedOutTheme {
            Scaffold(
                topBar = {
                    DetailTopAppBar(navController = navController, viewModel)
                },
                content = {
                    Box(
                        Modifier
                            .padding(it)
                            .fillMaxSize(), contentAlignment = Alignment.TopCenter
                    ) {

                        Column(Modifier.verticalScroll(scrollState)) {
                            DetailEssay2(viewModel = viewModel)
                            LastEssayPager(viewModel = viewModel, navController = navController)
                        }
                        //수정 옵션
                        AnimatedVisibility(
                            visible = viewModel.isActionClicked,
                            enter = fadeIn(
                                animationSpec = tween(
                                    durationMillis = 1000,
                                    easing = FastOutSlowInEasing
                                )
                            ),
                            exit = fadeOut(
                                animationSpec = tween(
                                    durationMillis = 500,
                                    easing = LinearEasing
                                )
                            )
                        ) {

                            ModifyOption(viewModel, navController = navController, writingViewModel = writingViewModel)

                        }
                        val text = when(viewModel.readDetailEssay().status){
                            "private" -> "저장"
                            "published" ->"발행"
                            "linkedout" -> "링크드아웃"
                            else -> {"검토중"}
                        }
                        WriteCompleteBox(type = text)


                    }
                }
            )
        }
    }
    else{
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black))
    }



}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailEssay2(viewModel: MyLogViewModel) {
    val essay = viewModel.readDetailEssay()
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            if (essay.thumbnail !=null){
                Column {
                    GlideImage(
                        model = essay.thumbnail, contentDescription = "", modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = essay.title!!, fontSize = viewModel.titleTextSize, modifier = Modifier)
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = essay.content!!,
                fontSize = viewModel.contentTextSize,
                modifier = Modifier,
                color = Color(0xFFB4B4B4)
            )
            Spacer(modifier = Modifier.height(46.dp))
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                Column {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                        (if (essay.author !=null) essay.author!!.nickname else "")?.let {
                            Text(
                                text = it,
                                fontSize = 12.sp,
                                textAlign = TextAlign.End,
                                color = Color(0xFF686868)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                        Text(
                            text = essay.createdDate ?: "",
                            fontSize = 12.sp,
                            textAlign = TextAlign.End,
                            color = Color(0xFF686868)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (essay.linkedOutGauge != null){
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                            Row {
                                repeat(essay.linkedOutGauge!!) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ring),
                                        contentDescription = "ring",
                                        modifier = Modifier.size(14.dp),
                                        colorFilter = ColorFilter.tint(Color(0xFF686868))
                                    )
                                    if (it != essay.linkedOutGauge!! - 1) Spacer(
                                        modifier = Modifier.width(
                                            4.dp
                                        )
                                    )
                                }
                            }
                        }
                    }

                }
            }
            Spacer(modifier = Modifier.height(28.dp))
            if (essay.tags != null){
                Row {
                    repeat(essay.tags!!.size){
                        SuggestionChip(
                            onClick = { },
                            label = { Text(essay.tags!![it].name) },
                            shape = RoundedCornerShape(50)
                        )
                        if (it != essay.tags!!.size-1) Spacer(modifier = Modifier.width(10.dp))
                    }
                }
            }

        }

    }

}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WriteCompleteBox(type : String){
    var isVisible by remember {
        mutableStateOf(true)
    }
    val text = when(type){
        "저장" -> "아무개님의 새 글이\n" +
                "'나만의 글'에 저장됐어요!"
        "발행" -> "아무개님의 새 글이\n" +
                "숨바꼭질을 시작했어요!"
        "링크드아웃" -> "아무개님의 새 글이\n숨바꼭질을 시작했어요!"
        "검토중" -> "아무개님의 새 글을\n꼼꼼하게 검토중이에요"
        else -> {""}
    }

    val imageUrl = when(type){
        "저장" -> PRIVATE_POPUP_URL
        "발행" -> PUBLISHED_POPUP_URL
        "링크드아웃" -> LINKEDOUT_POPUP_URL
        "검토중" -> INSPECT_POPUP_URL
        else -> {""}
    }

    if (isVisible){
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.7f))){

        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Box(modifier = Modifier.size(300.dp,286.dp)){
                GlideImage(model = R.drawable.box_complete, contentDescription = "completeBox", modifier = Modifier.fillMaxSize())
                Row(
                    Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 50.dp)
                        .fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    GlideImage(model = imageUrl, contentDescription = "", modifier = Modifier
                        .weight(2f)
                        .height(210.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(4f)) {
                        Row {
                            Text(text = "$type ", color = LinkedInColor, fontSize = 24.sp)
                            Text(text = "완료", fontSize = 24.sp, color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(text = text, textAlign = TextAlign.Center, color = Color.White)

                    }
                }
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){
                    Button(onClick = {
                        isVisible = false

                    },
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp), shape = RoundedCornerShape(20), colors = ButtonDefaults.buttonColors(containerColor = LinkedInColor)) {
                        Text(text = "닫기")
                    }
                }
            }



        }
    }

}






