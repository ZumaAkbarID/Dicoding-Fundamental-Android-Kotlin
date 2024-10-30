package com.rwa.submissionakhirdicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rwa.submissionakhirdicodingevent.data.EventRepository
import com.rwa.submissionakhirdicodingevent.data.Result
import com.rwa.submissionakhirdicodingevent.data.local.entity.EventEntity
import kotlinx.coroutines.launch

class DetailViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showError = MutableLiveData<Boolean>().apply {
        value = false
    }
    val showError: LiveData<Boolean> = _showError

    fun resetError() {
        _showError.value = false
    }

    private val _event = MutableLiveData<EventEntity?>()
    val event: LiveData<EventEntity?> get() = _event

    fun showDetailEvent(eventId: Int) {
        _isLoading.value = true
        val data = eventRepository.showDetailEvent(eventId)

        data.observeForever { result ->
            _isLoading.value = false
            if (result is Result.Success) {
                _event.value = result.data
            } else if (result is Result.Error) {
                _showError.value = true
            }
        }
    }

    fun clearEventData() {
        _event.value = null
    }

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