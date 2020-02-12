package com.example.movies_appp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movies_appp.R
import com.example.movies_appp.interfaces.MovieController
import com.example.movies_appp.models.RecyclerviewMovie
import kotlinx.android.synthetic.main.listrecentrecylerview.view.*

class RecentListAdapter(private val controller: MovieController) : RecyclerView.Adapter<TodoListHolder>()  {
    override fun getItemCount(): Int {
        return controller.movies.getCount()
    }

    override fun onBindViewHolder(holder: TodoListHolder, position: Int) {
        val list = controller.movies.getTask(position)
        holder.bindTask(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listrecentrecylerview, parent, false)
        val viewHolder = TodoListHolder(view)

        view.linearLayoutList.setOnClickListener {
            val position = viewHolder.adapterPosition
            controller.showMovie(position)
            this.notifyItemChanged(position)
        }

        return viewHolder
    }

}


class TodoListHolder(view: View) : RecyclerView.ViewHolder(view){
    fun bindTask(movies: RecyclerviewMovie){

        itemView.recentTextList.text = movies.title
    }
}