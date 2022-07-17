package com.davidodhiambo.imagefetch.di

import com.davidodhiambo.imagefetch.api.PhotoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module // Add this annotation to indicate that this class is a Dagger module.
 // The @InstallIn annotation is used to specify the Component class to install this module in.
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides // Add this annotation to provide objects to the dependencies graph.
    @Singleton // Add this annotation to indicate that this object should be created once and reused.
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(PhotoApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    @Provides
    @Singleton
    fun providePhotoApi(retrofit: Retrofit): PhotoApi =
        retrofit.create(PhotoApi::class.java)

    }
//annotation used tell dagger what to provide