package com.echoist.linkedout.viewModels

import androidx.lifecycle.ViewModel
import com.echoist.linkedout.data.ExampleItems
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val exampleItems: ExampleItems) : ViewModel(){
    val userItem = exampleItems.userItem
}