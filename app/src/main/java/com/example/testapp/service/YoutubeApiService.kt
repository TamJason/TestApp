package com.example.testapp.service

import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.Playlist

object YoutubeApiService {
    var mCredential: GoogleAccountCredential? = null

    fun getPlaylist(): List<Playlist> {
        val transport = AndroidHttp.newCompatibleTransport()
        val jsonFactory = JacksonFactory.getDefaultInstance()
        val mService = YouTube.Builder(
            transport, jsonFactory, mCredential
        )
            .setApplicationName("YouTube Data API Android Quickstart")
            .build()
        val playlistItems = mutableListOf<Playlist>()
        buildRequest(playlistItems, mService, 10, "")
        return playlistItems
    }


    private fun buildRequest(
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
            buildRequest(playlistItems, service, maxResults, response.nextPageToken)
        }
    }

}