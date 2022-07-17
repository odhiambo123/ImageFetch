package com.davidodhiambo.imagefetch.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.davidodhiambo.imagefetch.api.PhotoApi
import com.davidodhiambo.imagefetch.ui.gallery.GalleryViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepository @Inject constructor(private val photApi: PhotoApi) {
    fun searchPhotos(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            )
        ) {
            PhotosPagingSource(photApi, query)
        }.liveData
}