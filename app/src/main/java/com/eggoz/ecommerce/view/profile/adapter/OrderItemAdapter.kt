package com.eggoz.ecommerce.view.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.databinding.ItemOrderItemBinding
import com.eggoz.ecommerce.network.model.OrderDetail

class OrderItemAdapter: ListAdapter<OrderDetail.OrderLines.OrderItem, OrderItemAdapter.OrderRecyclerViewHolder>(OrderItemCallBack()) {

    class OrderRecyclerViewHolder(
        private val binding: ItemOrderItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OrderDetail.OrderLines.OrderItem) {
            binding.apply {
                itemData=item
                txtQnt.text="Qnt ${item.quantity?.toInt() ?:0}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderRecyclerViewHolder {
        val binding = ItemOrderItemBinding.inflate(LayoutInflater.from(parent.context))
        return OrderRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderRecyclerViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class OrderItemCallBack : DiffUtil.ItemCallback<OrderDetail.OrderLines.OrderItem>() {
    override fun areItemsTheSame(oldItem: OrderDetail.OrderLines.OrderItem, newItem: OrderDetail.OrderLines.OrderItem): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: OrderDetail.OrderLines.OrderItem, newItem: OrderDetail.OrderLines.OrderItem): Boolean =
        oldItem.productId == newItem.productId

}
