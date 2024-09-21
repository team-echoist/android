package com.echoist.linkedout.viewModels

import androidx.compose.animation.fadeIn
import androidx.lifecycle.ViewModel
import com.echoist.linkedout.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    fun logout() {
        userDataRepository.saveClickedAutoLogin(false)
    }

}