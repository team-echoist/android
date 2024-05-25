package com.echoist.linkedout.components

import android.content.ContentValues.TAG
import android.content.res.Configuration
import android.util.Log
import android.view.ViewConfiguration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Commit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastSumBy
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.api.EssayApi
import com.echoist.linkedout.viewModels.MyLogViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyLogTopAppBar(){
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        title = {
            Text(text = "구루브 님")
        },
        actions = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "", Modifier.size(30.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "",
                Modifier.size(30.dp)
            )

        }
    )
}
@Composable
fun EssayChips(pagerState: PagerState,viewModel: MyLogViewModel){
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(26.dp)){
//        HorizontalDivider(modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 25.dp)
//            , thickness = 1.dp
//            , color = Color(0xFF686868))

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 17.dp)) {
            Essaychip(
                text = "나만의 글 ${viewModel.myEssayList.size}",
                65.dp,
                {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(0)

                    }
                },
                color = if (pagerState.currentPage == 0) Color.White else Color.Gray
            )
            Essaychip(
                text = "발행한 글 ${viewModel.publishedEssayList.size}",
                65.dp,
                {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                color = if (pagerState.currentPage == 1) Color.White else Color.Gray
            )
            Essaychip(
                text = "에세이 모음 3",
                78.dp,
                {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(2)
                    }
                },
                color = if (pagerState.currentPage == 2) Color.White else Color.Gray
            )

        }
    }
}

@Composable
fun Essaychip(
    text: String,
    dividerWidth: Dp,
    clickable: () -> Unit,
    color: Color
    // todo pager number값을넣고 그에따른 색 변화, 터쳐블 변화 주기
){

    Column(modifier = Modifier.padding(end = 12.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            fontSize = 14.sp,
            text = text,
            color = color, // 색상을 먼저 적용합니다
            modifier = Modifier.clickable { clickable() } // Modifier.clickable을 마지막에 적용합니다
        )

        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(modifier = Modifier
            .width(dividerWidth),
            color = color,
            thickness = 2.dp)
    }
}


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun EssayListItem(
    item: EssayApi.EssayItem,
    pagerState: PagerState,
    viewModel: MyLogViewModel,
    navController: NavController
){
    val color = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
    }
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            viewModel.detailEssay = item
            viewModel.detailEssayBackStack.push(item)
            navController.navigate("MyLogDetailPage")
            Log.d(TAG, "pushpush: ${viewModel.detailEssayBackStack}")
        }
        .height(180.dp)){
        if (item.thumbnail != null){
            Box(modifier = Modifier.fillMaxSize()){
                GlideImage(model = item.thumbnail, contentDescription = "")
            }
        }
        //타이틀
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.title,
                    color = color,
                    fontSize = 20.sp,

                    )
                if (pagerState.currentPage == 1){
                    Spacer(modifier = Modifier.width(10.dp))
                    Surface(color = Color(0xFFFFBB36), modifier = Modifier.size(45.dp,18.dp), shape = CircleShape) {

                        Text(text = "Out", textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 1.dp))
                    }
                }

            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = item.content,
                maxLines = 3,
                color = color,
                overflow = TextOverflow.Ellipsis
            )

        }
        Box(contentAlignment = Alignment.TopEnd, modifier = Modifier
            .fillMaxSize()
            .padding(end = 20.dp, top = 20.dp)) {
            Icon(imageVector = Icons.Default.Commit, contentDescription = "", tint = color,)
        }

        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier
            .fillMaxSize()
            .padding(end = 20.dp, bottom = 20.dp)) {
            Text(text = item.createdDate!!, fontSize = 10.sp, color = Color(0xFF686868))
        }
        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier
            .fillMaxSize()) {
            HorizontalDivider(color = Color(0xFF686868))
        }

    }
}

@Composable
fun EssayListPage1(viewModel: MyLogViewModel, pagerState: PagerState, navController: NavController){

        LazyColumn {
            items(viewModel.myEssayList){it->
                EssayListItem(item = it,pagerState,viewModel,navController)
            }
        }
}

@Composable
fun EssayListPage2(viewModel: MyLogViewModel, pagerState: PagerState, navController: NavController){

        LazyColumn {
            items(viewModel.publishedEssayList) {
                EssayListItem(item = it, pagerState, viewModel, navController)
            }
        }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EssayPager(pagerState: PagerState, viewModel: MyLogViewModel, navController: NavController) {
    HorizontalPager(state = pagerState, modifier = Modifier.padding(top = 20.dp)) { page ->
        when (page) {
            0 -> EssayListPage1(viewModel,pagerState,navController)
            1 -> EssayListPage2(viewModel,pagerState,navController)
        }
    }
}

fun Modifier.drawHorizontalScrollbar(
    state: ScrollState,
    reverseScrolling: Boolean = false
): Modifier = drawScrollbar(state, Orientation.Horizontal, reverseScrolling)

fun Modifier.drawVerticalScrollbar(
    state: ScrollState,
    reverseScrolling: Boolean = false
): Modifier = drawScrollbar(state, Orientation.Vertical, reverseScrolling)

private fun Modifier.drawScrollbar(
    state: ScrollState,
    orientation: Orientation,
    reverseScrolling: Boolean
): Modifier = drawScrollbar(
    orientation, reverseScrolling
) { reverseDirection, atEnd, color, alpha ->
    if (state.maxValue > 0) {
        val canvasSize = if (orientation == Orientation.Horizontal) size.width else size.height
        val totalSize = canvasSize + state.maxValue
        val thumbSize = canvasSize / totalSize * canvasSize
        val startOffset = state.value / totalSize * canvasSize
        drawScrollbar(
            orientation, reverseDirection, atEnd, color, alpha, thumbSize, startOffset
        )
    }
}

fun Modifier.drawHorizontalScrollbar(
    state: LazyListState,
    reverseScrolling: Boolean = false
): Modifier = drawScrollbar(state, Orientation.Horizontal, reverseScrolling)

fun Modifier.drawVerticalScrollbar(
    state: LazyListState,
    reverseScrolling: Boolean = false
): Modifier = drawScrollbar(state, Orientation.Vertical, reverseScrolling)

private fun Modifier.drawScrollbar(
    state: LazyListState,
    orientation: Orientation,
    reverseScrolling: Boolean
): Modifier = drawScrollbar(
    orientation, reverseScrolling
) { reverseDirection, atEnd, color, alpha ->
    val layoutInfo = state.layoutInfo
    val viewportSize = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
    val items = layoutInfo.visibleItemsInfo
    val itemsSize = items.fastSumBy { it.size }
    if (items.size < layoutInfo.totalItemsCount || itemsSize > viewportSize) {
        val estimatedItemSize = if (items.isEmpty()) 0f else itemsSize.toFloat() / items.size
        val totalSize = estimatedItemSize * layoutInfo.totalItemsCount
        val canvasSize = if (orientation == Orientation.Horizontal) size.width else size.height
        val thumbSize = viewportSize / totalSize * canvasSize
        val startOffset = if (items.isEmpty()) 0f else items.first().run {
            (estimatedItemSize * index - offset) / totalSize * canvasSize
        }
        drawScrollbar(
            orientation, reverseDirection, atEnd, color, alpha, thumbSize, startOffset
        )
    }
}

fun Modifier.drawVerticalScrollbar(
    state: LazyGridState,
    spanCount: Int,
    reverseScrolling: Boolean = false
): Modifier = drawScrollbar(
    Orientation.Vertical, reverseScrolling
) { reverseDirection, atEnd, color, alpha ->
    val layoutInfo = state.layoutInfo
    val viewportSize = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
    val items = layoutInfo.visibleItemsInfo
    val rowCount = (items.size + spanCount - 1) / spanCount
    var itemsSize = 0
    for (i in 0 until rowCount) {
        itemsSize += items[i * spanCount].size.height
    }
    if (items.size < layoutInfo.totalItemsCount || itemsSize > viewportSize) {
        val estimatedItemSize = if (rowCount == 0) 0f else itemsSize.toFloat() / rowCount
        val totalRow = (layoutInfo.totalItemsCount + spanCount - 1) / spanCount
        val totalSize = estimatedItemSize * totalRow
        val canvasSize = size.height
        val thumbSize = viewportSize / totalSize * canvasSize
        val startOffset = if (rowCount == 0) 0f else items.first().run {
            val rowIndex = index / spanCount
            (estimatedItemSize * rowIndex - offset.y) / totalSize * canvasSize
        }
        drawScrollbar(
            Orientation.Vertical, reverseDirection, atEnd, color, alpha, thumbSize, startOffset
        )
    }
}

private fun DrawScope.drawScrollbar(
    orientation: Orientation,
    reverseDirection: Boolean,
    atEnd: Boolean,
    color: Color,
    alpha: () -> Float,
    thumbSize: Float,
    startOffset: Float
) {
    val thicknessPx = Thickness.toPx()
    val topLeft = if (orientation == Orientation.Horizontal) {
        Offset(
            if (reverseDirection) size.width - startOffset - thumbSize else startOffset,
            if (atEnd) size.height - thicknessPx else 0f
        )
    } else {
        Offset(
            if (atEnd) size.width - thicknessPx else 0f,
            if (reverseDirection) size.height - startOffset - thumbSize else startOffset
        )
    }
    val size = if (orientation == Orientation.Horizontal) {
        Size(thumbSize, thicknessPx)
    } else {
        Size(thicknessPx, thumbSize)
    }

    drawRect(
        color = color,
        topLeft = topLeft,
        size = size,
        alpha = alpha()
    )
}

private fun Modifier.drawScrollbar(
    orientation: Orientation,
    reverseScrolling: Boolean,
    onDraw: DrawScope.(
        reverseDirection: Boolean,
        atEnd: Boolean,
        color: Color,
        alpha: () -> Float
    ) -> Unit
): Modifier = composed {
    val scrolled = remember {
        MutableSharedFlow<Unit>(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    }
    val nestedScrollConnection = remember(orientation, scrolled) {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = if (orientation == Orientation.Horizontal) consumed.x else consumed.y
                if (delta != 0f) scrolled.tryEmit(Unit)
                return Offset.Zero
            }
        }
    }

    val alpha = remember { Animatable(0f) }
    LaunchedEffect(scrolled, alpha) {
        scrolled.collectLatest {
            alpha.snapTo(1f)
            delay(ViewConfiguration.getScrollDefaultDelay().toLong())
            alpha.animateTo(0f, animationSpec = FadeOutAnimationSpec)
        }
    }

    val isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr
    val reverseDirection = if (orientation == Orientation.Horizontal) {
        if (isLtr) reverseScrolling else !reverseScrolling
    } else reverseScrolling
    val atEnd = if (orientation == Orientation.Vertical) isLtr else true

    val color = BarColor

    Modifier
        .nestedScroll(nestedScrollConnection)
        .drawWithContent {
            drawContent()
            onDraw(reverseDirection, atEnd, color, alpha::value)
        }
}

private val BarColor: Color
    @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)

private val Thickness = 4.dp
private val FadeOutAnimationSpec =
    tween<Float>(durationMillis = ViewConfiguration.getScrollBarFadeDuration())

@Preview(widthDp = 400, heightDp = 400, showBackground = true)
@Composable
internal fun ScrollbarPreview() {
    val state = rememberScrollState()
    Column(
        modifier = Modifier
            .drawVerticalScrollbar(state)
            .verticalScroll(state),
    ) {
        repeat(50) {
            Text(
                text = "Item ${it + 1}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Preview(widthDp = 400, heightDp = 400, showBackground = true)
@Composable
internal fun LazyListScrollbarPreview() {
    val state = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.drawVerticalScrollbar(state),
        state = state
    ) {
        items(50) {
            Text(
                text = "Item ${it + 1}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Preview(widthDp = 400, showBackground = true)
@Composable
internal fun HorizontalScrollbarPreview() {
    val state = rememberScrollState()
    Row(
        modifier = Modifier
            .drawHorizontalScrollbar(state)
            .horizontalScroll(state)
    ) {
        repeat(50) {
            Text(
                text = (it + 1).toString(),
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 16.dp)
            )
        }
    }
}

@Preview(widthDp = 400, showBackground = true)
@Composable
internal fun LazyListHorizontalScrollbarPreview() {
    val state = rememberLazyListState()
    LazyRow(
        modifier = Modifier.drawHorizontalScrollbar(state),
        state = state
    ) {
        items(50) {
            Text(
                text = (it + 1).toString(),
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 16.dp)
            )
        }
    }
}