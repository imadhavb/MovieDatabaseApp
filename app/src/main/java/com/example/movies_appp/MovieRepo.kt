package com.example.movies_appp

import com.example.movies_appp.interfaces.MovieRepository
import com.example.movies_appp.models.MovieClass
import com.example.movies_appp.models.RecyclerviewMovie

internal class MovieRepo : MovieRepository {

    private var movies: MutableList<RecyclerviewMovie> = mutableListOf()
    private var moviesList: MutableList<RecyclerviewMovie> = mutableListOf()
    private var searchList: MutableList<MovieClass> = mutableListOf()

    init {
        //val seed = MovieClass((R.drawable.terminatordf.toString()), "Terminator: Dark Fate", "Terminator", "Kills Things", "2020-02-02", "7.5", "en", "Terminator")
        //searchList.add(seed)
    }
    override fun addSearch(task: MovieClass) {
        searchList.add(task)
    }

    override fun getCountSearch(): Int {
        return searchList.size
    }

    override fun getSearch(idx: Int): MovieClass {
        return searchList[idx]
    }

    override fun getAllSearch(): List<MovieClass> {
        return searchList
    }
    override fun addAllSearch(tasks: List<MovieClass>): Boolean {
        return searchList.addAll(tasks)
    }

    override fun replaceSearch(idx: Int, task: MovieClass) {
        if (idx >= searchList.size) throw Exception("Outside of bounds")
        searchList[idx] = task
    }

    override fun refreshSearch(tasks: List<MovieClass>) {
        this.searchList.clear()
        this.searchList.addAll(tasks)
    }
    override fun removeEverything() {
        this.searchList.clear()
    }
    override fun addTask(task: RecyclerviewMovie) {
        movies.add(task)

    }
    override fun updateTask(idx: Int, task: RecyclerviewMovie) {
        movies[idx] = task
    }

    override fun getCount(): Int {
        return movies.size
    }

    override fun getTask(idx: Int): RecyclerviewMovie {
        return movies[idx]
    }

    override fun getAll(): List<RecyclerviewMovie> {
        return movies
    }

    override fun remove(task: RecyclerviewMovie) {
        movies.remove(task)
    }

    override fun replace(idx: Int, task: RecyclerviewMovie) {
        if (idx >= movies.size) throw Exception("Outside of bounds")
        movies[idx] = task
    }
    override fun refresh(tasks: List<RecyclerviewMovie>) {
        this.movies.clear()
        this.movies.addAll(tasks)
    }

    override fun addList(task: RecyclerviewMovie) {
        moviesList.add(task)

    }
    override fun updateList(idx: Int, task: RecyclerviewMovie) {
        moviesList[idx] = task
    }

    override fun getCountList(): Int {
        return moviesList.size
    }

    override fun getList(idx: Int): RecyclerviewMovie {
        return moviesList[idx]
    }

    override fun getAllList(): List<RecyclerviewMovie> {
        return moviesList
    }

    override fun removeList(task: RecyclerviewMovie) {
        moviesList.remove(task)
    }

    override fun replaceList(idx: Int, task: RecyclerviewMovie) {
        if (idx >= moviesList.size) throw Exception("Outside of bounds")
        moviesList[idx] = task
    }
    override fun refreshList(tasks: List<RecyclerviewMovie>) {
        this.moviesList.clear()
        this.moviesList.addAll(tasks)
    }

}