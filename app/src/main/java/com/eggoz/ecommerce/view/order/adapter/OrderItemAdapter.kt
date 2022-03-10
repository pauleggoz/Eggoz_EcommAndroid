package com.eggoz.ecommerce.view.order.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.databinding.ItemOrderItemBinding
import com.eggoz.ecommerce.network.model.OrderDetail

class OrderItemAdapter :ListAdapter<OrderDetail.OrderLines.OrderItem, OrderItemAdapter.OrderRecyclerViewHolder>(OrderItemListCallBack()) {


    class OrderRecyclerViewHolder(
        private val binding: ItemOrderItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: OrderDetail.OrderLines.OrderItem) {
            binding.apply {
                itemData =item
                txtName.text="${item.quantity?.toInt() ?:0} X ${item.name}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderRecyclerViewHolder {
        val binding = ItemOrderItemBinding.inflate(LayoutInflater.from(parent.context))
        return OrderRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderRecyclerViewHolder, position: Int) {
        val currentArticle = getItem(position)
        holder.bind(currentArticle)
    }
}

class OrderItemListCallBack : DiffUtil.ItemCallback<OrderDetail.OrderLines.OrderItem>() {
    override fun areItemsTheSame(oldItem: OrderDetail.OrderLines.OrderItem, newItem: OrderDetail.OrderLines.OrderItem): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: OrderDetail.OrderLines.OrderItem, newItem: OrderDetail.OrderLines.OrderItem): Boolean =
        oldItem.name == newItem.name

}
