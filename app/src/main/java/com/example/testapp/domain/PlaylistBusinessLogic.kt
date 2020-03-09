package com.example.testapp.domain

import android.content.Context
import com.example.testapp.data.entity.Playlist
import com.example.testapp.data.entity.Track
import com.example.testapp.domain.model.Session
import com.example.testapp.service.local.PlaylistRepository
import com.example.testapp.service.local.TrackRepository
import com.example.testapp.view.playlist.model.PlaylistModel
import com.example.testapp.view.playlistdetail.model.PlaylistTrack
import com.google.api.services.youtube.model.PlaylistItem

class PlaylistBusinessLogic {

    fun fetchPlaylist(forceRefresh: Boolean, context: Context): List<PlaylistModel> {
        return PlaylistRepository.fetchPlaylist(forceRefresh, context, Session.accountName.orEmpty()).map { it.toPlayListModel() }
    }

    fun fetchPlaylistTracks(playlistId: String, forceRefresh: Boolean, context: Context): List<PlaylistTrack> {
        return TrackRepository.fetchTracks(playlistId, false, context).map { it.toViewModel() }
    }

    private fun Playlist.toPlayListModel(): PlaylistModel {
        return PlaylistModel(
            id = this.id,
            title = this.title,
            thumbnail = this.thumbnail,
            itemCount = this.itemCount
        )
    }

    private fun PlaylistItem.toPlaylistTrack(): PlaylistTrack {
        return PlaylistTrack(
            id = this.id,
            title = this.snippet.title,
            author = this.snippet.channelTitle,
            thumbnail = this.snippet.thumbnails.high.url,
            duration = ""
        )
    }

    private fun Track.toViewModel(): PlaylistTrack {
        return PlaylistTrack(
            id = this.id,
            author = this.author,
            thumbnail = this.thumbnail,
            title = this.title,
            duration = this.duration
        )
    }
}
