package com.example.movies_appp.interfaces

import com.example.movies_appp.models.MovieClass
import com.example.movies_appp.models.RecyclerviewMovie

interface MovieRepository {
    fun getCount(): Int
    fun getTask(idx: Int): RecyclerviewMovie
    fun getAll(): List<RecyclerviewMovie>
    fun addTask(task: RecyclerviewMovie)
    fun updateTask(idx: Int, task: RecyclerviewMovie)
    fun remove(task: RecyclerviewMovie)
    fun replace(idx: Int, task: RecyclerviewMovie)
    fun refresh(tasks: List<RecyclerviewMovie>)

    fun addList(task: RecyclerviewMovie)
    fun updateList(idx: Int, task: RecyclerviewMovie)
    fun getCountList(): Int
    fun getList(idx: Int): RecyclerviewMovie
    fun getAllList(): List<RecyclerviewMovie>
    fun removeList(task: RecyclerviewMovie)
    fun replaceList(idx: Int, task: RecyclerviewMovie)
    fun refreshList(tasks: List<RecyclerviewMovie>)

    fun addSearch(task: MovieClass)
    fun getCountSearch(): Int
    fun getSearch(idx: Int): MovieClass
    fun getAllSearch(): List<MovieClass>
    fun replaceSearch(idx: Int, task: MovieClass)
    fun refreshSearch(tasks: List<MovieClass>)
    fun addAllSearch(tasks: List<MovieClass>): Boolean
    fun removeEverything()

}


interface RecentRepository {
    fun getCount(): Int
    fun getTask(idx: Int): RecyclerviewMovie?
    fun getAll(): List<RecyclerviewMovie>
    fun addTask(task: RecyclerviewMovie)
    fun getList(idx: Int): RecyclerviewMovie?
    fun getAllList(): List<RecyclerviewMovie>
    fun addTaskList(task: RecyclerviewMovie)
    fun getCountList(): Int
    fun deleteList(task: RecyclerviewMovie)
}