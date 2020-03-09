package com.example.testapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Track(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val duration: String,
    val thumbnail: String,
    val playlistId: String
)