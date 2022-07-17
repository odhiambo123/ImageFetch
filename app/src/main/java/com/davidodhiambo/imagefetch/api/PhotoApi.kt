package com.davidodhiambo.imagefetch.api

import com.davidodhiambo.imagefetch.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface PhotoApi {
    companion object{
        const val BASE_URL = "https://api.unsplash.com/"  // Base URL for the API https://unsplash.com/documentation#search-photos
        const val CLIENT_ID = BuildConfig.UNSPLASH_ACCESS_KEY
    }
    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("search/photos")//https://unsplash.com/documentation#search-photos
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): FetchResponse
}