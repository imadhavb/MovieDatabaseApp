package com.example.movies_appp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movies_appp.MovieDatabase
import com.example.movies_appp.R
import kotlinx.android.synthetic.main.fragment_text_fragment_larger.view.*

/**
 * Makes the overview bigger by masking a fragment on the activity
 */
class TextFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_text_fragment_larger, container, false)
        val larger = arguments?.getString(MovieDatabase.OVERVIEW)

        view.text_view_frag.text = larger

        return view
    }


}
