package com.example.testapp.service.local

import android.content.Context
import com.example.testapp.data.AppDatabase
import com.example.testapp.data.entity.Track
import com.example.testapp.service.remote.YoutubeApiService
import com.google.api.services.youtube.model.PlaylistItem
import com.google.api.services.youtube.model.Video

// TODO use dependency injection
object TrackRepository {

    fun fetchTracks(playlistId: String, forceRefresh: Boolean = false, context: Context): List<Track> {
        val trackDao = AppDatabase.getDatabase(context).trackDao()
        return if (forceRefresh || CacheService.isCacheExpired(playlistId)) {
            val results = YoutubeApiService.getPlaylistTracks(playlistId)
            val trackList = results.map { it.toDataModel(playlistId) }
            trackDao.insertList(*trackList.toTypedArray())
            VideoRepository.insertVideoList(results.mapNotNull { it.second }, context)
            CacheService.updateCacheValidity(playlistId)
            trackList
        } else {
            trackDao.getAll(playlistId)
        }
    }

    private fun Pair<PlaylistItem, Video?>.toDataModel(playlistId: String) : Track {
        return Track(
            id = this.first.id,
            title = this.first.snippet.title,
            thumbnail =  this.first.snippet.thumbnails.high.url,
            author = this.second?.snippet?.channelTitle.orEmpty(),
            duration = this.second?.contentDetails?.duration.orEmpty(),
            playlistId = playlistId,
            videoId = this.second?.id.orEmpty()
        )
    }
}