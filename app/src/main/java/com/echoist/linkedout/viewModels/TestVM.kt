package com.echoist.linkedout.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TestVM @Inject constructor(writingViewModel: WritingViewModel) : ViewModel(){
    val test = writingViewModel.accessToken
}