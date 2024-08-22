package com.echoist.linkedout.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.echoist.linkedout.R
import com.echoist.linkedout.components.MyLogTopAppBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabletDrawableTopBar(title: String, isBack: Boolean = false, onCloseClick: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                textAlign = TextAlign.Center
            )
        },
        actions = {
            if (isBack.not()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "close",
                    modifier = Modifier
                        .clickable { onCloseClick() }
                        .padding(end = 20.dp)
                        .size(30.dp),
                    tint = Color.White
                )
            }
        },
        navigationIcon = {
            if (isBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "arrow back",
                    modifier = Modifier
                        .clickable { onCloseClick() }
                        .padding(start = 10.dp)
                        .size(30.dp),
                    tint = Color.White
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabletMainTopBar(
    onClick: () -> Unit,
    onClickNotification: () -> Unit,
    isExistUnreadAlerts: Boolean,
    isClickedTutorial: () -> Unit
) {
    val img = if (isExistUnreadAlerts) R.drawable.icon_noti_on else R.drawable.icon_noti_off
    TopAppBar(
        title = { }, colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
        navigationIcon = {
            Icon(
                tint = Color.White,
                painter = painterResource(id = R.drawable.hamburber),
                contentDescription = "Menu",
                modifier = Modifier
                    .clickable { onClick() }
                    .padding(start = 10.dp)
                    .size(24.dp)
            )
        },
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.icon_information),
                contentDescription = "Notifications",
                tint = Color.White,
                modifier = Modifier
                    .clickable { isClickedTutorial() }
                    .padding(end = 10.dp)
                    .size(30.dp)
            )
            Icon(
                painter = painterResource(id = img),
                contentDescription = "Notifications",
                tint = Color.Unspecified,
                modifier = Modifier
                    .clickable { onClickNotification() }
                    .padding(end = 10.dp)
                    .size(30.dp)
            )
        },
    )
}