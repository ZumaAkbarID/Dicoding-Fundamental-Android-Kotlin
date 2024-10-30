package com.rwa.submissionakhirdicodingevent.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rwa.submissionakhirdicodingevent.data.local.entity.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM event WHERE is_active = 1")
    fun getActiveEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event WHERE is_active = 0")
    fun getFinishedEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event WHERE is_fav = 1")
    fun getFavEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event WHERE id = :id")
    suspend fun getDetailEvent(id: Int): EventEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Query("UPDATE event SET is_fav = :isFav WHERE id = :id")
    suspend fun setFavEvent(id: Int, isFav: Boolean)

    @Query("SELECT EXISTS(SELECT * FROM event WHERE id = :id AND is_fav = 1)")
    suspend fun isEventFav(id: Int): Boolean

    @Query("SELECT EXISTS(SELECT is_active FROM event WHERE id = :id)")
    suspend fun isEventActive(id: Int): Boolean

    @Delete
    suspend fun deleteFavEvent(eventEntity: EventEntity)
}
