package com.example.testapp.domain

import android.content.Context
import com.example.testapp.data.entity.Playlist
import com.example.testapp.data.entity.Track
import com.example.testapp.domain.model.Session
import com.example.testapp.service.local.PlaylistRepository
import com.example.testapp.service.local.TrackRepository
import com.example.testapp.view.playlist.model.PlaylistModel
import com.example.testapp.view.playlistdetail.model.PlaylistTrack

class PlaylistBusinessLogic {

    fun fetchPlaylist(forceRefresh: Boolean, context: Context): List<PlaylistModel> {
        return PlaylistRepository.fetchPlaylist(forceRefresh, context, Session.accountName.orEmpty()).map { it.toViewModel() }
    }

    fun fetchPlaylistTracks(playlistId: String, forceRefresh: Boolean, context: Context): List<PlaylistTrack> {
        return TrackRepository.fetchTracks(playlistId, forceRefresh, context).map { it.toViewModel() }
    }

    private fun Playlist.toViewModel(): PlaylistModel {
        return PlaylistModel(
            id = this.id,
            title = this.title,
            thumbnail = this.thumbnail,
            itemCount = this.itemCount
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
