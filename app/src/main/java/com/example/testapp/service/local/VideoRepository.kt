package com.example.testapp.service.local

import android.content.Context
import com.example.testapp.data.AppDatabase
import com.example.testapp.service.remote.YoutubeApiService
import com.google.api.services.youtube.model.Video

object VideoRepository {
    fun insertVideoList(videoList: List<Video>, context: Context) {
        AppDatabase.getDatabase(context).videoDao().insertList(*videoList.map { it.toDataModel() }.toTypedArray())
    }

    fun getVideo(videoId: String, forceRefresh: Boolean, context: Context): com.example.testapp.data.entity.Video? {
        val videoDao = AppDatabase.getDatabase(context).videoDao()
        return if (forceRefresh || CacheService.isCacheExpired(videoId)) {
            val results = YoutubeApiService.getVideoList(listOf(videoId))
            val firstItem: Video? = results.firstOrNull()
            firstItem?.let {
                val videoDataModel = it.toDataModel()
                videoDao.insert(videoDataModel)
                CacheService.updateCacheValidity(videoId)
                videoDataModel
            }

            null
        } else {
            videoDao.getVideo(videoId)
        }
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