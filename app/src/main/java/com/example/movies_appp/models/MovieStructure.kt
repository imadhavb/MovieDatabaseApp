package com.example.movies_appp.models

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class MovieClass  (
    val poster_path: String,
    val backdrop_path: String,
    val title: String,
    val overview: String,
    val release_date: String,
    val vote_average: String,
    val original_lang: String,
    val original_title: String
)
data class RecyclerviewMovie(
    val id: Int,
    val poster_path: String,
    val backdrop_path: String,
    val title: String,
    val overview: String,
    val release_date: String,
    val vote_average: String,
    val original_lang: String,
    val original_title: String
)

