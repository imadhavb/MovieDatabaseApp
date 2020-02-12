package com.example.movies_appp.interfaces

interface MovieController {
    fun homeScreen()
    fun showMovie(idx: Int)
    fun deleteList(idx: Int)
    fun showMovieSearch(idx: Int)
    val movies: MovieRepository
}