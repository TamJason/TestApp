package com.example.testapp.view.playlistdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapp.R
import com.example.testapp.view.playlistdetail.model.PlaylistTrack
import kotlinx.android.synthetic.main.playlist_track_item.view.*

class PlaylistTrackItemAdapter :
    RecyclerView.Adapter<PlaylistTrackItemAdapter.PlaylistTrackItemViewHolder>() {
    var trackItems = listOf<PlaylistTrack>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistTrackItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_track_item, parent, false)
        return PlaylistTrackItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trackItems.size
    }

    override fun onBindViewHolder(holder: PlaylistTrackItemViewHolder, position: Int) {
        holder.trackTitle.text = trackItems[position].title
        holder.trackAuthor.text = trackItems[position].author
        holder.trackDuration.text = trackItems[position].duration.formatDuration()
        Glide.with(holder.itemView).load(trackItems[position].thumbnail).into(holder.trackThumbail)
    }

    inner class PlaylistTrackItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val trackTitle = view.trackTitle
        val trackThumbail = view.trackThumbnail
        val trackAuthor = view.trackAuthor
        val trackDuration = view.trackDuration
    }

    private fun String.formatDuration(): String {
        return this.replace("PT", "").replace("H",":").replace("M", ":").replace("S", "")
    }
}