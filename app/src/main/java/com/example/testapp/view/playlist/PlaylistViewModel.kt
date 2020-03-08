package com.example.testapp.view.playlist

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapp.domain.PlaylistBusinessLogic
import com.example.testapp.view.playlist.model.PlaylistModel
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class PlaylistViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private val playlistBusinessLogic = PlaylistBusinessLogic()
    private val playlistModelMutableLiveData = MutableLiveData<List<PlaylistModel>>()
    val playlistModelLiveData: LiveData<List<PlaylistModel>> = playlistModelMutableLiveData

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    fun fetchPlaylist(fragment: Fragment, requestCode: Int) {
        launch {
            try {
                val result =  playlistBusinessLogic.fetchPlaylist()
                withContext(Dispatchers.Main) {
                    playlistModelMutableLiveData.value = result
                }
            } catch (e: UserRecoverableAuthIOException) {
                fragment.startActivityForResult(e.intent, requestCode)
            }
        }

    }
}