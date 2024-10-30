package com.rwa.submissionakhirdicodingevent.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.rwa.submissionakhirdicodingevent.data.EventRepository
import com.rwa.submissionakhirdicodingevent.preference.SettingPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingViewModel(
    private val pref: SettingPreferences,
    private val eventRepository: EventRepository
) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun getReminderSetting(): LiveData<Boolean> {
        return pref.getReminderSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun saveReminderSetting(isReminder: Boolean) {
        viewModelScope.launch {
            pref.saveReminderSetting(isReminder)
        }
    }

    fun getFirstActiveEvent() = liveData(Dispatchers.IO) {
        val (name, desc) = eventRepository.getFirstActiveEvent()
        emit(Pair(name, desc))
    }
}