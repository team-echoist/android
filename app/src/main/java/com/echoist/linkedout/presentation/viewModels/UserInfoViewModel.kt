package com.echoist.linkedout.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.echoist.linkedout.data.repository.UserDataRepository
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