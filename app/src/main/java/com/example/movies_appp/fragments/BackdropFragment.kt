package com.example.movies_appp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movies_appp.MovieDatabase
import com.example.movies_appp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_backdrop.view.*

/**
 * Makes the backdrop bigger by masking a fragment on the activity
 */
class BackdropFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_backdrop, container, false)
        val larger = arguments?.getString(MovieDatabase.BACKDROP_PATH)
        Picasso.get()
            .load(larger)
            .placeholder(R.drawable.gravity)
            .into(view.image_frag_backdrop)

        return view
    }


}
