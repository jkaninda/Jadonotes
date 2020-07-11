package com.jkantech.jadonotes.ui.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class Note(

        var title: String? = "",
        var text: String? = "",
       // var category: String?="",
        var editdate: String?="",
        var createdate: String?="",
        var color: String?="#ffdddd",
        var filename: String? = "") : Parcelable, Serializable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())



    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(text)
        parcel.writeString(editdate)
        parcel.writeString(createdate)
        parcel.writeString(color)
        parcel.writeString(filename)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        private val serialVersionUid: Long = 4242424242
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}

