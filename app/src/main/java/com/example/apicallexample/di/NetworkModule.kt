package com.example.apicallexample.di

import com.example.apicallexample.data.api.MoviesApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

//    @Provides
//    @Singleton
//    fun provideRetrofit():Retrofit{
//        return Retrofit.Builder()
////            .baseUrl("https://api.themoviedb.org/3/")
//            .baseUrl("http://192.168.1.14:3000/api/")
//            .addConverterFactory(MoshiConverterFactory.create())
//            .build()
//    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory()) // Add this
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi): Retrofit { // Inject Moshi here
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.14:3000/api/")
            .addConverterFactory(MoshiConverterFactory.create(moshi)) // Use the Moshi instance
            .build()
    }

    @Provides
    @Singleton
    fun provideMoviesApiService(retrofit: Retrofit):MoviesApiService{
        return retrofit.create(MoviesApiService::class.java)
    }
}