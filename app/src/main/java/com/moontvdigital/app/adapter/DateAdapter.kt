package com.moontvdigital.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.moontvdigital.app.R
import com.moontvdigital.app.data.ShowDate

class DateAdapter(private val dateList: List<ShowDate>, private val listener: DateClickedListener) : Adapter<DateAdapter.DateViewHolder>() {

    private var currentPos = 0;

    class DateViewHolder(itemView: View) : ViewHolder(itemView) {
        val dateContainer: LinearLayout = itemView.findViewById(R.id.dateContainer)
        val tvDay: TextView = itemView.findViewById(R.id.tvDay)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_date_card_layout, parent, false)
        return DateViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dateList.size
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val showDate = dateList[position]
        holder.tvDate.text = showDate.date.toString()
        holder.tvDay.text = showDate.day

        if (position == currentPos) {
            holder.dateContainer.isSelected = true
        } else {
            holder.dateContainer.isSelected = false
        }

        holder.dateContainer.setOnClickListener {
            currentPos = position
            notifyDataSetChanged()
            listener.onDateClicked(showDate)
        }

    }

    interface DateClickedListener {
        fun onDateClicked(showDate: ShowDate)
    }
}