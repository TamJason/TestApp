package com.example.testapp.service.local

import android.content.Context
import com.example.testapp.data.AppDatabase
import com.example.testapp.data.entity.Track
import com.example.testapp.service.remote.YoutubeApiService
import com.google.api.services.youtube.model.PlaylistItem

object TrackRepository {

    fun fetchTracks(playlistId: String, forceRefresh: Boolean = false, context: Context): List<Track> {
        val trackDao = AppDatabase.getDatabase(context).trackDao()
        return if (forceRefresh || CacheService.isCacheExpired(playlistId)) {
            val results = YoutubeApiService.getPlaylistTracks(playlistId).map { it.toDataModel(playlistId) }
            trackDao.insertList(*results.toTypedArray())
            CacheService.updateCacheValidity(playlistId)
            results
        } else {
            trackDao.getAll(playlistId)
        }
    }

    private fun PlaylistItem.toDataModel(playlistId: String) : Track {
        return Track(
            id = this.id,
            title = this.snippet.title,
            thumbnail =  this.snippet.thumbnails.high.url,
            author = this.snippet.channelTitle,
            duration = "", //TODO
            playlistId = playlistId
        )
    }
}