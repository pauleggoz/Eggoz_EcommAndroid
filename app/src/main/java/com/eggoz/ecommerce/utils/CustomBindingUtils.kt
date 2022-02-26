package com.eggoz.ecommerce.utils

import android.annotation.SuppressLint
import android.graphics.Paint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.eggoz.ecommerce.R
import com.google.android.material.textview.MaterialTextView
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object CustomBindingUtils {
    @JvmStatic
    @BindingAdapter(value = ["setImageUrl"])
    fun ImageView.bindImageUrl(url: String?) {
        if (url != null && url.isNotBlank()) {
            Glide.with(context)
                .asBitmap()
                .load(url)
                .centerInside()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.logo1)
                .into(this)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setAdapter"])
    fun RecyclerView.bindRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>) {
        this.run {
//            this.setHasFixedSize(true)
            this.adapter = adapter
            this.isNestedScrollingEnabled = false
        }
    }


    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter(value = ["bind:setCount", "bind:postval"], requireAll = false)
    fun TextView.bindText(value: Int?, postval: String?) {
        this.run {
            this.text = "${value ?: 0} ${postval ?: ""}"
        }
    }


    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter(value = ["bind:weight", "bind:weightval"], requireAll = false)
    fun TextView.bindweightText(weight: String?, weightval: String?) {
        this.run {
            this.text = " ${weight?.toDouble()?.toInt() ?: 0} ${weightval ?: ""}"
        }
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter(value = ["bind:pre", "bind:post"], requireAll = false)
    fun TextView.bindpriceText(pre: String?, post: String?) {
        this.run {
            val df = DecimalFormat("#.##")
            val price = df.format(post?.toDouble() ?: 0.00)
            this.text = "$pre $price"
        }
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter(value = ["bind:premat", "bind:postmat"], requireAll = false)
    fun MaterialTextView.bindpriceText(premat: String?, postmat: Double?) {
        this.run {
            val df = DecimalFormat("#.##")
            val price = df.format(postmat ?: 0.00)
            this.text = "$premat $price"
        }
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter(value = ["bind:itemcurr", "bind:itemprice"], requireAll = false)
    fun MaterialTextView.bindpriceText(itemcurr: String?, itemprice: String?) {
        this.run {
            val df = DecimalFormat("#.##")
            val price = df.format(itemprice?.toDouble() ?: 0.00)
            this.text = "$itemcurr $price"
        }
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter(value = ["bind:iteamName", "bind:iteamCount", "bind:iteamval"],requireAll = false)
    fun MaterialTextView.bindpriceText(iteamName: String?, iteamCount: Int?, iteamval: String) {
        this.run {
            this.text = "${iteamName ?: ""} ${iteamCount ?: 0} $iteamval"
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    @JvmStatic
    @BindingAdapter(value = ["bind:itemDate"], requireAll = false)
    fun MaterialTextView.bindpriceText(iteamName: String?) {
        this.run {
            val date: Date =
                SimpleDateFormat("yyyy-MM-dd").parse(iteamName)
            val sdf =
                SimpleDateFormat("dd-MMM-yyyy")

            this.text = "Start date: ${sdf.format(date.time)}"
        }
    }



    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    @BindingAdapter(value = ["bind:setDate"],requireAll = false)
    fun TextView.bindsetDateText(setDate: String?) {
        this.run {
            var dddate = setDate?:""
            dddate = dddate.substring(0, dddate.length - 10)
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)
            format.timeZone = TimeZone.getTimeZone("UTC")
            try {
                val date: Date = format.parse(dddate)!!
                val sdf = SimpleDateFormat("dd MMM, hh:mm aa")
                dddate = sdf.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            this.text = dddate
        }
    }


    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter(value = ["bind:setPrice"],requireAll = false)
    fun TextView.bindsetPriceText(price: String?) {
        this.run {
            this.text = "₹ ${price?.toDouble() ?:0.0}"
        }
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter(value = ["bind:setDisPrice"],requireAll = false)
    fun TextView.bindsetDisPriceText(disprice: String?) {
        this.run {
            val dis= (disprice?.toDouble())?.div(4.0) ?:0.0
            val disPrice= disprice?.toDouble()?.plus(dis) ?:0.0
            this.paintFlags =
                this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            this.text = "₹ $disPrice"
        }
    }

}