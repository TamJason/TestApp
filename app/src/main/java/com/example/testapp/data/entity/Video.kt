package com.example.testapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Video(
    @PrimaryKey val id: String,
    val duration: String,
    val title: String,
    val channelTitle: String
)