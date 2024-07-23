package com.echoist.linkedout.page.myLog

import android.content.ContentValues.TAG
import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.formatDateTime
import com.echoist.linkedout.ui.theme.LinkedInColor
import com.echoist.linkedout.ui.theme.LinkedOutTheme
import com.echoist.linkedout.viewModels.WritingViewModel


@Composable
fun TemporaryStoragePage(navController: NavController, viewModel: WritingViewModel) {
    var isModifyClicked by remember { mutableStateOf(false) }
    var selectedItems by remember { mutableStateOf<Set<EssayApi.EssayItem>>(emptySet()) }

    // 데이터 로드를 위한 remember와 LaunchedEffect
    val storageEssaysList by remember { mutableStateOf(viewModel.storageEssaysList)  }
    LaunchedEffect(Unit) {
        viewModel.getAllStoredData()
    }

    LinkedOutTheme {
        Scaffold(topBar = {
            StorageTopAppBar(
                navController = navController,
                onModifyClicked = { isModifyClicked = !isModifyClicked },
                isModifyClicked = isModifyClicked
            )
        }) {
            Column(Modifier.padding(it)) {
                StorageWritingEssay(viewModel.title.value.text, viewModel.getCurrentDate())
                if (isModifyClicked) {
                    StorageSelectBox(viewModel.storageEssaysList)
                } else {
                    Spacer(modifier = Modifier.height(20.dp))
                }
                StorageEssayList(
                    essayList = storageEssaysList,
                    isModifyClicked = isModifyClicked,
                    selectedItems = selectedItems,
                    onSelectionChange = { selectedItems = it },
                    viewModel = viewModel,
                    navController = navController,
                    modifier = Modifier.weight(7f)
                )
                val btnColor = if (selectedItems.isEmpty()) Color(0xFF868686) else LinkedInColor
                val selectedIds = selectedItems.map { it.essayPrimaryId }
                DeleteBtn(count = selectedItems.size,
                    onDeleteClicked = {
                    viewModel.deleteEssays(selectedIds)
                        Log.d(TAG, "TemporaryStoragePage: $selectedIds")
                    selectedItems = emptySet() }, btnColor = btnColor)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageTopAppBar(
    navController: NavController,
    onModifyClicked: () -> Unit,
    isModifyClicked: Boolean
) {
    val text = if (isModifyClicked) "완료" else "편집"

    androidx.compose.material3.TopAppBar(
        modifier = Modifier.padding(horizontal = 10.dp),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = "쓰다 만 글",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
        },
        actions = {
            Text(text = text, color = Color.White, modifier = Modifier.clickable { onModifyClicked() })
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                Modifier
                    .size(30.dp)

                    .clickable { navController.popBackStack() },
                tint = Color.White
            )
        }
    )
}

@Composable
fun StorageWritingEssay(title : String, createdDate : String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(97.dp)
            .background(Color(0xFF131313))
            .padding(horizontal = 35.dp)
    ) {
        Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(text = title, fontSize = 14.sp, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = createdDate, fontSize = 10.sp,
                    lineHeight = 15.sp,
                    color = Color(0xFF727070),
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(25.dp)
                    .background(color = Color(0xFFE43446), shape = RoundedCornerShape(size = 27.dp))
                    .padding(start = 6.dp, top = 1.dp, end = 6.dp, bottom = 1.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "작성중", fontSize = 10.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun StorageSelectBox(storageEssayList : List<EssayApi.EssayItem>) {

    Row(
        Modifier
            .fillMaxWidth()
            .height(61.dp)
            .padding(horizontal = 35.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Row (Modifier.weight(8f)){
            Text(text = "전체 ", color = Color.White)
            Text(text = "${storageEssayList.size} ", color = LinkedInColor)
            Text(text = "개 ", color = Color.White)

        }
        Box(
            modifier = Modifier
                .width(62.dp)
                .height(24.dp)
                .background(color = Color(0xFF131313), shape = RoundedCornerShape(size = 4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "전체 삭제", color = LinkedInColor, fontSize = 12.sp)
        }
    }
}

@Composable
fun StorageEssayItem(
    essayItem: EssayApi.EssayItem,
    isModifyClicked: Boolean,
    isSelected: Boolean,
    onItemSelected: (Boolean) -> Unit,
    isEssayClicked: () -> Unit
) {
    val color = if (isSelected) LinkedInColor else Color(0xFF252525)

    Box( modifier = Modifier
        .padding(horizontal = 35.dp)
        .fillMaxWidth()
        .clickable { if (!isModifyClicked) isEssayClicked() } //수정 상태가 아닐 때만 클릭 가능
        .height(73.dp)){
        Row(
            verticalAlignment = Alignment.CenterVertically,

        ) {
            Column(
                Modifier
                    .weight(8f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = essayItem.title!!, color = Color.White) // Added color to match the theme
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatDateTime(essayItem.createdDate!!), fontSize = 10.sp,
                    lineHeight = 15.sp,
                    color = Color(0xFF727070),
                )
            }
            if (isModifyClicked){
                IconButton(onClick = { onItemSelected(!isSelected) }) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        tint = color,
                        contentDescription = null
                    )
                }
            }


        }
        if (isModifyClicked && !isSelected) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.4f)))

        }
    }

}


@Composable
fun StorageEssayList(
    essayList: List<EssayApi.EssayItem>,
    isModifyClicked: Boolean,
    selectedItems: Set<EssayApi.EssayItem>,
    onSelectionChange: (Set<EssayApi.EssayItem>) -> Unit,
    viewModel: WritingViewModel,
    navController: NavController,
    modifier: Modifier
) {
    LazyColumn(modifier) {
        items(essayList) { item ->
            val isSelected = item in selectedItems

            StorageEssayItem(
                item,
                isModifyClicked = isModifyClicked,
                isSelected = isSelected,
                onItemSelected = { selected ->
                    val newSelectedItems = if (selected) {
                        selectedItems + item
                    } else {
                        selectedItems - item
                    }
                    onSelectionChange(newSelectedItems)
                },{viewModel.getEssayById(item.essayPrimaryId!!,navController)}
            )
        }

    }
}

@Composable
fun DeleteBtn(count: Int, onDeleteClicked: () -> Unit, btnColor: Color) {

    Log.d(TAG, "DeleteBtn: c$count")
    Button(
        onClick = { onDeleteClicked() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(61.dp),
        shape = RoundedCornerShape(20),
        colors = ButtonDefaults.buttonColors(containerColor = btnColor)
    ) {
        Text(text = "총 ${count}개 삭제")
    }
}
