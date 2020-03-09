package com.example.testapp.view.playlist.model

import android.os.Parcel
import android.os.Parcelable

data class PlaylistModel(
    val id: String,
    val thumbnail: String,
    val title: String,
    val itemCount: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(id)
        parcel.writeString(thumbnail)
        parcel.writeString(title)
        parcel.writeInt(itemCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlaylistModel> {
        override fun createFromParcel(parcel: Parcel): PlaylistModel {
            return PlaylistModel(parcel)
        }

        override fun newArray(size: Int): Array<PlaylistModel?> {
            return arrayOfNulls(size)
        }
    }
}