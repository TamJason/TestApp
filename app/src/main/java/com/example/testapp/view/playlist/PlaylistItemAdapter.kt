package com.example.testapp.view.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.example.testapp.R
import com.example.testapp.view.playlist.model.PlaylistModel
import kotlinx.android.synthetic.main.playlist_item.view.*

class PlaylistItemAdapter : RecyclerView.Adapter<PlaylistItemAdapter.PlaylistItemViewHolder>() {
    var playlistItems = listOf<PlaylistModel>()
    var onClick: (playlistModel: PlaylistModel)-> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return PlaylistItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlistItems.size
    }

    override fun onBindViewHolder(holder: PlaylistItemViewHolder, position: Int) {
        holder.playlistTitle.text = playlistItems[position].title
        holder.playlistItemCount.text = holder.itemView.resources.getString(R.string.playlist_item_count, playlistItems[position].itemCount)
        holder.itemView.setOnClickListener{onClick.invoke(playlistItems[position])}
        Glide.with(holder.itemView).load(playlistItems[position].thumbnail).fitCenter().into(holder.playlistThumbnail)
    }

    inner class PlaylistItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playlistTitle = view.playlistItemTitle
        val playlistThumbnail = view.playlistItemThumbnail
        val playlistItemCount = view.playlistItemCount
    }
}