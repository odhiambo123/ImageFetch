package com.davidodhiambo.imagefetch.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val id: String,
    val description: String,
    val urls: PhotoUrls,
    val user: User,

    ) : Parcelable {

    @Parcelize
    data class PhotoUrls(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String,
    ) : Parcelable

    @Parcelize
    data class User(
        val name: String,
        val username: String,
    ) : Parcelable {
        val atttributionUrl get() = "https://unsplash.com/$username?utm_source=unsplash&utm_source=ImageFetch&utm_medium=referral"
    }

}