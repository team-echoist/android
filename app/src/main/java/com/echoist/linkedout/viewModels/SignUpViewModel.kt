package com.echoist.linkedout.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {
    var userEmail = mutableStateOf("")
    var userPw = mutableStateOf("")

    var agreement_service = mutableStateOf(false)
    var agreement_collection = mutableStateOf(false)
    var agreement_teen = mutableStateOf(false)
    var agreement_marketing = mutableStateOf(false)

}