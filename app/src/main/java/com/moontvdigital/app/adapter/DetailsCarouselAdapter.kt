package com.moontvdigital.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.moontvdigital.app.R
import com.moontvdigital.app.data.BannerItem

class DetailsCarouselAdapter(private var mContext: Context, private var bannerUrls: List<String>) : PagerAdapter() {
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context)
            .inflate(R.layout.item_details_carousel_layout, container, false)
        val imageViewBanner = view.findViewById<ImageView>(R.id.imageViewBanner)

        val bannerUrl = bannerUrls[position]

        Glide.with(mContext)
            .load(bannerUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageViewBanner)
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return bannerUrls.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }
}
