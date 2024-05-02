package com.echoist.linkedout.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel

class WritingViewModel : ViewModel() {

    var focusState = mutableStateOf(false)
    var title = mutableStateOf(TextFieldValue(""))
    var content = mutableStateOf(TextFieldValue(""))
    var date = mutableStateOf("")

}