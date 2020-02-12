package com.example.movies_appp

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.movies_appp.interfaces.MovieController
import com.example.movies_appp.interfaces.MovieRepository
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import com.example.movies_appp.adapters.MovieListAdapter
import com.example.movies_appp.fragments.FragmentRecentList
import com.example.movies_appp.fragments.SearchViewfragment
import com.example.movies_appp.models.MovieClass
import kotlinx.android.synthetic.main.fragment_search_view.*
import java.util.*




class MainActivity : AppCompatActivity(), MovieController {


    //Timer for notification to be sent out on (in ms). 86400000 is one day in ms so notification is sent daily
    private val mNotificationTime = Calendar.getInstance().timeInMillis + 86400000
    //Boolean for previous notifications
    private var mNotified = false
    // menu search function
    override fun deleteList(idx: Int) {
        val current = movies.getList(idx)
        if (current != null) {
            MovieDb.deleteList(current)
            movies.refreshList(MovieDb.getAllList())
        }
    }


    // kept the button in homescreen for consistency, but we don't really need it on home screen
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.logo_pic -> {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.mainmenu, menu)

        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {

            override fun onMenuItemActionExpand(menuItem: MenuItem): Boolean {
                val listRecentFragment = FragmentRecentList()
                //listRecentFragment.arguments = bundle
                supportFragmentManager.popBackStack()
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.frameLayoutSearch, listRecentFragment)
                    .addToBackStack(listRecentFragment.toString())
                    .commit()

                return true
            }

            override fun onMenuItemActionCollapse(menuItem: MenuItem): Boolean {
                return true
            }
        })
//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))


        //If user has not been notified in past 24 hours, notifies them
        if (!mNotified) {
            NotificationUtils().setNotification(mNotificationTime, this@MainActivity)
        }
        // voice search action
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                val searchFragment = SearchViewfragment()
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchItem.collapseActionView()
                val urlforthis = url + query
                val request = Request.Builder()
                    .url(urlforthis)
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute request")
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        val body = response?.body()?.string()
                        val gson = GsonBuilder().create()
                        val info = gson.fromJson(body, MainActivity.Information::class.java)
                        val bundle = Bundle()
                        bundle.putString("movie", body)
                        searchFragment.arguments = bundle
                        if (info.results.isNotEmpty()) {
                            supportFragmentManager
                                .beginTransaction()
                                .add(R.id.frameLayoutSearch, searchFragment)
                                .addToBackStack(searchFragment.toString())
                                .commit()

                        }
                        else{
                            handler.post{
                                Toast.makeText(this@MainActivity, "No movie was found", Toast.LENGTH_SHORT).show()
                                val listRecentFragment = FragmentRecentList()
                                supportFragmentManager.popBackStack()
                                supportFragmentManager
                                    .beginTransaction()
                                    .add(R.id.frameLayoutSearch, listRecentFragment)
                                    .addToBackStack(listRecentFragment.toString())
                                    .commit()

                            }
                        }
                    }
                })
            }
        }



        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchFragment = SearchViewfragment()
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchItem.collapseActionView()
                val urlforthis = url + query
                val request = Request.Builder()
                    .url(urlforthis)
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute request")
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        val body = response?.body()?.string()
                        val gson = GsonBuilder().create()
                        val info = gson.fromJson(body, MainActivity.Information::class.java)
                        val bundle = Bundle()
                        bundle.putString("movie", body)
                        searchFragment.arguments = bundle
                        if (info.results.isNotEmpty()) {
                            supportFragmentManager
                                .beginTransaction()
                                .add(R.id.frameLayoutSearch, searchFragment)
                                .addToBackStack(searchFragment.toString())
                                .commit()

                        }
                        else{
                            handler.post{
                                Toast.makeText(this@MainActivity, "No movie was found", Toast.LENGTH_SHORT).show()
                                val listRecentFragment = FragmentRecentList()
                                supportFragmentManager.popBackStack()
                                supportFragmentManager
                                    .beginTransaction()
                                    .add(R.id.frameLayoutSearch, listRecentFragment)
                                    .addToBackStack(listRecentFragment.toString())
                                    .commit()

                            }
                        }
                    }
                })
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               return true
            }

        })
        return true
    }

    override fun homeScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun showMovie(idx: Int) {
        val current = movies.getList(idx)
        val intent = Intent(this, MovieDatabase::class.java)
        intent.putExtra("overview", current.overview)
        intent.putExtra("release_date", current.release_date)
        intent.putExtra("poster_path", current.poster_path)
        intent.putExtra("vote_average",current.vote_average)
        intent.putExtra("title", current.title)
        intent.putExtra("back_drop", current.backdrop_path)
        intent.putExtra("original_language", current.original_lang)
        intent.putExtra("original_title", current.original_title)
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun showMovieSearch(idx: Int) {
        val current = movies.getSearch(idx)
        val intent = Intent(this, MovieDatabase::class.java)
        intent.putExtra("overview", current.overview)
        intent.putExtra("release_date", current.release_date)
        intent.putExtra("poster_path", current.poster_path)
        intent.putExtra("vote_average",current.vote_average)
        intent.putExtra("title", current.title)
        intent.putExtra("back_drop", current.backdrop_path)
        intent.putExtra("original_language", current.original_lang)
        intent.putExtra("original_title", current.original_title)
        startActivity(intent)
    }


    //Client for API Calls from OKHTTP implementation
    private var url = "https://api.themoviedb.org/3/search/movie?api_key=ff7868011db3616bcd9e710305c855a5&query="
    override lateinit var  movies: MovieRepository
    lateinit var MovieDb: RecentRepo
    lateinit var handler: Handler
    lateinit var receiver: MyReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler = Handler()
        receiver = MyReceiver()

        MovieDb = RecentRepo(this)
        val actionbar = supportActionBar
        actionbar!!.title = "BingeCheck"

        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setLogo(R.drawable.ic_local_movies_black_24dp)
        getSupportActionBar()?.setDisplayUseLogoEnabled(true)



        //Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show()
        movies = MovieRepo()
        MovieDb = RecentRepo(this)
        movies.refreshList(MovieDb.getAllList())

        val Listadapter = MovieListAdapter(this)
        recyclerviewlist.adapter = Listadapter
        recyclerviewlist.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) //RecyclerView

    }
    private val client = OkHttpClient()

    override fun onResume()  {
        super.onResume()
        registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_LOW))
        registerReceiver(receiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
        handler.post{
            recyclerviewlist.adapter?.notifyDataSetChanged() //updates recyclerview
        }

    }
    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    class Information(val results: List<Result>)

    class Result(
        val poster_path: String,
        val id: Int,
        val backdrop_path: String,
        val original_language: String,
        val title: String,
        val overview: String,
        val release_date: String,
        val vote_average: String,
        val original_title: String
    )


}
