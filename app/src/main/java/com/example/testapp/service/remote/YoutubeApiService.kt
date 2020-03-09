package com.example.testapp.service.remote

import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.Playlist
import com.google.api.services.youtube.model.PlaylistItem
import com.google.api.services.youtube.model.Video

object YoutubeApiService {
    var mCredential: GoogleAccountCredential? = null
    private lateinit var mService: YouTube

    fun updateCredentials(credentials: GoogleAccountCredential) {
        mCredential = credentials
        val transport = AndroidHttp.newCompatibleTransport()
        val jsonFactory = JacksonFactory.getDefaultInstance()
        mService = YouTube.Builder(
            transport, jsonFactory,
            mCredential
        )
            .setApplicationName("YouTube Data API Android Quickstart")
            .build()
    }

    fun getPlaylistTracks(playlistId: String): List<Pair<PlaylistItem, Video?>> {
        val playlistItems = mutableListOf<Pair<PlaylistItem, Video?>>()
        buildPlaylistItemsRequest(
            playlistItems,
            playlistId,
            mService,
            10,
            ""
        )
        return playlistItems
    }

    fun getPlaylist(): List<Playlist> {
        val playlistItems = mutableListOf<Playlist>()
        buildPlaylistRequest(
            playlistItems,
            mService,
            10,
            ""
        )
        return playlistItems
    }

    fun getVideoList(playlistItems: List<PlaylistItem>): List<Video> {
        val request = mService.Videos().list("snippet, contentDetails")
        request.id = playlistItems.map { it.contentDetails.videoId }.joinToString()
        return request.execute().items
    }


    private fun buildPlaylistRequest(
        playlistItems: MutableList<Playlist>,
        service: YouTube,
        maxResults: Int,
        nextPageToken: String?
    ) {
        nextPageToken?.let {
            val request = service.Playlists().list("snippet,contentDetails")
            request.maxResults = maxResults.toLong()
            request.mine = true
            request.pageToken = it
            val response = request.execute()
            playlistItems.addAll(response.items)
            buildPlaylistRequest(
                playlistItems,
                service,
                maxResults,
                response.nextPageToken
            )
        }
    }


    private fun buildPlaylistItemsRequest(
        playlistItems: MutableList<Pair<PlaylistItem, Video?>>,
        playlistId: String,
        service: YouTube,
        maxResults: Int,
        nextPageToken: String?
    ) {
        nextPageToken?.let {
            val request = service.PlaylistItems().list("snippet,contentDetails")
            request.maxResults = maxResults.toLong()
            request.pageToken = it
            request.playlistId = playlistId
            val response = request.execute()
            val videoList = getVideoList(response.items)
            response.items.forEach { playlistItem ->
                playlistItems.add(
                    Pair(
                        playlistItem,
                        videoList.find { it.id == playlistItem.contentDetails.videoId })
                )
            }
            buildPlaylistItemsRequest(
                playlistItems,
                playlistId,
                service,
                maxResults,
                response.nextPageToken
            )
        }
    }

}