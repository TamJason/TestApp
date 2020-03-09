package com.example.testapp.view.playlist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
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
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.playlist_fragment_title)
        playlistRecyclerView.adapter = playlistItemAdapter
        playlistSwipeView.setOnRefreshListener { playlistViewModel.fetchPlaylist(this, REQUEST_AUTHORIZATION, true) }
        playlistItemAdapter.onClick = {
            findNavController().navigate(PlaylistFragmentDirections.toPlaylistDetail(it))
        }
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
        if(playlists.isEmpty()) {
            playlistNoItem.visibility = View.VISIBLE
        }
        else {
            playlistNoItem.visibility = View.GONE
        }
    }

    companion object {
        private const val REQUEST_AUTHORIZATION = 1001
    }
}