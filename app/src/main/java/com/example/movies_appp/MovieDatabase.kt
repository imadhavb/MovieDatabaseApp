package com.example.movies_appp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies_appp.adapters.MovieAdapter
import com.example.movies_appp.fragments.*
import com.example.movies_appp.interfaces.MovieController
import com.example.movies_appp.interfaces.MovieRepository
import com.example.movies_appp.models.RecyclerviewMovie
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import com.example.movies_appp.interfaces.RecentRepository
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_result_movie.*
import okhttp3.*
import java.io.IOException


class MovieDatabase : AppCompatActivity(), MovieController {
    override fun deleteList(idx: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMovieSearch(idx: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    lateinit var MovieDb: RecentRepository
    override lateinit var  movies: MovieRepository
    lateinit var receiver: MyReceiver
    lateinit var handler: Handler


    // home button
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

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(menuItem: MenuItem): Boolean {
                val listRecentFragment = FragmentRecentList()
                //listRecentFragment.arguments = bundle
                supportFragmentManager.popBackStack()
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.frameLayout, listRecentFragment)
                    .addToBackStack(listRecentFragment.toString())
                    .commit()

                return true
            }

            override fun onMenuItemActionCollapse(menuItem: MenuItem): Boolean {
                //supportFragmentManager.popBackStack()
                return true
            }
        })

        var url = "https://api.themoviedb.org/3/search/movie?api_key=ff7868011db3616bcd9e710305c855a5&query="
        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))
        val searchFragment = SearchViewfragment()
        

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //val searchFragment = SearchViewfragment()
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
                                .add(R.id.frameLayout, searchFragment)
                                .addToBackStack(searchFragment.toString())
                                .commit()

                        }
                        else{
                            handler.post{
                                Toast.makeText(this@MovieDatabase, "No movie was found", Toast.LENGTH_SHORT).show()
                                val listRecentFragment = FragmentRecentList()
                                supportFragmentManager.popBackStack()
                                supportFragmentManager
                                    .beginTransaction()
                                    .add(R.id.frameLayout, listRecentFragment)
                                    .addToBackStack(listRecentFragment.toString())
                                    .commit()

                            }
                        }
                    }
                })
                return true
            }


            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }
        })
        return true
    }

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.database_layout)
        handler = Handler()
        receiver = MyReceiver()

        val actionbar = supportActionBar

        actionbar!!.setTitle(intent.getStringExtra("title"))
        actionbar.setDisplayHomeAsUpEnabled(true)

        getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        getSupportActionBar()?.setLogo(R.drawable.ic_local_movies_black_24dp);
        getSupportActionBar()?.setDisplayUseLogoEnabled(true);

        var movie_title: String = intent.getStringExtra("title")
        var movie_description: String = intent.getStringExtra("overview")
        val movie_rating: String = intent.getStringExtra("vote_average")
        //var movie_rating: String = "Rating: " + intent.getStringExtra("vote_average")
        var posterLink: String = intent.getStringExtra("poster_path")
        var movie_release: String = intent.getStringExtra("release_date")
        var backdrop_path: String = intent.getStringExtra("back_drop")
        var original_lang: String = intent.getStringExtra("original_language")
        var original_title: String = intent.getStringExtra("original_title")
        text_Moive_Description.text = movie_description

        //Sets movie title
        movieTitle(movie_title, original_title)

        //Sets movie release date and rating
        dateRatingEdit(movie_release, movie_rating)

        //Adds the flag and original language
        languageEdit(original_lang)

        //Adds the poster and backdrop
        posterAdd(posterLink, backdrop_path)

        // clicking home btn takes to home
        img_Movie_Poster.setOnClickListener { posterLarger() } // Make the poster bigger
        text_Moive_Description.setOnClickListener { overviewLarger() } // make the text bigger
        backdropPath.setOnClickListener { backdropLarger()} // make the backdrop bigger
        addToList.setOnClickListener{
            val movie = RecyclerviewMovie(-1, posterLink, backdrop_path, movie_title, movie_description, movie_release, movie_rating,original_lang, original_title)
            MovieDb.addTaskList(movie) //adds to database
            movies.refreshList(MovieDb.getAllList())
            Toast.makeText(this, "Added to List", Toast.LENGTH_SHORT).show()
        }

            movies = MovieRepo()
            MovieDb = RecentRepo(this)
            movies.refresh(MovieDb.getAll())

            val adapter = MovieAdapter(this)
            recyclerview.adapter = adapter
            recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) //RecyclerView

            val movie = RecyclerviewMovie(-1, posterLink, backdrop_path, movie_title, movie_description, movie_release, movie_rating,original_lang, original_title)
            MovieDb.addTask(movie) //adds to database
            movies.refresh(MovieDb.getAll())



    }

    override fun showMovie(idx: Int){
        val current = movies.getTask(idx)
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


    private fun languageEdit(original_lang: String){
        text_Movie_Lang.text = original_lang.toUpperCase()
        when(original_lang){
            "en" -> {
                flagLayout.setBackgroundResource(R.drawable.united_kingdom_flag_large)
            }
            "ja" ->{
                flagLayout.setBackgroundResource(R.drawable.japan_flag_large)
            }
            "de" ->{
                flagLayout.setBackgroundResource(R.drawable.germany_flag_large)
            }
            "es" -> {
                flagLayout.setBackgroundResource(R.drawable.spain_flag_large)
            }
            else -> {
                flagLayout.setBackgroundResource(R.drawable.global)
            }

        }
    }

    private fun posterAdd(posterLink: String, backdrop_path: String){
        //set movie picture
        //for poster
        Picasso.get()
            .load(posterLink)
            .placeholder(R.drawable.blackposter)
            .into(img_Movie_Poster)
        //for backdrop
        Picasso.get()
            .load(backdrop_path)
            .placeholder(R.drawable.blackbackdrop)
            .into(backdropPath)

    }
    private fun dateRatingEdit(movie_release: String, movie_rating: String){
        //takes the date and formats its it to Month Day, Year. Example:(January 01, 2019)
        val date = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val dateCreated: String = formatter.format(date)
        var delimiter = "-"
        val parts = movie_release.split(delimiter)
        var movieRelease = ""
        when (parts[1]) {
            "01" -> {
                movieRelease = "January " + parts[2] + ", " + parts[0]
            }
            "02" -> {
                movieRelease = "February " + parts[2] + ", " + parts[0]
            }
            "03" -> {
                movieRelease = "March " + parts[2] + ", " + parts[0]
            }
            "04" -> {
                movieRelease = "April " + parts[2] + ", " + parts[0]
            }
            "05" -> {
                movieRelease = "May " + parts[2] + ", " + parts[0]
            }
            "06" -> {
                movieRelease = "June " + parts[2] + ", " + parts[0]
            }
            "07" -> {
                movieRelease = "July " + parts[2] + ", " + parts[0]
            }
            "08" -> {
                movieRelease = "August " + parts[2] + ", " + parts[0]
            }
            "09" -> {
                movieRelease = "September " + parts[2] + ", " + parts[0]
            }
            "10" -> {
                movieRelease = "October " + parts[2] + ", " + parts[0]
            }
            "11" -> {
                movieRelease = "November " + parts[2] + ", " + parts[0]
            }
            "12" -> {
                movieRelease = "December " + parts[2] + ", " + parts[0]
            }
        }

        //checks to see if the movie is out yet. If so the rating will be TBA
        if(movie_release.compareTo(dateCreated) > 0){
            text_Movie_Rating.text = "TBA"
        }
        else if(movie_release.compareTo(dateCreated) < 0){
            text_Movie_Rating.text = movie_rating
        }
        else{
            text_Movie_Rating.text = movie_rating
            movieRelease = "Today"
        }

        text_Release_Date.text = movieRelease

        //Adds color depending on rating. (7.5 >) = Green, (7.5> and >5) = Yellow, (< 5) = Red, (=0.0) = White
        if(movie_rating.toDouble() <= 5.0 && movie_rating.toDouble() > 0.0){
            colorLayout.setBackgroundColor(resources.getColor(R.color.colorRed))
        }
        else if(movie_rating.toDouble() > 5.0 && movie_rating.toDouble() < 7.5){
            colorLayout.setBackgroundColor(resources.getColor(R.color.colorYellow))
        }
        else if(movie_rating.toDouble() >= 7.5){
            colorLayout.setBackgroundColor(resources.getColor(R.color.colorGreen))
        }
        else if(movie_rating.toDouble() == 0.0){
            colorLayout.setBackgroundColor(resources.getColor(R.color.colorWhite))
        }
    }

    private fun movieTitle(movie_title : String, original_title: String){
        var checkTitle = false
        if(original_title != movie_title){
            text_Movie_Title.text = movie_title
            //text_Movie_Title.isClickable
            text_Movie_Title.setOnClickListener {
                if (!checkTitle) {
                    text_Movie_Title.text = original_title
                    checkTitle = true
                } else {
                    text_Movie_Title.text = movie_title
                    checkTitle = false

                }
            }
        }
        else{
            text_Movie_Title.text = movie_title
        }
    }


    override fun homeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }

    override fun onResume()  {
        super.onResume()
        recyclerview.adapter?.notifyDataSetChanged() //updates recyclerview
        registerReceiver(receiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
    }

    /*
        Fragments Below
    */

    //Calls a fragment to make the poster larger
    private fun posterLarger() {
        val largerFragment = Larger_Fragment()
        val args = intent.extras
        largerFragment.arguments = args
        supportFragmentManager
            .beginTransaction()
            .add(R.id.frameLayout, largerFragment) //.add(where the fragment is going over, what the fragment is)
            .addToBackStack(largerFragment.toString()) //adds to backstack to when back is pressed it goes back to MovieDatabase and not MainActivity
            .commit()

    }
    //Calls a fragment to make the overview larger
    private fun overviewLarger() {
        val largerFragment = TextFragment()
        val args = intent.extras
        largerFragment.arguments = args
        supportFragmentManager
            .beginTransaction()
            .add(R.id.frameLayout, largerFragment)
            .addToBackStack(largerFragment.toString())
            .commit()

    }
    //Calls a fragment to make the backdrop larger
    private fun backdropLarger() {
        val largerFragment = BackdropFragment()
        val args = intent.extras
        largerFragment.arguments = args
        supportFragmentManager
            .beginTransaction()
            .add(R.id.frameLayout, largerFragment)
            .addToBackStack(largerFragment.toString())
            .commit()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    companion object {
        val POSTER_PATH = "poster_path"
        val OVERVIEW = "overview"
        val BACKDROP_PATH = "back_drop"
    }
}

