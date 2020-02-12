package com.example.movies_appp

import android.content.Context
import com.example.movies_appp.interfaces.RecentRepository
import com.example.movies_appp.models.RecyclerviewMovie


class RecentRepo(ctx: Context): RecentRepository {

    private val MD: IDatabase

    init {
        MD = RecentDatabase(ctx)
    }

    override fun getCount(): Int {
        return MD.getMovies().size
    }

    override fun getTask(idx: Int): RecyclerviewMovie? {
        return MD.getMovie(idx)
    }

    override fun getAll(): List<RecyclerviewMovie> {
        return MD.getMovies()
    }


    override fun addTask(task: RecyclerviewMovie) {
        MD.addMovie(task)
    }

    override fun getCountList(): Int {
        return MD.getMoviesList().size
    }

    override fun getList(idx: Int): RecyclerviewMovie? {
        return MD.getMovieList(idx)
    }

    override fun getAllList(): List<RecyclerviewMovie> {
        return MD.getMoviesList()
    }


    override fun addTaskList(task: RecyclerviewMovie) {
        MD.addMovieList(task)
    }

    override fun deleteList(task: RecyclerviewMovie) {
        MD.deleteList(task)
    }




}