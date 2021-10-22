package com.eggoz.ecommerce.view.product.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.network.model.HomeSlider

class ImageSliderProd(private var result:List<String>) : PagerAdapter() {
    override fun getCount(): Int {
        return result.size
    }

    override fun getPageWidth(position: Int): Float {
        return 0.8f
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageLayout: View =
            LayoutInflater.from(container.context)
                .inflate(R.layout.slider_item_container, container, false)!!

        val image = imageLayout
            .findViewById<View>(R.id.imageView) as ImageView


        container.addView(imageLayout)

        Glide.with(imageLayout)
            .asBitmap()
            .load(result[position])
            .centerInside()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.logo1)
            .into(image)


        return imageLayout
    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }


}
