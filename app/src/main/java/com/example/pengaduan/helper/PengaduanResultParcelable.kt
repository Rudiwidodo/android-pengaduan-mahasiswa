package com.example.pengaduan.helper

import android.os.Parcel
import android.os.Parcelable


data class PengaduanResultParcelable(
    var id: Int? = null,
    var nim: String? = null,
    var isiPengaduan: String? = null,
    var foto: String? = null,
    var isActive: Int? = null,
    var isArchive: Int? = null,
    var userId: Int? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(nim)
        parcel.writeString(isiPengaduan)
        parcel.writeString(foto)
        parcel.writeValue(isActive)
        parcel.writeValue(isArchive)
        parcel.writeValue(userId)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PengaduanResultParcelable> {
        override fun createFromParcel(parcel: Parcel): PengaduanResultParcelable {
            return PengaduanResultParcelable(parcel)
        }

        override fun newArray(size: Int): Array<PengaduanResultParcelable?> {
            return arrayOfNulls(size)
        }
    }
}
