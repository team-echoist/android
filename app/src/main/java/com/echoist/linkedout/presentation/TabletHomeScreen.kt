package com.echoist.linkedout.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.echoist.linkedout.R

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