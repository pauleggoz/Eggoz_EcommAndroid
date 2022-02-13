package com.eggoz.ecommerce.view.home.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.ItemOrderListBinding
import com.eggoz.ecommerce.network.model.OrderList
import com.eggoz.ecommerce.network.model.OrderModel

class CalendarOrderAdapter : ListAdapter<OrderList.OrderResult, CalendarOrderAdapter.OrderRecyclerViewHolder>(OrderCallBack()) {

    class OrderRecyclerViewHolder(
        private val binding: ItemOrderListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OrderList.OrderResult) {
            binding.apply {
//                itemData=item
//                itemTitle="${item.orderitem.name} ${item.orderitem.sku} Pcs X ${item.orderitem.quantity}"
                root.setOnClickListener { val bundle = Bundle()
                    bundle.putString("id", "${item.id}")
                    Navigation.findNavController(root)
                        .navigate(R.id.action_nav_orderlist_to_nav_orderdetail, bundle)

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderRecyclerViewHolder {
        val binding = ItemOrderListBinding.inflate(LayoutInflater.from(parent.context))
        return OrderRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderRecyclerViewHolder, position: Int) {
        val currentArticle = getItem(position)
        holder.bind(currentArticle)
    }
}

class OrderCallBack : DiffUtil.ItemCallback<OrderList.OrderResult>() {
    override fun areItemsTheSame(oldItem: OrderList.OrderResult, newItem: OrderList.OrderResult): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: OrderList.OrderResult, newItem: OrderList.OrderResult): Boolean =
        oldItem.orderPriceAmount == newItem.orderPriceAmount

}