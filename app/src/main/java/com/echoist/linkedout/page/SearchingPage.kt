package com.echoist.linkedout.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.echoist.linkedout.viewModels.SearchingViewModel


@Composable
fun SearchingBar(viewModel: SearchingViewModel, onClick: () -> Unit, drawerState: DrawerState){

    val keyboardController = LocalSoftwareKeyboardController.current
    if (drawerState.isOpen) keyboardController?.show() else keyboardController?.hide()

    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp), verticalAlignment = Alignment.CenterVertically){
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
            contentDescription = "back",
            tint = Color.White,
            modifier = Modifier.clickable {
                keyboardController?.hide()
                onClick()
            }
        )
        Spacer(modifier = Modifier.width(10.dp))
        TextField(
            shape = RoundedCornerShape(30),
            modifier = Modifier
                .weight(1f)
                .height(55.dp),
            value = viewModel.searchingText,
            onValueChange = {
                viewModel.searchingText = it
            },
            placeholder = { Text(text = "검색", color = Color(0xFF686868)) },
            singleLine = true,
            trailingIcon = { if (viewModel.searchingText.isNotEmpty()) Icon(
                imageVector = Icons.Default.Cancel,
                contentDescription = "cancel",
                modifier = Modifier.clickable {
                    viewModel.searchingText = ""
                }
            )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF222222),
                unfocusedContainerColor = Color(0xFF222222),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
    }
}


