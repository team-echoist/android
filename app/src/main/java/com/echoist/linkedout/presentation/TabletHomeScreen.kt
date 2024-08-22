package com.echoist.linkedout.presentation

import android.annotation.SuppressLint
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R
import com.echoist.linkedout.page.home.LineChartExample
import com.echoist.linkedout.page.home.LogoutBtn
import com.echoist.linkedout.page.home.MyLinkedOutBar
import com.echoist.linkedout.page.home.MyProfile
import com.echoist.linkedout.page.home.ShopDrawerItem
import com.echoist.linkedout.viewModels.HomeViewModel
import com.echoist.linkedout.viewModels.WritingViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun TabletHomeRoute(
    statusCode: Int
) {
    GlideImage(
        model = R.drawable.home_basic_tablet,
        contentDescription = "home_img",
        modifier = Modifier
            .fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )
}

@Composable
fun TabletDrawableItems(text: String, isSelected: Boolean, onClick: () -> Unit) {
    NavigationDrawerItem(
        modifier = Modifier.height(70.dp),
        label = {
            Text(
                text = text,
                color = if (isSelected) Color(0xFF616FED) else Color.White,
                fontWeight = FontWeight.SemiBold
            )
        },
        selected = false,
        onClick = { onClick() },
    )
}