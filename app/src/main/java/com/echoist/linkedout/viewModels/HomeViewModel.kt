package com.echoist.linkedout.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.data.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val exampleItems: ExampleItems) : ViewModel(){
    val userItem = exampleItems.userItem
    var myProfile by mutableStateOf(exampleItems.myProfile)

    fun updateProfile(userInfo: UserInfo){
        exampleItems.myProfile = userInfo
    }

    fun getMyInfo(): UserInfo { // 함수 이름 변경
        return exampleItems.myProfile
    }
}