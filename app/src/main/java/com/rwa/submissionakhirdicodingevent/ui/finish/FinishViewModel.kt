package com.rwa.submissionakhirdicodingevent.ui.finish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rwa.submissionakhirdicodingevent.data.EventRepository
import com.rwa.submissionakhirdicodingevent.data.local.entity.EventEntity
import kotlinx.coroutines.launch

class FinishViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Take a look back at our finished events"
    }
    val text: LiveData<String> = _text

    fun getFinishedEvent() = eventRepository.getFinishedEvent()

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