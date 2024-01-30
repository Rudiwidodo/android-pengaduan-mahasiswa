package com.example.pengaduan.helper

import android.os.Parcel
import android.os.Parcelable

data class ProfileResultParcelable(
    var id: Int? = null,
    var nim: String? = null,
    var nama: String? = null,
    var email: String? = null,
    var foto: String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(nim)
        parcel.writeString(nama)
        parcel.writeString(email)
        parcel.writeString(foto)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfileResultParcelable> {
        override fun createFromParcel(parcel: Parcel): ProfileResultParcelable {
            return ProfileResultParcelable(parcel)
        }

        override fun newArray(size: Int): Array<ProfileResultParcelable?> {
            return arrayOfNulls(size)
        }
    }
}