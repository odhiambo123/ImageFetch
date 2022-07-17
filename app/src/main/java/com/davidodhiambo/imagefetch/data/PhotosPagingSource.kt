package com.davidodhiambo.imagefetch.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.davidodhiambo.imagefetch.api.PhotoApi
import retrofit2.HttpException
import java.io.IOException

private const val PHOTOS_STARTING_PAGE_INDEX = 1

class PhotosPagingSource(
    private val photoApi: PhotoApi,
    private val query: String
): PagingSource<Int, Photo>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {

        val position = params.key ?: PHOTOS_STARTING_PAGE_INDEX
        return try {
            val response = photoApi.searchPhotos(query, position, params.loadSize)
            val photos = response.results
            LoadResult.Page(
                data = photos,
                prevKey = if (position == PHOTOS_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {

            LoadResult.Error(e)
        }catch (e: HttpException) {

            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int {
        val position = state.anchorPosition ?: PHOTOS_STARTING_PAGE_INDEX
        return position - 1
    }

}