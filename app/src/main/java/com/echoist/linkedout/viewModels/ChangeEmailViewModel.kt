package com.echoist.linkedout.viewModels

import androidx.lifecycle.ViewModel
import com.echoist.linkedout.data.ExampleItems
import com.echoist.linkedout.data.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeEmailViewModel @Inject constructor(
    private val exampleItems: ExampleItems
) : ViewModel() {

    fun getMyInfo(): UserInfo {
        return exampleItems.myProfile
    }
}