package com.example.movies_appp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movies_appp.R
import com.example.movies_appp.interfaces.MovieController
import com.example.movies_appp.models.MovieClass
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.searchview_recycler.view.*

class SearchviewAdapter(private val controller: MovieController) :RecyclerView.Adapter<SearchListHolder>()  {
    override fun getItemCount(): Int {
        return controller.movies.getCountSearch()
    }

    override fun onBindViewHolder(holder: SearchListHolder, position: Int) {
        val list = controller.movies.getSearch(position)
        holder.bindTask(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.searchview_recycler, parent, false)
        val viewHolder = SearchListHolder(view)

        view.linearLayoutSearch.setOnClickListener {
            val position = viewHolder.adapterPosition
            controller.showMovieSearch(position)
            this.notifyItemChanged(position)
        }

        return viewHolder
    }

}
class SearchListHolder(view: View) : RecyclerView.ViewHolder(view){
    fun bindTask(movies: MovieClass){
        Picasso.get()
            .load(movies.poster_path)
            .into(itemView.imageViewSearch)

        itemView.textViewSearch.text = movies.title
    }
}