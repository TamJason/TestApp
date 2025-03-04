package com.example.testapp.view.playlist

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapp.domain.PlaylistBusinessLogic
import com.example.testapp.view.playlist.model.PlaylistModel
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class PlaylistViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private val playlistBusinessLogic = PlaylistBusinessLogic()
    private val job = Job()
    private val playlistModelMutableLiveData = MutableLiveData<List<PlaylistModel>>()
    val playlistModelLiveData: LiveData<List<PlaylistModel>> = playlistModelMutableLiveData

    fun fetchPlaylist(fragment: Fragment, requestCode: Int, forceRefresh: Boolean = false) {
        launch(Dispatchers.IO) {
            try {
                val result =  playlistBusinessLogic.fetchPlaylist(forceRefresh, getApplication())
                withContext(Dispatchers.Main) {
                    playlistModelMutableLiveData.value = result
                }
            } catch (e: UserRecoverableAuthIOException) {
                fragment.startActivityForResult(e.intent, requestCode)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    playlistModelMutableLiveData.value = listOf()
                }
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