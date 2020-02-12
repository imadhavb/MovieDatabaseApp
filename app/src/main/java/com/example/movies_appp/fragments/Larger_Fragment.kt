package com.example.movies_appp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movies_appp.MovieDatabase
import com.example.movies_appp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_result_movie.view.*

/**
 * Makes the poster bigger by masking a fragment on the activity
 */
class Larger_Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  {

        val view = inflater.inflate(R.layout.fragment_larger_, container, false)
        val larger = arguments?.getString(MovieDatabase.POSTER_PATH)
        Picasso.get()
            .load(larger)
            .placeholder(R.drawable.gravity)
            .into(view.img_Movie_Poster)

        return view

    }
}
