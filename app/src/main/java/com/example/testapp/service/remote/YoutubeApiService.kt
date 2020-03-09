package com.example.testapp.service.remote

import com.example.testapp.config.AppConfiguration
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

    fun initService(credentials: GoogleAccountCredential) {
        mCredential = credentials
        val transport = AndroidHttp.newCompatibleTransport()
        val jsonFactory = JacksonFactory.getDefaultInstance()
        mService = YouTube.Builder(
            transport, jsonFactory,
            mCredential
        )
            .setApplicationName("Test App")
            .build()
    }

    fun getPlaylistTracks(playlistId: String): List<Pair<PlaylistItem, Video?>> {
        val playlistItems = mutableListOf<Pair<PlaylistItem, Video?>>()
        retrievePlaylistItems(
            playlistItems,
            playlistId,
            mService,
            AppConfiguration.MAX_API_RESULTS,
            ""
        )
        return playlistItems
    }

    fun getPlaylist(): List<Playlist> {
        val playlistItems = mutableListOf<Playlist>()
        retrievePlaylist(
            playlistItems,
            mService,
            AppConfiguration.MAX_API_RESULTS,
            ""
        )
        return playlistItems.filter { it.status.privacyStatus == "public" }
    }

    fun getVideoList(videoIdList: List<String>): List<Video> {
        val request = mService.Videos().list("snippet, contentDetails")
        request.id = videoIdList.joinToString()
        return request.execute().items
    }

    /**
     * Recursive method to fetch playlist  from Youtube service based on credentials.
     * Stop's when nextPageToken is null
     *
     * @param playlistItems List of playlist
     * @param service - youtube api service
     * @param maxResults - maximum number of items to request
     * @param nextPageToken - token for the next results
     */
    private fun retrievePlaylist(
        playlistItems: MutableList<Playlist>,
        service: YouTube,
        maxResults: Int,
        nextPageToken: String?
    ) {
        nextPageToken?.let {
            val request = service.Playlists().list("snippet,contentDetails, status")
            request.maxResults = maxResults.toLong()
            request.mine = true
            request.pageToken = it
            val response = request.execute()
            playlistItems.addAll(response.items)
            retrievePlaylist(
                playlistItems,
                service,
                maxResults,
                response.nextPageToken
            )
        }
    }

    /**
     * Recursive method to fetch playlist items and corresponding video from Youtube service.
     * Stop's when nextPageToken is null
     *
     * @param playlistItems List of results Pair of PlaylistItem and Video
     * @param playlistId - id of the playlist to fetch it's PlaylistItems
     * @param service - youtube api service
     * @param maxResults - maximum number of items to request
     * @param nextPageToken - token for the next results
     */
    private fun retrievePlaylistItems(
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
            val videoList = getVideoList(response.items.map { it.contentDetails.videoId })
            response.items.forEach { playlistItem ->
                playlistItems.add(
                    Pair(
                        playlistItem,
                        videoList.find { it.id == playlistItem.contentDetails.videoId })
                )
            }
            retrievePlaylistItems(
                playlistItems,
                playlistId,
                service,
                maxResults,
                response.nextPageToken
            )
        }
    }

}