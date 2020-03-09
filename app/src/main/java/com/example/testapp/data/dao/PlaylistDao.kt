package com.example.testapp.data.dao

import androidx.room.*
import com.example.testapp.data.entity.Playlist

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: Playlist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(vararg playlist: Playlist)

    @Query("SELECT * FROM playlist WHERE accountName = :accountName")
    fun getAll(accountName: String): List<Playlist>

    @Delete
    fun deletePlaylist(playlist: Playlist)
}