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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.Routes
import com.echoist.linkedout.TYPE_STORY
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.components.ModifyStoryBox
import com.echoist.linkedout.data.Story
import com.echoist.linkedout.formatDateTime
import com.echoist.linkedout.navigateWithClearBackStack
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.viewModels.MyLogViewModel

@Composable
fun StoryDetailPage(viewModel: MyLogViewModel, navController: NavController) {
    viewModel.readEssayListInStory()


    Scaffold(topBar = {
        StoryDetailTopAppBar(navController, viewModel) {
            viewModel.isModifyStoryClicked = true
        }
    }, bottomBar = {})
    {
        Column(Modifier.padding(it)) {
            StoryDetailTitle(viewModel.getSelectedStory(), viewModel.getUserInfo().nickname!!)
            Spacer(modifier = Modifier.height(20.dp))
            StoryDetailList(viewModel, navController)
        }
        if (viewModel.isModifyStoryClicked) {
            ModifyStoryBox(viewModel = viewModel, navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun StoryDetailTopAppBar(
    navController: NavController,
    viewModel: MyLogViewModel,
    isModifyClicked: () -> Unit
) {
    TopAppBar(modifier = Modifier.padding(horizontal = 10.dp),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
            ) {
                //StoryChip()
                GlideImage(
                    model = R.drawable.storychip_icon, contentDescription = "", modifier = Modifier
                        .padding(top = 1.dp)
                        .size(45.dp, 18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = viewModel.getSelectedStory().name,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "${viewModel.getSelectedStory().essaysCount}편",
                    color = Color(0xFF6B6B6B),
                    fontSize = 16.sp
                )

            }

        },
        actions = {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "",
                Modifier.clickable { isModifyClicked() })
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                Modifier
                    .size(30.dp)

                    .clickable {
                        navigateWithClearBackStack(navController, "${Routes.MyLog}/2")
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
fun StoryDetailTitle(story: Story, userName: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(LinkedInColor)
            .fillMaxWidth()
            .height(170.dp)
            .padding(horizontal = 44.dp)
    ) {
        Text(
            text = "${story.essaysCount}편의 글",
            fontSize = 12.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = story.name, fontSize = 24.sp, fontWeight = FontWeight.Bold,
            color = Color(0xFF000000),
        )
        Spacer(modifier = Modifier.height(11.dp))
        Text(
            text = "$userName 아무개",
            fontSize = 12.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold
        )
    }
}

//viewmodel.modiftstorylist
@Composable
fun StoryDetailItem(essayItem: EssayApi.EssayItem, num: Int, isItemClicked: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable { isItemClicked() }
        .height(91.dp)) {
        Row(Modifier.padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "$num", color = LinkedInColor)
            Spacer(modifier = Modifier.width(40.dp))
            Column(
                Modifier
                    .weight(0.5f)
                    .fillMaxSize(), verticalArrangement = Arrangement.Center
            ) {
                Text(text = essayItem.title!!, fontSize = 16.sp, color = LinkedInColor)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = formatDateTime(essayItem.createdDate!!),
                    fontSize = 12.sp,
                    color = Color(0xFF3E415B)
                )
            }
            if (essayItem.status == "published") {
                Icon(
                    painter = painterResource(id = R.drawable.option_link),
                    tint = Color(0xFF3E415B),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun StoryDetailList(viewModel: MyLogViewModel, navController: NavController) {
    LazyColumn(Modifier.padding(start = 16.dp)) {
        items(viewModel.modifyStoryEssayItems) {
        }
        itemsIndexed(viewModel.essayListInStroy) { i, essay ->
            StoryDetailItem(essay, i + 1) {
                viewModel.readDetailEssayInStory(
                    essay.id!!, navController, i + 1,
                    TYPE_STORY, viewModel.getSelectedStory().id!!
                )
            }
        }
    }
}

@Preview
@Composable
fun StoryChip() {

    Text(
        text = "   스토리   ",
        color = Color.Black,
        textAlign = TextAlign.Center,
        fontSize = 10.sp,
        modifier = Modifier
            .height(18.dp)
            .padding(bottom = 5.dp)
            .background(
                LinkedInColor, shape = RoundedCornerShape(50)
            )
    )

}

