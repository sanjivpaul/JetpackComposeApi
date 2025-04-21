package com.example.apicallexample.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Movie (
    val id:Int,
    val title:String,
    val overview:String,
    val poster_path:String,
    val popularity: Int,
    val vote_average: Double,
    val vote_count: Int,
    val release_date: String
)