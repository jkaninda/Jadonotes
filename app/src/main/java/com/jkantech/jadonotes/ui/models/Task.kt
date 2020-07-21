package com.jkantech.jadonotes.ui.models

import android.os.Parcel
import android.os.Parcelable

class Task (
    var id:Int?=null,
    var taskdetails:String?=null,
    var notifytime:Long?=null,
    val tastcompleted:Boolean=false
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(taskdetails)
        parcel.writeValue(notifytime)
        parcel.writeByte(if (tastcompleted) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}