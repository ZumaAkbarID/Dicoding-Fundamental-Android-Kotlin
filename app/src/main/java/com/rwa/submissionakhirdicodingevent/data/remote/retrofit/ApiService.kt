package com.rwa.submissionakhirdicodingevent.data.remote.retrofit

import com.rwa.submissionakhirdicodingevent.data.remote.response.DetailEventResponse
import com.rwa.submissionakhirdicodingevent.data.remote.response.EventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    suspend fun getActiveEvent(
        @Query("active") active: Int = 1,
    ): EventResponse

    @GET("events")
    suspend fun getFinishedEvent(
        @Query("active") active: Int = 0,
    ): EventResponse

    @GET("events/{id}")
    suspend fun getDetailEvents(
        @Path("id") eventId: Int
    ): DetailEventResponse

    @GET("events")
    suspend fun getFirstActiveEvent(
        @Query("active") active: Int = -1,
        @Query("limit") limit: Int = 1,
    ): EventResponse
}