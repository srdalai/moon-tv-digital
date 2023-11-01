package com.moontvdigital.app.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.moontvdigital.app.R
import com.moontvdigital.app.data.HallData

class HallAdapter(private val hallList: List<HallData?>, private val onItemClickListener: OnItemClickListener) : Adapter<HallAdapter.HallViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HallViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hall_card_layout, parent, false)
        return HallViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hallList.size
    }

    override fun onBindViewHolder(holder: HallViewHolder, position: Int) {
        val hallItem = hallList[position]
        holder.tvHallName.text = hallItem?.hallName

        /*val color = Color.argb(255, (10..100).random(), (10..100).random(), (10..100).random())
        holder.cardView.setCardBackgroundColor(color)*/


        holder.bind(hallItem!!, onItemClickListener)
    }

    class HallViewHolder(itemView: View) : ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val tvHallName: TextView = itemView.findViewById(R.id.tvHallName)

        fun bind(hallData: HallData, listener: OnItemClickListener) {
            cardView.setOnClickListener {
                listener.onItemClicked(hallData)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(hallData: HallData)
    }
}