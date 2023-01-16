package com.psr.ibssc_2021.models

import android.net.wifi.aware.ParcelablePeerHandle
import android.os.Parcel
import android.os.Parcelable

data class Announ (
    val data: String=""





) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!


    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(data)




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