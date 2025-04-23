package com.example.apicallexample.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetailsResponse(
    val id:Int,
    val title:String,
    val overview :String,
    val poster_path:String,
    val release_date:String,
    val runtime:String?, // if run time is null
    val vote_average:Float,
)
