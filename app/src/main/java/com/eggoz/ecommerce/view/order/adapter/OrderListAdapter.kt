package com.eggoz.ecommerce.view.order.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.databinding.ItemCustomOrderBinding
import com.eggoz.ecommerce.network.model.OrderDetail
import com.eggoz.ecommerce.network.model.OrderList

class OrderAdapter(val callback: (OrderDetail) -> (Unit)) : ListAdapter<OrderDetail, OrderAdapter.OrderRecyclerViewHolder>(OrderCallBack()) {


    class OrderRecyclerViewHolder(
        private val binding: ItemCustomOrderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OrderDetail, callback: (OrderDetail) -> Unit) {
            binding.apply {
                itemData=item
//                txtOrderqnt.text="${item.orderLines?.totalQuantity} Pieces"
                root.setOnClickListener {
                    callback(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderRecyclerViewHolder {
        val binding = ItemCustomOrderBinding.inflate(LayoutInflater.from(parent.context))
        return OrderRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderRecyclerViewHolder, position: Int) {
        val currentArticle = getItem(position)
        holder.bind(currentArticle,callback)
    }
}

class OrderCallBack : DiffUtil.ItemCallback<OrderDetail>() {
    override fun areItemsTheSame(oldItem: OrderDetail, newItem: OrderDetail): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: OrderDetail, newItem: OrderDetail): Boolean =
        oldItem.orderPriceAmount == newItem.orderPriceAmount

}