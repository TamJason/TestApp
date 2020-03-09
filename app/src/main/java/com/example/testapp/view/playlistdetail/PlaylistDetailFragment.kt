package com.example.testapp.view.playlistdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.testapp.R
import com.example.testapp.view.playlistdetail.model.PlaylistTrack
import kotlinx.android.synthetic.main.fragment_playlist.*
import kotlinx.android.synthetic.main.fragment_playlist_detail.*
import kotlinx.android.synthetic.main.playlist_item.*

class PlaylistDetailFragment : Fragment() {
    private val playlistDetailViewModel: PlaylistDetailViewModel by viewModels()
    private val trackAdapter = PlaylistTrackItemAdapter()
    private val args: PlaylistDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playlistDetailViewModel.playlistTrackLiveData.observe(this, Observer { updateTrackList(it) })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_playlist_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistDetailTrackRecyclerView.adapter = trackAdapter
        plalistDetailTitle.text = args.playlist.title
        playlistDetailItemCount.text = args.playlist.itemCount.toString()
        Glide.with(this).load(args.playlist.thumbnail).into(playlistDetailImage)
        playlistDetailViewModel.fetchPlaylistTracks(args.playlist.id)
    }

    private fun updateTrackList(trackList: List<PlaylistTrack>) {
        trackAdapter.trackItems = trackList
        trackAdapter.notifyDataSetChanged()
    }
}