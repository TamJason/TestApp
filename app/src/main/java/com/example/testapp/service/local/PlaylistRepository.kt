package com.example.testapp.service.local

import android.content.Context
import com.example.testapp.data.AppDatabase
import com.example.testapp.data.entity.Playlist
import com.example.testapp.service.remote.YoutubeApiService

// TODO use dependency injection
object PlaylistRepository {

    fun fetchPlaylist(forceRefresh: Boolean = false, context: Context, accountName: String): List<Playlist> {
        val playlistDao = AppDatabase.getDatabase(context).playlistDao()
        return if (forceRefresh || CacheService.isCacheExpired(accountName)) {
            val results = YoutubeApiService.getPlaylist().map { it.toDataModel(accountName) }
            playlistDao.insertList(*results.toTypedArray())
            CacheService.updateCacheValidity(accountName)
            results
        } else {
            playlistDao.getAll(accountName)
        }
    }

    private fun com.google.api.services.youtube.model.Playlist.toDataModel(accountName: String): Playlist {
        return Playlist(
            id = this.id,
            thumbnail = this.snippet.thumbnails.standard.url,
            title = this.snippet.title,
            itemCount = this.contentDetails.itemCount.toInt(),
            accountName = accountName
        )
    }


}