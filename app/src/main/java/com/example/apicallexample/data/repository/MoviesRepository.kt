package com.example.apicallexample.data.repository

import com.example.apicallexample.data.api.MoviesApiService
import com.example.apicallexample.data.model.Movie
import com.example.apicallexample.data.model.MovieDetailsResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
    private val apiService: MoviesApiService
){
    suspend fun getPopularMovies(token:String, page:Int):List<Movie>{
        val response= apiService.getPopularMovies(token, page)
        return if (response.isSuccessful){
            response.body()?.results?: emptyList()
        }else{
            emptyList()
        }

    }

    suspend fun getMovieDetails(token: String, movieId:Int):MovieDetailsResponse?{
        val response = apiService.getMovieDetails(token, movieId)
        return if(response.isSuccessful){
            response.body()
        }else{
            null
        }

    }

}