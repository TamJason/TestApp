package com.example.testapp.data.dao

import androidx.room.*
import com.example.testapp.data.entity.Playlist
import com.example.testapp.data.entity.Track

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(track: Track)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(vararg trackList: Track)

    @Query("SELECT * FROM track WHERE playlistId = :playlistId")
    fun getAll(playlistId: String): List<Track>

    @Delete
    fun delete(track: Track)
}