package com.example.testapp.domain

import com.example.testapp.service.YoutubeApiService
import com.example.testapp.view.playlist.model.PlaylistModel
import com.google.api.services.youtube.model.Playlist

class PlaylistBusinessLogic {

    // TODO add repository for caching
    fun fetchPlaylist(): List<PlaylistModel> {
        return YoutubeApiService.getPlaylist().map { it.toPlayListModel() }
    }


    private fun Playlist.toPlayListModel(): PlaylistModel {
        return PlaylistModel(
            title = this.snippet.title,
            thumbnail = this.snippet.thumbnails.high.url,
            itemCount = this.contentDetails.itemCount.toInt()
        )
    }
}
