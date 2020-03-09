package com.example.testapp.service.local

import android.content.Context
import com.example.testapp.data.AppDatabase
import com.google.api.services.youtube.model.Video

object VideoRepository {
    fun insertVideoList(videoList: List<Video>, context: Context) {
        AppDatabase.getDatabase(context).videoDao().insertList(*videoList.map { it.toDataModel() }.toTypedArray())
    }

    private fun Video.toDataModel(): com.example.testapp.data.entity.Video {
        return com.example.testapp.data.entity.Video(
            id = this.id,
            duration = this.contentDetails.duration,
            title = this.snippet.title,
            channelTitle = this.snippet.channelTitle
        )
    }
}