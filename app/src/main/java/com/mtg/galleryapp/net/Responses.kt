package com.mtg.galleryapp.net

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class AuthorizationResponse(@SerializedName("auth") val auth: Boolean,
                                 @SerializedName("token") val token: String)

data class ImagesResponse(@SerializedName("pictures") val pictures: MutableList<PicturesResponse>,
                          @SerializedName("page") val page: Int,
                          @SerializedName("pageCount") val pageCount: Int,
                          @SerializedName("hasMore") val hasMore: Boolean)

data class PicturesResponse(@SerializedName("id") val id: String,
                            @SerializedName("cropped_picture") val croppedPicture: String) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString() ?: "", parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(croppedPicture)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PicturesResponse> {
        override fun createFromParcel(parcel: Parcel): PicturesResponse {
            return PicturesResponse(parcel)
        }

        override fun newArray(size: Int): Array<PicturesResponse?> {
            return arrayOfNulls(size)
        }
    }
}

data class ImageDetailResponse(@SerializedName("id") val id: String,
                               @SerializedName("author") val author: String,
                               @SerializedName("camera") val camera: String,
                               @SerializedName("tags") val tags: String,
                               @SerializedName("cropped_picture") val croppedPicture: String,
                               @SerializedName("full_picture") val fullPicture: String)