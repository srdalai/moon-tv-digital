package com.moontvdigital.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.moontvdigital.app.data.HallData
import com.moontvdigital.app.databinding.ItemHallCardLayoutBinding

class HallAdapter(
    private val hallList: List<HallData?>,
    private val onItemClickListener: OnItemClickListener
) : Adapter<HallAdapter.HallViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HallViewHolder {
        val binding =
            ItemHallCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HallViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return hallList.size
    }

    override fun onBindViewHolder(holder: HallViewHolder, position: Int) {
        val hallItem = hallList[position]
        holder.bind(hallItem!!, onItemClickListener)
    }

    inner class HallViewHolder(val binding: ItemHallCardLayoutBinding) : ViewHolder(binding.root) {

        fun bind(hallData: HallData, listener: OnItemClickListener) {
            Glide.with(binding.ivHallImage.context)
                .load(hallData.hallFilmThumbnail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivHallImage)

            binding.tvHallName.text = hallData.hallName
            val timingStr = hallData.hallTiming
            binding.tvHallTiming.text = timingStr

            binding.parentLayout.setOnClickListener {
                listener.onItemClicked(hallData)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(hallData: HallData)
    }
}