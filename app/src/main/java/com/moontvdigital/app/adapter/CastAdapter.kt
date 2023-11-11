package com.moontvdigital.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.moontvdigital.app.R
import com.moontvdigital.app.data.CastItem

class CastAdapter(private val castList: List<CastItem>, private val listener: CastClickListener) : Adapter<CastAdapter.CastViewHolder>() {

    class CastViewHolder(itemView: View) : ViewHolder(itemView) {
        val parentFrame: FrameLayout = itemView.findViewById(R.id.parentFrame)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val tvCharName: TextView = itemView.findViewById(R.id.tvCharName)
        val tvActorName: TextView = itemView.findViewById(R.id.tvActorName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cast_list_layout, parent, false)
        return CastViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return castList.size
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {

        val castItem = castList[position]
        Glide.with(holder.itemView.context)
            .load(castItem.actorImagePath)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imageView)

        holder.tvCharName.text = castItem.actorType
        holder.tvActorName.text = castItem.actorName

        holder.parentFrame.setOnClickListener {
            listener.onCastItemClick(castItem)
        }
    }

    interface CastClickListener {
        fun onCastItemClick(castItem: CastItem)
    }
}