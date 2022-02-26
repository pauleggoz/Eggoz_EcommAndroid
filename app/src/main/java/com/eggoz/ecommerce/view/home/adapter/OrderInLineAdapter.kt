package com.eggoz.ecommerce.view.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.databinding.ItemOrderInlineBinding
import com.eggoz.ecommerce.network.model.OrderDetail
import com.eggoz.ecommerce.network.model.Orderhistory

class OrderInLineAdapter : ListAdapter<OrderDetail.OrderLines.OrderItem, OrderInLineAdapter.OrderInlineRecyclerViewHolder>(OrderInlineCallBack()) {

    class OrderInlineRecyclerViewHolder(
        private val binding: ItemOrderInlineBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OrderDetail.OrderLines.OrderItem) {
//            val df = DecimalFormat("#.##")
//            val price = df.format(item.price ?: 0.00)
            binding.apply {
                itemData=item
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderInlineRecyclerViewHolder {
        val binding = ItemOrderInlineBinding.inflate(LayoutInflater.from(parent.context))
        return OrderInlineRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderInlineRecyclerViewHolder, position: Int) {
        val currentArticle = getItem(position)
        holder.bind(currentArticle)
    }


}

class OrderInlineCallBack : DiffUtil.ItemCallback<OrderDetail.OrderLines.OrderItem>() {
    override fun areItemsTheSame(oldItem: OrderDetail.OrderLines.OrderItem, newItem: OrderDetail.OrderLines.OrderItem): Boolean =
        oldItem == newItem

    
    override fun areContentsTheSame(oldItem: OrderDetail.OrderLines.OrderItem, newItem: OrderDetail.OrderLines.OrderItem): Boolean =
        oldItem.quantity == newItem.quantity

}