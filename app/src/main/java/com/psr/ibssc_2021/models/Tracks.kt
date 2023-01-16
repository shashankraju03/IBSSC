package com.psr.ibssc_2021.models

import android.net.wifi.aware.ParcelablePeerHandle
import android.os.Parcel
import android.os.Parcelable

data class Tracks (
    val link: String="",
    val dt: String="",
    val status: String=""




    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!


    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(link)
        parcel.writeString(dt)
        parcel.writeString(status)




    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}