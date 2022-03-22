package com.eggoz.ecommerce.view.order.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.databinding.ItemOrderStatusHistoryBinding
import com.eggoz.ecommerce.network.model.OrderEventModel
import java.text.SimpleDateFormat
import java.util.*

class OrderStatusHistoryAdapter :
    ListAdapter<OrderEventModel.Result, OrderStatusHistoryAdapter.OrderRecyclerViewHolder>(OrderStatusListCallBack()) {


    class OrderRecyclerViewHolder(
        private val binding: ItemOrderStatusHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: OrderEventModel.Result,
                 lastElement: Boolean,
                 fristElement: Boolean) {
            binding.apply {

                itemdata = item

                if (fristElement)
                    viewTop.visibility=View.GONE
                if (lastElement)
                    viewBelow.visibility=View.GONE

                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSzzz", Locale.US)

                try {
                    val d = sdf.parse(item.date)
                    val date2 = (SimpleDateFormat("dd MMM, hh:mm aa")).format(d)
                    txtDate.text = "$date2"
                } catch (e: Exception) {
                    e.printStackTrace();
                }


            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderRecyclerViewHolder {
        val binding = ItemOrderStatusHistoryBinding.inflate(LayoutInflater.from(parent.context))
        return OrderRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderRecyclerViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,position==itemCount-1,position==0)
    }
}

class OrderStatusListCallBack : DiffUtil.ItemCallback<OrderEventModel.Result>() {
    override fun areItemsTheSame(oldItem: OrderEventModel.Result, newItem: OrderEventModel.Result): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: OrderEventModel.Result, newItem: OrderEventModel.Result): Boolean =
        oldItem.user == newItem.user

}