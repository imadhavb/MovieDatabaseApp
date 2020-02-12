package com.example.movies_appp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.movies_appp.models.RecyclerviewMovie

/**
 * Database for the recent movies that were clicked on
 */
object MovieContract {
    object MovieEntry : BaseColumns {
        const val TABLE_NAME = "Recents"
        const val COLUMN_NAME_POSTER_PATH = "PosterPath"
        const val COLUMN_NAME_BACKDROP_PATH = "Backdrop"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_OVERVIEW = "overview"
        const val COLUMN_NAME_RELEASE_DATE = "release"
        const val COLUMN_NAME_VOTE_AVERAGE = "vote"
        const val COLUMN_NAME_ORIGINAL_LANG = "lang"
        const val COLUMN_NAME_ORIGINAL_TITLE = "ogTitle"
        const val COLUMN_NAME_DELETED = "deleted"


    }
    object MovieList : BaseColumns {
        const val TABLE_NAME = "List"
        const val COLUMN_NAME_POSTER_PATH = "PosterPath"
        const val COLUMN_NAME_BACKDROP_PATH = "Backdrop"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_OVERVIEW = "overview"
        const val COLUMN_NAME_RELEASE_DATE = "release"
        const val COLUMN_NAME_VOTE_AVERAGE = "vote"
        const val COLUMN_NAME_ORIGINAL_LANG = "lang"
        const val COLUMN_NAME_ORIGINAL_TITLE = "ogTitle"
        const val COLUMN_NAME_DELETED = "deleted"


    }
}

interface IDatabase {
    fun addMovie(movie: RecyclerviewMovie)
    fun getMovies(): List<RecyclerviewMovie>
    fun getMovie(id: Int) : RecyclerviewMovie?


    fun addMovieList(movie: RecyclerviewMovie)
    fun getMoviesList(): List<RecyclerviewMovie>
    fun getMovieList(id: Int) : RecyclerviewMovie?
    fun deleteList(task: RecyclerviewMovie)



}


// CREATE TABLE songs (_id INTEGER PRIMARY KEY AUTOINCREMENT, song_name TEXT, song_artist INTEGER, song_awesome BOOL, deleted BOOL DEFAULT 0)
private const val CREATE_SONG_TABLE = "CREATE TABLE ${MovieContract.MovieEntry.TABLE_NAME} (" +
        "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "${MovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH} TEXT, " +
        "${MovieContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH} TEXT, " +
        "${MovieContract.MovieEntry.COLUMN_NAME_TITLE } TEXT,  " +
        "${MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW} TEXT, "  +
        "${MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE} TEXT, "  +
        "${MovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE} TEXT, "  +
        "${MovieContract.MovieEntry.COLUMN_NAME_ORIGINAL_LANG} TEXT, "  +
        "${MovieContract.MovieEntry.COLUMN_NAME_ORIGINAL_TITLE} TEXT, "  +
        "${MovieContract.MovieEntry.COLUMN_NAME_DELETED} BOOL DEFAULT 0" +
        ")"

private const val CREATE_LIST_TABLE = "CREATE TABLE ${MovieContract.MovieList.TABLE_NAME} (" +
        "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "${MovieContract.MovieList.COLUMN_NAME_POSTER_PATH} TEXT, " +
        "${MovieContract.MovieList.COLUMN_NAME_BACKDROP_PATH} TEXT, " +
        "${MovieContract.MovieList.COLUMN_NAME_TITLE } TEXT,  " +
        "${MovieContract.MovieList.COLUMN_NAME_OVERVIEW} TEXT, "  +
        "${MovieContract.MovieList.COLUMN_NAME_RELEASE_DATE} TEXT, "  +
        "${MovieContract.MovieList.COLUMN_NAME_VOTE_AVERAGE} TEXT, "  +
        "${MovieContract.MovieList.COLUMN_NAME_ORIGINAL_LANG} TEXT, "  +
        "${MovieContract.MovieList.COLUMN_NAME_ORIGINAL_TITLE} TEXT, "  +
        "${MovieContract.MovieList.COLUMN_NAME_DELETED} BOOL DEFAULT 0" +
        ")"

private const val DELETE_TODO_TABLE = "DROP TABLE IF EXISTS ${MovieContract.MovieEntry.TABLE_NAME}"
private const val DELETE_TODO_TABLE_LIST = "DROP TABLE IF EXISTS ${MovieContract.MovieList.TABLE_NAME}"

class RecentDatabase(ctx: Context) : IDatabase {


    class SongDbHelper(ctx: Context) : SQLiteOpenHelper(ctx, DATABASE_NAME, null, DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(CREATE_SONG_TABLE)
            db?.execSQL(CREATE_LIST_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL(DELETE_TODO_TABLE)
            db?.execSQL(DELETE_TODO_TABLE_LIST)
            onCreate(db)
        }

        companion object{
            val DATABASE_NAME = "Movie.db"
            val DATABASE_VERSION = 1
        }
    }

    private val MD : SQLiteDatabase

    init {
        MD = SongDbHelper(ctx).writableDatabase
    }
    override fun deleteList(movie: RecyclerviewMovie) {
        val cvs = toContentValues(movie)
        cvs.put(MovieContract.MovieList.COLUMN_NAME_DELETED, "1")
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(movie.id.toString())
        MD.update(MovieContract.MovieList.TABLE_NAME, cvs, selection, selectionArgs)
    }
    override fun addMovieList(movie: RecyclerviewMovie) {
        val cvs = toContentValuesList(movie)
        MD.insert(MovieContract.MovieList.TABLE_NAME, null, cvs)
    }

    override fun getMoviesList(): List<RecyclerviewMovie> {
        val projection = arrayOf(BaseColumns._ID,
            MovieContract.MovieList.COLUMN_NAME_POSTER_PATH,
            MovieContract.MovieList.COLUMN_NAME_BACKDROP_PATH,
            MovieContract.MovieList.COLUMN_NAME_TITLE,
            MovieContract.MovieList.COLUMN_NAME_OVERVIEW,
            MovieContract.MovieList.COLUMN_NAME_RELEASE_DATE,
            MovieContract.MovieList.COLUMN_NAME_VOTE_AVERAGE,
            MovieContract.MovieList.COLUMN_NAME_ORIGINAL_LANG,
            MovieContract.MovieList.COLUMN_NAME_ORIGINAL_TITLE) //which columns do we want?
        val sortOrder = "${BaseColumns._ID} DESC" // how should the songs be ordered
        val selection = "${MovieContract.MovieList.COLUMN_NAME_DELETED} = ?" // where clause?
        val selectionArgs = arrayOf("0")

        val cursor2 = MD.query(
            MovieContract.MovieList.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
        val moviesList = mutableListOf<RecyclerviewMovie>()
        with(cursor2){
            while(cursor2.moveToNext()){
                val id = getInt(getColumnIndex(BaseColumns._ID))
                val poster_path = getString(getColumnIndex(MovieContract.MovieList.COLUMN_NAME_POSTER_PATH))
                val backdrop_path = getString(getColumnIndex(MovieContract.MovieList.COLUMN_NAME_BACKDROP_PATH))
                val title = getString(getColumnIndex(MovieContract.MovieList.COLUMN_NAME_TITLE))
                val overview = getString(getColumnIndex(MovieContract.MovieList.COLUMN_NAME_OVERVIEW))
                val release_date = getString(getColumnIndex(MovieContract.MovieList.COLUMN_NAME_RELEASE_DATE))
                val vote_average = getString(getColumnIndex(MovieContract.MovieList.COLUMN_NAME_VOTE_AVERAGE))
                val original_lang = getString(getColumnIndex(MovieContract.MovieList.COLUMN_NAME_ORIGINAL_LANG))
                val original_title = getString(getColumnIndex(MovieContract.MovieList.COLUMN_NAME_ORIGINAL_TITLE))
                val movie = RecyclerviewMovie(id, poster_path, backdrop_path, title, overview, release_date,vote_average, original_lang, original_title)
                moviesList.add(movie)
            }
        }
        return moviesList
    }
    override fun getMovieList(id: Int): RecyclerviewMovie? {
        val projection = arrayOf(BaseColumns._ID,
            MovieContract.MovieList.COLUMN_NAME_POSTER_PATH,
            MovieContract.MovieList.COLUMN_NAME_BACKDROP_PATH,
            MovieContract.MovieList.COLUMN_NAME_TITLE,
            MovieContract.MovieList.COLUMN_NAME_OVERVIEW,
            MovieContract.MovieList.COLUMN_NAME_RELEASE_DATE,
            MovieContract.MovieList.COLUMN_NAME_VOTE_AVERAGE,
            MovieContract.MovieList.COLUMN_NAME_ORIGINAL_LANG


        )
        val sortOrder = "${BaseColumns._ID} ASC" // how should the songs be ordered
        val selection = "${MovieContract.MovieList.COLUMN_NAME_DELETED} = ?" // where clause?
        val selectionArgs = arrayOf("0")

        val cursor3 = MD.query(
            MovieContract.MovieList.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
        val moviesList = mutableListOf<RecyclerviewMovie>()
        with(cursor3){
            while(cursor3.moveToNext()){
                val id = getInt(getColumnIndex(BaseColumns._ID))
                val poster_path = getString(getColumnIndex(MovieContract.MovieList.COLUMN_NAME_POSTER_PATH))
                val backdrop_path = getString(getColumnIndex(MovieContract.MovieList.COLUMN_NAME_BACKDROP_PATH))
                val title = getString(getColumnIndex(MovieContract.MovieList.COLUMN_NAME_TITLE))
                val overview = getString(getColumnIndex(MovieContract.MovieList.COLUMN_NAME_OVERVIEW))
                val release_date = getString(getColumnIndex(MovieContract.MovieList.COLUMN_NAME_RELEASE_DATE))
                val vote_average = getString(getColumnIndex(MovieContract.MovieList.COLUMN_NAME_VOTE_AVERAGE))
                val original_lang = getString(getColumnIndex(MovieContract.MovieList.COLUMN_NAME_ORIGINAL_LANG))
                val original_title = getString(getColumnIndex(MovieContract.MovieList.COLUMN_NAME_ORIGINAL_TITLE))
                val movie = RecyclerviewMovie(id, poster_path, backdrop_path, title, overview, release_date,vote_average, original_lang, original_title)
                moviesList.add(movie)
            }
        }
        if(moviesList.size == 1) return moviesList[0]
        return null
    }
    override fun addMovie(task: RecyclerviewMovie) {
        val cvs = toContentValues(task)
        MD.insert(MovieContract.MovieEntry.TABLE_NAME, null, cvs)

    }

    override fun getMovies(): List<RecyclerviewMovie> {
        val cursor1 = MD.rawQuery(
            "SELECT DISTINCT * " +
                    "FROM ${MovieContract.MovieEntry.TABLE_NAME} " +
                    "GROUP BY ${MovieContract.MovieEntry.COLUMN_NAME_TITLE} " +
                    "ORDER BY ${BaseColumns._ID} DESC", null
        )// SQL code to SELECT all the data FROM the table database and GROUP together the duplicates then ORDER them by most recently clicked by adding DESC
        val movies = mutableListOf<RecyclerviewMovie>()
        with(cursor1){
            while(cursor1.moveToNext()){
                val id = getInt(getColumnIndex(BaseColumns._ID))
                val poster_path = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH))
                val backdrop_path = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH))
                val title = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_TITLE))
                val overview = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW))
                val release_date = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE))
                val vote_average = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE))
                val original_lang = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_ORIGINAL_LANG))
                val original_title = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_ORIGINAL_TITLE))
                val movie = RecyclerviewMovie(id, poster_path, backdrop_path, title, overview, release_date,vote_average, original_lang, original_title)
                movies.add(movie)
            }
        }
        return movies
    }

    override fun getMovie(id: Int): RecyclerviewMovie? {
        val projection = arrayOf(BaseColumns._ID,
            MovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH,
            MovieContract.MovieEntry.COLUMN_NAME_TITLE,
            MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_NAME_ORIGINAL_LANG


        )
        val sortOrder = "${BaseColumns._ID} ASC" // how should the songs be ordered
        val selection = "${MovieContract.MovieEntry.COLUMN_NAME_DELETED} = ?" // where clause?
        val selectionArgs = arrayOf("0")

        val cursor = MD.query(
            MovieContract.MovieEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
        val movies = mutableListOf<RecyclerviewMovie>()
        with(cursor){
            while(cursor.moveToNext()){
                val id = getInt(getColumnIndex(BaseColumns._ID))
                val poster_path = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH))
                val backdrop_path = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH))
                val title = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_TITLE))
                val overview = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW))
                val release_date = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE))
                val vote_average = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE))
                val original_lang = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_ORIGINAL_LANG))
                val original_title = getString(getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_ORIGINAL_TITLE))
                val movie = RecyclerviewMovie(id, poster_path, backdrop_path, title, overview, release_date,vote_average, original_lang, original_title)
                movies.add(movie)
            }
        }
        if(movies.size == 1) return movies[0]
        return null
    }

    /*override fun deleteMovie(movie: RecyclerviewMovie) {
        val cvs = toContentValues(movie)
        cvs.put(MovieContract.MovieEntry.COLUMN_NAME_DELETED, "1")
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(movie.id.toString())
        MD.update(MovieContract.MovieEntry.TABLE_NAME, cvs, selection, selectionArgs)
    }

    override fun updateMovie(task: RecyclerviewMovie) {
        val cvs = toContentValues(task)
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(task.id.toString())
        MD.update(MovieContract.MovieEntry.TABLE_NAME, cvs, selection, selectionArgs)
    }*/

    private fun toContentValues(movie: RecyclerviewMovie): ContentValues {
        val cv = ContentValues()
        cv.put(MovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH, movie.poster_path)
        cv.put(MovieContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH, movie.backdrop_path)
        cv.put(MovieContract.MovieEntry.COLUMN_NAME_TITLE, movie.title)
        cv.put(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW, movie.overview)
        cv.put(MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, movie.release_date)
        cv.put(MovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE, movie.vote_average)
        cv.put(MovieContract.MovieEntry.COLUMN_NAME_ORIGINAL_LANG, movie.original_lang)
        cv.put(MovieContract.MovieEntry.COLUMN_NAME_ORIGINAL_TITLE, movie.original_title)
        return cv
    }

    private fun toContentValuesList(movie: RecyclerviewMovie): ContentValues {
        val cv = ContentValues()
        cv.put(MovieContract.MovieList.COLUMN_NAME_POSTER_PATH, movie.poster_path)
        cv.put(MovieContract.MovieList.COLUMN_NAME_BACKDROP_PATH, movie.backdrop_path)
        cv.put(MovieContract.MovieList.COLUMN_NAME_TITLE, movie.title)
        cv.put(MovieContract.MovieList.COLUMN_NAME_OVERVIEW, movie.overview)
        cv.put(MovieContract.MovieList.COLUMN_NAME_RELEASE_DATE, movie.release_date)
        cv.put(MovieContract.MovieList.COLUMN_NAME_VOTE_AVERAGE, movie.vote_average)
        cv.put(MovieContract.MovieList.COLUMN_NAME_ORIGINAL_LANG, movie.original_lang)
        cv.put(MovieContract.MovieList.COLUMN_NAME_ORIGINAL_TITLE, movie.original_title)
        return cv
    }
}