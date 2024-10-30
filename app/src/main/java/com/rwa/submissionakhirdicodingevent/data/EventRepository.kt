package com.rwa.submissionakhirdicodingevent.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import com.rwa.submissionakhirdicodingevent.data.local.entity.EventEntity
import com.rwa.submissionakhirdicodingevent.data.local.room.EventDao
import com.rwa.submissionakhirdicodingevent.data.remote.retrofit.ApiService

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
) {
    // Load Active Events
    fun getActiveEvent(): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getActiveEvent()
            val events = response.listEvents
            val eventsList = events.map { e ->
                EventEntity(
                    id = e?.id ?: -1,
                    name = e?.name ?: "",
                    summary = e?.summary ?: "",
                    description = e?.description ?: "",
                    imageLogo = e?.imageLogo ?: "",
                    mediaCover = e?.mediaCover ?: "",
                    category = e?.category ?: "",
                    ownerName = e?.ownerName ?: "",
                    cityName = e?.cityName ?: "",
                    quota = e?.quota ?: 0,
                    registrants = e?.registrants ?: 0,
                    beginTime = e?.beginTime ?: "",
                    endTime = e?.endTime ?: "",
                    link = e?.link ?: "",
                    isActive = true,
                    isFav = eventDao.isEventFav(e?.id ?: -1)
                )
            }
            emit(Result.Success(eventsList))
        } catch (err: Exception) {
            emit(Result.Error(err.message.toString()))
        }
    }

    // Load Finished Events
    fun getFinishedEvent(): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFinishedEvent()
            val events = response.listEvents
            val eventsList = events.map { e ->
                EventEntity(
                    id = e?.id ?: -1,
                    name = e?.name ?: "",
                    summary = e?.summary ?: "",
                    description = e?.description ?: "",
                    imageLogo = e?.imageLogo ?: "",
                    mediaCover = e?.mediaCover ?: "",
                    category = e?.category ?: "",
                    ownerName = e?.ownerName ?: "",
                    cityName = e?.cityName ?: "",
                    quota = e?.quota ?: 0,
                    registrants = e?.registrants ?: 0,
                    beginTime = e?.beginTime ?: "",
                    endTime = e?.endTime ?: "",
                    link = e?.link ?: "",
                    isActive = false,
                    isFav = eventDao.isEventFav(e?.id ?: -1)
                )
            }
            emit(Result.Success(eventsList))
        } catch (err: Exception) {
            emit(Result.Error(err.message.toString()))
        }
    }

    // Load Fav Event
    fun getFavEvent(): LiveData<Result<List<EventEntity>>> {
        val result = MediatorLiveData<Result<List<EventEntity>>>()
        result.value = Result.Loading
        val source = eventDao.getFavEvents()

        result.addSource(source) { data ->
            result.value = Result.Success(data)
        }

        return result
    }

    // Set Fav Event
    suspend fun setFavEvent(e: EventEntity, favState: Boolean) {
        e.isFav = favState

        if (favState) {
            eventDao.insertEvents(listOf(e))
        } else {
            eventDao.deleteFavEvent(e)
        }
    }

    // Load DetailEvent
    fun showDetailEvent(eventId: Int): LiveData<Result<EventEntity>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailEvents(eventId)
            val event = response.event
            val eventData = EventEntity(
                id = event?.id ?: -1,
                name = event?.name ?: "",
                summary = event?.summary ?: "",
                description = event?.description ?: "",
                imageLogo = event?.imageLogo ?: "",
                mediaCover = event?.mediaCover ?: "",
                category = event?.category ?: "",
                ownerName = event?.ownerName ?: "",
                cityName = event?.cityName ?: "",
                quota = event?.quota ?: 0,
                registrants = event?.registrants ?: 0,
                beginTime = event?.beginTime ?: "",
                endTime = event?.endTime ?: "",
                link = event?.link ?: "",
                isActive = eventDao.isEventActive(event?.id ?: -1),
                isFav = eventDao.isEventFav(event?.id ?: -1)
            )

            emit(Result.Success(eventData))
        } catch (err: Exception) {
            emit(Result.Error(err.message.toString()))
        }
    }

    suspend fun getFirstActiveEvent(): Pair<String, String> {
        return try {
            val response = apiService.getFirstActiveEvent()
            val events = response.listEvents

            if (events.isNotEmpty()) {
                val event = events.first()
                val eventEntity = EventEntity(
                    id = event?.id ?: -1,
                    name = event?.name ?: "",
                    summary = event?.summary ?: "",
                    description = event?.description ?: "",
                    imageLogo = event?.imageLogo ?: "",
                    mediaCover = event?.mediaCover ?: "",
                    category = event?.category ?: "",
                    ownerName = event?.ownerName ?: "",
                    cityName = event?.cityName ?: "",
                    quota = event?.quota ?: 0,
                    registrants = event?.registrants ?: 0,
                    beginTime = event?.beginTime ?: "",
                    endTime = event?.endTime ?: "",
                    link = event?.link ?: "",
                    isActive = true,
                    isFav = false
                )

                eventEntity.name to eventEntity.summary
            } else {
                "" to ""
            }
        } catch (e: Exception) {
            "" to ""
        }
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao)
            }.also { instance = it }
    }
}