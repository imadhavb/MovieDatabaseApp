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

class MovieListAdapter(private val controller: MovieController) : RecyclerView.Adapter<TodoHolderList>()  {
    override fun getItemCount(): Int {
        return controller.movies.getCountList()
    }

    override fun onBindViewHolder(holder: TodoHolderList, position: Int) {
        val list = controller.movies.getList(position)
        holder.bindTask(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolderList {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_recommendations, parent, false)
        val viewHolder = TodoHolderList(view)

        view.linearLayout.setOnClickListener {
            val position = viewHolder.adapterPosition
            controller.showMovie(position)
            this.notifyItemChanged(position)
        }

        view.linearLayout.setOnLongClickListener  {
            val position = viewHolder.adapterPosition
            controller.deleteList(position)
            this.notifyItemRemoved(position)
            true
        }

        return viewHolder
    }

}
class TodoHolderList(view: View) : RecyclerView.ViewHolder(view){
    fun bindTask(movies: RecyclerviewMovie){
        Picasso.get()
            .load(movies.poster_path)
            .placeholder(R.drawable.blackposter)
            .into(itemView.recentImage)

        itemView.recentText.text = movies.title
    }
}