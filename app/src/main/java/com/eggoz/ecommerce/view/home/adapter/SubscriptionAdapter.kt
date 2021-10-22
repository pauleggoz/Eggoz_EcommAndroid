package com.eggoz.ecommerce.view.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.ItemSubscriptionBinding
import com.eggoz.ecommerce.network.model.Sublist
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class SubscriptionAdapter(var result: List<Sublist.Result>) : RecyclerView.Adapter<SubscriptionAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var listItem: View


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemBinding: ItemSubscriptionBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_subscription, parent, false
        )
        listItem = itemBinding.root
        context = itemBinding.root.context
        return ViewHolder(itemBinding)
    }



    override fun getItemCount(): Int {
        return result.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            txtPrice.text="₹ ${result[position].product?.ecommercePrice?.toDouble()?.toInt() ?:0}"
            txtHeading.text=result[position].product?.description
            var days=""
            if (result[position].days!=null && result[position].days?.size ?:0 >0)
            for (i in result[position].days?.indices!!){
                if (result[position].days!=null) {
                    if (result[position].days!![i] == 0){
                        if (days!="")
                            days+=",Sun"
                        else
                            days="Sun"
                    }else if (result[position].days!![i] == 1){
                        if (days!="")
                            days+=",Mon"
                        else
                            days="Mon"
                    }else if (result[position].days!![i] == 2){
                        if (days!="")
                            days+=",Tue"
                        else
                            days="Tue"
                    }else if (result[position].days!![i] == 3){
                        if (days!="")
                            days+=",Wed"
                        else
                            days="Wed"
                    }else if (result[position].days!![i] == 4){
                        if (days!="")
                            days+=",Thu"
                        else
                            days="Thu"
                    }else if (result[position].days!![i] == 5){
                        if (days!="")
                            days+=",Fri"
                        else
                            days="Fri"
                    }else if (result[position].days!![i] == 6){
                        if (days!="")
                            days+=",Sat"
                        else
                            days="Sat"
                    }
                }
            }
            txtDays.text=days

            val date: Date =
                SimpleDateFormat("yyyy-MM-dd").parse(result[position].startDate)
            val sdf =
                SimpleDateFormat("dd-MMM-yyyy")

            Glide.with(context)
                .asBitmap()
                .load(result[position].product?.productImage ?: "")
                .centerInside()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.logo1)
                .into(imgItem)

            txtDate.text="Start date: ${sdf.format(date.time)}"
        }
    }


    class ViewHolder(itemBinding: ItemSubscriptionBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: ItemSubscriptionBinding = itemBinding

    }

}