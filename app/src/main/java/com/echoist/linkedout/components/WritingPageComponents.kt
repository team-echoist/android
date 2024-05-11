package com.echoist.linkedout.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun BlankWarningAlert(dialogState: MutableState<Boolean>){
        AlertDialog(
            onDismissRequest = { dialogState.value = false },
            confirmButton = {
                Button(onClick = { dialogState.value = false }) {
                    Text(text = "확인")
                }
            },
            text = { Text(text = "제목, 또는 내용이 공백이면 완료할 수 없습니다.") },
        )
    }

