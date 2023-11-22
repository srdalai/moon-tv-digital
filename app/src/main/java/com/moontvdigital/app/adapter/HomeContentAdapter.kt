package com.moontvdigital.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.moontvdigital.app.data.HomeContent
import com.moontvdigital.app.databinding.ItemHomeContentLayoutBinding

class HomeContentAdapter(
    private val homeItems: List<HomeContent?>,
    private val onItemClickListener: OnItemClickListener
) : Adapter<HomeContentAdapter.HomeContentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeContentViewHolder {
        val binding = ItemHomeContentLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeContentViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return homeItems.size
    }

    override fun onBindViewHolder(holder: HomeContentViewHolder, position: Int) {
        val homeContent = homeItems[position]
        holder.bind(homeContent!!, onItemClickListener)
    }


    inner class HomeContentViewHolder(val binding: ItemHomeContentLayoutBinding) :
        ViewHolder(binding.root) {
        fun bind(homeContent: HomeContent, listener: OnItemClickListener) {
            Glide.with(binding.imageView)
                .load(homeContent.filmPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imageView)

            binding.parentFrame.setOnClickListener {
                listener.onItemClicked(homeContent)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(homeContent: HomeContent)
    }
}