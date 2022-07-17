package com.davidodhiambo.imagefetch.api

import android.provider.ContactsContract
import com.davidodhiambo.imagefetch.data.Photo

data class FetchResponse(
    val results: List<Photo>
    )