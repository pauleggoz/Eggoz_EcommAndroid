package com.eggoz.ecommerce.view.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.ItemProductBinding
import com.eggoz.ecommerce.databinding.ItemSubscriptionBinding
import com.eggoz.ecommerce.network.model.Products
import com.eggoz.ecommerce.network.model.Sublist
import com.eggoz.ecommerce.view.product.ProductFragment
import com.eggoz.ecommerce.view.product.adapter.ProductAdapter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class SubscriptionAdapter : ListAdapter<Sublist.Result, SubscriptionAdapter.SubscriptionViewHolder>(SubscriptionCallBack()) {

    class SubscriptionViewHolder(
        private val binding: ItemSubscriptionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        val context: Context = binding.root.context

        fun bind(item: Sublist.Result) {
            binding.apply {
                itemdata = item

                var days=""
                if (item.days!=null && item.days.size ?:0 >0)
                    for (i in item.days.indices){
                        if (item.days[i] == 0){
                            if (days!="")
                                days+=",Sun"
                            else
                                days="Sun"
                        }else if (item.days[i] == 1){
                            if (days!="")
                                days+=",Mon"
                            else
                                days="Mon"
                        }else if (item.days[i] == 2){
                            if (days!="")
                                days+=",Tue"
                            else
                                days="Tue"
                        }else if (item.days[i] == 3){
                            if (days!="")
                                days+=",Wed"
                            else
                                days="Wed"
                        }else if (item.days[i] == 4){
                            if (days!="")
                                days+=",Thu"
                            else
                                days="Thu"
                        }else if (item.days[i] == 5){
                            if (days!="")
                                days+=",Fri"
                            else
                                days="Fri"
                        }else if (item.days[i] == 6){
                            if (days!="")
                                days+=",Sat"
                            else
                                days="Sat"
                        }
                    }
                iteamday=days
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder {
        val binding = ItemSubscriptionBinding.inflate(LayoutInflater.from(parent.context))
        return SubscriptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) {
        val currentArticle = getItem(position)
        holder.bind(currentArticle)
    }


}

class SubscriptionCallBack : DiffUtil.ItemCallback<Sublist.Result>() {
    override fun areItemsTheSame(oldItem: Sublist.Result, newItem: Sublist.Result): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Sublist.Result, newItem: Sublist.Result): Boolean =
        oldItem.subscription?.name == newItem.subscription?.name

}
