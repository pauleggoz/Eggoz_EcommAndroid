package com.eggoz.ecommerce.view.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.databinding.ItemOrderInlineBinding
import com.eggoz.ecommerce.network.model.OrderList

class OrderInLineAdapter : ListAdapter<OrderList.OrderResult.OrderLines.OrderItem, OrderInLineAdapter.OrderInlineRecyclerViewHolder>(OrderInlineCallBack()) {

    class OrderInlineRecyclerViewHolder(
        private val binding: ItemOrderInlineBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OrderList.OrderResult.OrderLines.OrderItem) {
//            val df = DecimalFormat("#.##")
//            val price = df.format(item.price ?: 0.00)
            binding.apply {
                itemData=item
                txtPrice.text="₹ ${item.price}"
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

class OrderInlineCallBack : DiffUtil.ItemCallback<OrderList.OrderResult.OrderLines.OrderItem>() {
    override fun areItemsTheSame(oldItem: OrderList.OrderResult.OrderLines.OrderItem, newItem: OrderList.OrderResult.OrderLines.OrderItem): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: OrderList.OrderResult.OrderLines.OrderItem, newItem: OrderList.OrderResult.OrderLines.OrderItem): Boolean =
        oldItem.quantity == newItem.quantity

}