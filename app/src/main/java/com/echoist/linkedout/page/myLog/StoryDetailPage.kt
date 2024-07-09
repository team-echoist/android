package com.echoist.linkedout.page.myLog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.echoist.linkedout.R
import com.echoist.linkedout.components.ModifyStoryBox
import com.echoist.linkedout.data.RelatedEssay
import com.echoist.linkedout.data.Story
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.MyLogViewModel

@Composable
fun StoryDetailPage(viewModel: MyLogViewModel, navController: NavController) {
    viewModel.readStoryEssayList()

        LinkedOutTheme {
            Scaffold(topBar = { StoryDetailTopAppBar(navController, viewModel) {
                viewModel.isModifyStoryClicked = true
            }
            }, bottomBar = {})
            {
                Column(Modifier.padding(it)) {
                    StoryDetailTitle(viewModel.getSelectedStory(),viewModel.getUserInfo().nickname!!)
                    StoryDetailList(viewModel,navController)
                }
                if (viewModel.isModifyStoryClicked){
                    ModifyStoryBox(viewModel = viewModel, navController = navController)
                }
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryDetailTopAppBar(navController: NavController, viewModel: MyLogViewModel,isModifyClicked : ()->Unit) {
    TopAppBar(modifier = Modifier.padding(horizontal = 10.dp),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SuggestionChip(onClick = { }, label = { Text(text = "스토리", fontSize = 10.sp) })
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "돌연한 출발", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "8편", color =  Color(0xFF6B6B6B), fontSize = 16.sp)

                }
            }
        },
        actions = {Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "",Modifier.clickable { isModifyClicked() })},
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                Modifier
                    .size(30.dp)
                    .clickable {
                        navController.popBackStack()
                        viewModel.modifyStoryEssayItems
                            .toMutableList()
                            .clear()
                        viewModel.setSelectStory(Story())
                        viewModel.storyTextFieldTitle = ""
                    },
                tint = Color.White
            )
        }
    )
}

@Composable
fun StoryDetailTitle(story: Story,userName : String){
    Column(verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(LinkedInColor)
            .fillMaxWidth()
            .height(170.dp)
            .padding(horizontal = 44.dp)) {
        Text(text = "${story.essaysCount}편의 글", fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = story.name,fontSize = 24.sp,
            color = Color(0xFF000000),)
        Spacer(modifier = Modifier.height(11.dp))
        Text(text = "$userName 아무개", fontSize = 12.sp)
    }
}
//viewmodel.modiftstorylist
@Composable
fun StoryDetailItem(essayItem: RelatedEssay, num : Int,isItemClicked : ()->Unit){
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable { isItemClicked() }
        .height(91.dp)){
        Row(Modifier.padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "$num")
            Spacer(modifier = Modifier.width(40.dp))
            Column(
                Modifier
                    .weight(0.5f)
                    .fillMaxSize(),verticalArrangement = Arrangement.Center) {
                Text(text = essayItem.title)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = essayItem.createdDate)
            }
            Icon(painter = painterResource(id = R.drawable.option_linkedout), tint = Color.Unspecified, contentDescription = "")
        }
    }
}

@Composable
fun StoryDetailList(viewModel: MyLogViewModel,navController : NavController){
    LazyColumn {
        items(viewModel.modifyStoryEssayItems){
        }
        itemsIndexed(viewModel.modifyStoryEssayItems){i,essay->
            StoryDetailItem(essay,i+1){viewModel.readDetailEssayInStory(essay.id,navController,i+1)}

        }
    }
}