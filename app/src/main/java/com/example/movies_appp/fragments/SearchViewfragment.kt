package com.example.movies_appp.fragments


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies_appp.MainActivity
import com.example.movies_appp.MovieDatabase
import com.example.movies_appp.MovieRepo
import com.example.movies_appp.R
import com.example.movies_appp.interfaces.MovieController
import com.example.movies_appp.interfaces.MovieRepository
import com.example.movies_appp.models.MovieClass
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_search_view.*
import okhttp3.*
import java.io.IOException
import java.lang.Exception
import com.example.movies_appp.adapters.SearchviewAdapter


/**
 * A simple [Fragment] subclass.
 */
class SearchViewfragment : Fragment(), MovieController {
    override fun deleteList(idx: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun homeScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMovie(idx: Int) {
    }

    override fun showMovieSearch(idx: Int) {
        val current = movies.getSearch(idx)
        val intent = Intent(activity, MovieDatabase::class.java)
        intent.putExtra("overview", current.overview)
        intent.putExtra("release_date", current.release_date)
        intent.putExtra("poster_path", current.poster_path)
        intent.putExtra("vote_average", current.vote_average)
        intent.putExtra("title", current.title)
        intent.putExtra("back_drop", current.backdrop_path)
        intent.putExtra("original_language", current.original_lang)
        intent.putExtra("original_title", current.original_title)
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override lateinit var movies: MovieRepository
    lateinit var handler: Handler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_view, container, false)
        movies = MovieRepo()

        return view
    }


    //private val client = OkHttpClient()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        movies = MovieRepo()

        handler = Handler()
        val searchAdapter = SearchviewAdapter(this)
        searchRecyclerview.adapter = searchAdapter
        searchRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        try {
            val url = arguments!!.getString("movie")
            val gson = GsonBuilder().create()
            val info = gson.fromJson(url, MainActivity.Information::class.java)
            if (info.results.isNotEmpty()) {
                var tempSearch: MutableList<MovieClass> = mutableListOf()
                loop@ for (i in 0 until info.results.size) {
                    if (i == 9) {
                        break@loop
                    } else {
                        val description = info.results[i].overview

                        val releaseDate = info.results[i].release_date

                        val posterPath = "https://image.tmdb.org/t/p/w500/" + info.results[i].poster_path

                        val voteAverage = info.results[i].vote_average

                        val mvTitle = info.results[i].title

                        val lang = info.results[i].original_language

                        val ogTitle = info.results[i].original_title

                        val backDrop = "https://image.tmdb.org/t/p/w500/" + info.results[i].backdrop_path
                        var movie = MovieClass(
                            posterPath,
                            backDrop,
                            mvTitle,
                            description,
                            releaseDate,
                            voteAverage,
                            lang,
                            ogTitle
                        )
                        tempSearch.add(movie)
                    }

                }
                movies.addAllSearch(tempSearch)
                handler.post {
                    searchRecyclerview.adapter?.notifyDataSetChanged() //updates recyclerview
                }
                Log.d("check35", movies.getAllSearch().toString())


            }

        }catch (ex: Exception) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show()
        }

    }

}