package com.example.movies_appp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movies_appp.R
import com.example.movies_appp.interfaces.MovieController
import com.example.movies_appp.models.RecyclerviewMovie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerview_recommendations.view.*

class MovieAdapter(private val controller: MovieController) : RecyclerView.Adapter<TodoHolder>()  {
    override fun getItemCount(): Int {
        return controller.movies.getCount()
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        val list = controller.movies.getTask(position)
        holder.bindTask(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_recommendations, parent, false)
        val viewHolder = TodoHolder(view)

        view.linearLayout.setOnClickListener {
            val position = viewHolder.adapterPosition
            controller.showMovie(position)
            this.notifyItemChanged(position)
        }

        return viewHolder
    }

}


class TodoHolder(view: View) : RecyclerView.ViewHolder(view){
    fun bindTask(movies: RecyclerviewMovie){
        Picasso.get()
            .load(movies.poster_path)
            .placeholder(R.drawable.blackposter)
            .into(itemView.recentImage)

        itemView.recentText.text = movies.title
    }
}