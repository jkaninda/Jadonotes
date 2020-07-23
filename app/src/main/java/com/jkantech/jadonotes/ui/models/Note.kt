package com.jkantech.jadonotes.ui.models

import android.os.Parcel
import android.os.Parcelable
/**
 * Created by Jonas Kaninda on 10/07/2020.
 */
class Note(
        var id:Int?=null,
        var title: String? = "",
        var text: String? ="",
        var category: String? = "",
        var editdate: String? = "",
        var createdate: String? = "",
        var color: String? = "#ffdddd",
        var text_size: Int=1,
        var isdeleted:Int?=null



):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readValue(Int::class.java.classLoader) as? Int) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeString(text)
        parcel.writeString(category)
        parcel.writeString(editdate)
        parcel.writeString(createdate)
        parcel.writeString(color)
        parcel.writeInt(text_size)
        parcel.writeValue(isdeleted)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }

        operator fun get(id: Int?): Int? {
            return id

        }
    }
}