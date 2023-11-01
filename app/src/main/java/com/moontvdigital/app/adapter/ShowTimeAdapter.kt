package com.moontvdigital.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.moontvdigital.app.R
import com.moontvdigital.app.data.ShowDate
import com.moontvdigital.app.data.ShowTime

class ShowTimeAdapter(private val showTimes: List<ShowTime?>, private val listener: ShowTimeListener): Adapter<ShowTimeAdapter.ShowTimeViewHolder>() {

    var currentPos = 0;

    inner class ShowTimeViewHolder(itemView: View) : ViewHolder(itemView) {
        val tvShowTime: TextView = itemView.findViewById(R.id.tvShowTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowTimeViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_show_time_layout, parent, false)
        return ShowTimeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return showTimes.size
    }

    override fun onBindViewHolder(holder: ShowTimeViewHolder, position: Int) {
        val showTime = showTimes[position]
        holder.tvShowTime.text = showTime?.showStartTime

        holder.tvShowTime.isSelected = position == currentPos

        holder.tvShowTime.setOnClickListener {
            currentPos = position
            notifyDataSetChanged()
            listener.onShowTimeClicked(showTime!!)
        }
    }

    interface ShowTimeListener {
        fun onShowTimeClicked(showTime: ShowTime)
    }
}