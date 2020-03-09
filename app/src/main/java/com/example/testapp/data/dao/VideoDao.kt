package com.example.testapp.data.dao

import androidx.room.*
import com.example.testapp.data.entity.Video

@Dao
interface VideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(video: Video)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(vararg videoList: Video)

    @Query("SELECT * FROM video")
    fun getAll(): List<Video>

    @Query("SELECT * from video WHERE id = :videoId")
    fun getVideo(videoId: String) : Video?

    @Delete
    fun delete(video: Video)
}