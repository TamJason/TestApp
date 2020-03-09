package com.example.testapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Playlist(
    @PrimaryKey val id: String,
    val thumbnail: String,
    val title: String,
    val itemCount: Int,
    val accountName: String
)