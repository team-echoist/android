package com.echoist.linkedout.presentation.home.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echoist.linkedout.data.repository.UserDataRepository
import com.echoist.linkedout.presentation.home.drawable.setting.TimeSelectionIndex
import com.echoist.linkedout.presentation.util.getHourString
import com.echoist.linkedout.presentation.util.getMinuteString
import com.echoist.linkedout.presentation.util.getPeriodString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val _timeSelection = MutableStateFlow(userDataRepository.getTimeSelection())
    
    private val _writingRemindNotification =
        MutableStateFlow(userDataRepository.getWritingRemindNotification())
    val writingRemindNotification: StateFlow<Boolean> = _writingRemindNotification

    val hour: StateFlow<String> = _timeSelection.map { getHourString(it.hourIndex) }
        .stateIn(viewModelScope, SharingStarted.Lazily, "")

    val min: StateFlow<String> = _timeSelection.map { getMinuteString(it.minuteIndex) }
        .stateIn(viewModelScope, SharingStarted.Lazily, "")

    val period: StateFlow<String> = _timeSelection.map { getPeriodString(it.periodIndex) }
        .stateIn(viewModelScope, SharingStarted.Lazily, "")

    fun updateWritingRemindNotification(isEnabled: Boolean) {
        _writingRemindNotification.value = isEnabled
    }

    fun getTimeSelection(): TimeSelectionIndex {
        return userDataRepository.getTimeSelection()
    }

    fun saveWritingRemindNotification(isChecked: Boolean) {
        userDataRepository.saveWritingRemindNotification(isChecked)
    }

    fun saveTimeSelection(time: TimeSelectionIndex) {
        userDataRepository.saveTimeSelection(time)
    }

}