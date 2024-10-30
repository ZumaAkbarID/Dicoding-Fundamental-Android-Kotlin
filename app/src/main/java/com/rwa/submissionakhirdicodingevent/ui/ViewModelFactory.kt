package com.rwa.submissionakhirdicodingevent.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rwa.submissionakhirdicodingevent.data.EventRepository
import com.rwa.submissionakhirdicodingevent.di.Injection
import com.rwa.submissionakhirdicodingevent.preference.SettingPreferences
import com.rwa.submissionakhirdicodingevent.ui.detail.DetailViewModel
import com.rwa.submissionakhirdicodingevent.ui.favorite.FavoriteViewModel
import com.rwa.submissionakhirdicodingevent.ui.finish.FinishViewModel
import com.rwa.submissionakhirdicodingevent.ui.home.HomeViewModel
import com.rwa.submissionakhirdicodingevent.ui.setting.SettingViewModel

class ViewModelFactory private constructor(
    private val eventRepository: EventRepository,
    private val pref: SettingPreferences
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(eventRepository) as T
        }
        if (modelClass.isAssignableFrom(FinishViewModel::class.java)) {
            return FinishViewModel(eventRepository) as T
        }
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(eventRepository) as T
        }
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(eventRepository) as T
        }
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(pref, eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(context),
                    Injection.providePref(context)
                )
            }.also { instance = it }
    }
}