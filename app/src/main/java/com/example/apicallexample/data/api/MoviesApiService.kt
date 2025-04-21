package com.example.apicallexample.data.api

import com.example.apicallexample.data.model.MovieDetailsResponse
import com.example.apicallexample.data.model.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Header("Authorization") token:String,
        @Query("page") page:Int
    ):Response<MoviesResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Header("Authorization") token:String,
        @Path("movie_id") movieId:Int
    ):Response<MovieDetailsResponse>
}