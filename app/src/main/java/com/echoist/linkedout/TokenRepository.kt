package com.echoist.linkedout

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepository @Inject constructor() {
    private var _isReAuthenticationRequired = mutableStateOf(false)
    val isReAuthenticationRequired: State<Boolean> = _isReAuthenticationRequired
    fun setReAuthenticationRequired(value: Boolean) {
        _isReAuthenticationRequired.value = value
    }
}