package com.example.testapp.view.playlist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.testapp.R
import com.example.testapp.view.playlist.model.PlaylistModel
import kotlinx.android.synthetic.main.fragment_playlist.*

class PlaylistFragment : Fragment() {

    private val playlistViewModel: PlaylistViewModel by viewModels()
    private val playlistItemAdapter = PlaylistItemAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playlistViewModel.playlistModelLiveData.observe(this, Observer { updatePlayList(it) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistRecyclerView.adapter = playlistItemAdapter
        playlistSwipeView.setOnRefreshListener { playlistViewModel.fetchPlaylist(this, REQUEST_AUTHORIZATION) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    override fun onResume() {
        super.onResume()
        playlistViewModel.fetchPlaylist(this, REQUEST_AUTHORIZATION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == REQUEST_AUTHORIZATION && resultCode == Activity.RESULT_OK) {
            playlistViewModel.fetchPlaylist(this, REQUEST_AUTHORIZATION)
        }
    }

    private fun updatePlayList(playlists: List<PlaylistModel>) {
        playlistItemAdapter.playlistItems = playlists
        playlistItemAdapter.notifyDataSetChanged()
        playlistSwipeView.isRefreshing = false
    }

    companion object {
        private const val REQUEST_AUTHORIZATION = 1001
    }
}