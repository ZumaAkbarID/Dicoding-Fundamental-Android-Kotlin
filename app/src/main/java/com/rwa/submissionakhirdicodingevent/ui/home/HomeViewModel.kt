package com.rwa.submissionakhirdicodingevent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rwa.submissionakhirdicodingevent.data.EventRepository
import com.rwa.submissionakhirdicodingevent.data.local.entity.EventEntity
import kotlinx.coroutines.launch

class HomeViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Dicoding Event"
    }
    val text: LiveData<String> = _text

    private val _subtext = MutableLiveData<String>().apply {
        value = "Recommendation an active event for you!"
    }
    val subtext: LiveData<String> = _subtext

    fun getActiveEvent() = eventRepository.getActiveEvent()

    fun favEvent(e: EventEntity) {
        viewModelScope.launch {
            eventRepository.setFavEvent(e, true)
        }
    }

    fun deleteFavEvent(e: EventEntity) {
        viewModelScope.launch {
            eventRepository.setFavEvent(e, false)
        }
    }
}
