package com.example.movies_appp.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies_appp.*
import com.example.movies_appp.MovieRepo
import com.example.movies_appp.adapters.RecentListAdapter
import com.example.movies_appp.interfaces.MovieController
import com.example.movies_appp.interfaces.MovieRepository
import kotlinx.android.synthetic.main.fragment_recent_list.*

/**
 * A simple [Fragment] subclass.
 */
class FragmentRecentList : Fragment(), MovieController {
    override fun homeScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMovie(idx: Int) {
        val current = movies.getTask(idx)
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

    override fun deleteList(idx: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMovieSearch(idx: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override lateinit var  movies: MovieRepository
    lateinit var MovieDb: RecentRepo
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recent_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        movies = MovieRepo()
        MovieDb = RecentRepo(activity!!.applicationContext)
        movies.refresh(MovieDb.getAll())
        Log.d("check000", MovieDb.getAll().toString())

        val listRecentAdapter = RecentListAdapter(this)
        recyclerRecentList.adapter = listRecentAdapter
        recyclerRecentList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        recyclerRecentList.adapter?.notifyDataSetChanged()
    }


}
