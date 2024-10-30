package com.rwa.submissionakhirdicodingevent.di

import android.content.Context
import com.rwa.submissionakhirdicodingevent.data.EventRepository
import com.rwa.submissionakhirdicodingevent.data.local.room.EventDatabase
import com.rwa.submissionakhirdicodingevent.data.remote.retrofit.ApiConfig
import com.rwa.submissionakhirdicodingevent.preference.SettingPreferences
import com.rwa.submissionakhirdicodingevent.preference.dataStore

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        val pref = SettingPreferences.getInstance(context.dataStore)
        return EventRepository.getInstance(apiService, dao)
    }

    fun providePref(context: Context): SettingPreferences {
        return SettingPreferences.getInstance(context.dataStore)
    }
}