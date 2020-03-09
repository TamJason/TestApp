package com.example.testapp.view.playlistdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapp.domain.PlaylistBusinessLogic
import com.example.testapp.view.playlistdetail.model.PlaylistTrack
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PlaylistDetailViewModel(application: Application): AndroidViewModel(application), CoroutineScope {
    private val playlistBusinessLogic = PlaylistBusinessLogic()
    private val job = Job()

    private val playlistTrackMutableLiveData = MutableLiveData<List<PlaylistTrack>>()
    val playlistTrackLiveData: LiveData<List<PlaylistTrack>> = playlistTrackMutableLiveData

    fun fetchPlaylistTracks(playlistId: String, forceRefresh: Boolean = false) {
        launch(Dispatchers.IO) {
            val results = playlistBusinessLogic.fetchPlaylistTracks(playlistId, forceRefresh, getApplication())
            withContext(Dispatchers.Main) {
                playlistTrackMutableLiveData.value = results
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}