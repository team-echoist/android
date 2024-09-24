package com.echoist.linkedout.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepository @Inject constructor() {
    private val _isReAuthenticationRequired = MutableStateFlow(false)
    val isReAuthenticationRequired: StateFlow<Boolean> = _isReAuthenticationRequired

    fun setReAuthenticationRequired(value: Boolean) {
        _isReAuthenticationRequired.value = value
    }
}
