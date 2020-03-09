package com.example.testapp.view.playlistdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapp.domain.PlaylistBusinessLogic
import com.example.testapp.view.playlistdetail.model.PlaylistTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class PlaylistDetailViewModel(application: Application): AndroidViewModel(application), CoroutineScope {
    private val playlistBusinessLogic = PlaylistBusinessLogic()
    private val playlistTrackMutableLiveData = MutableLiveData<List<PlaylistTrack>>()
    val playlistTrackLiveData: LiveData<List<PlaylistTrack>> = playlistTrackMutableLiveData


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    fun fetchPlaylistTracks(playlistId: String, forceRefresh: Boolean = false) {
        launch(Dispatchers.IO) {
            val results = playlistBusinessLogic.fetchPlaylistTracks(playlistId, forceRefresh, getApplication())
            withContext(Dispatchers.Main) {
                playlistTrackMutableLiveData.value = results
            }
        }
    }
}