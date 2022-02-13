package com.eggoz.ecommerce.view.order.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.ItemCustomOrderBinding
import com.eggoz.ecommerce.databinding.ItemOrderListBinding
import com.eggoz.ecommerce.network.model.OrderList
import com.eggoz.ecommerce.network.model.OrderModel
import com.eggoz.ecommerce.view.home.adapter.OrderInLineAdapter

class OrderListAdapter : ListAdapter<OrderList.OrderResult, OrderListAdapter.OrderRecyclerViewHolder>(OrderCallBack()) {


    class OrderRecyclerViewHolder(
        private val binding: ItemCustomOrderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var orderinlineAdapter: OrderInLineAdapter

        fun bind(item: OrderList.OrderResult) {
            orderinlineAdapter = OrderInLineAdapter()
            binding.apply {
                itemData=item
                viewadapterOrderInline=orderinlineAdapter
                orderinlineAdapter.submitList(item.orderLines.orderItems)
//                itemTitle="${item.orderitem.name} ${item.orderitem.sku} Pcs X ${item.orderitem.quantity}"
                root.setOnClickListener { val bundle = Bundle()
                    bundle.putString("id", item.orderId)
                    Navigation.findNavController(root)
                        .navigate(R.id.action_nav_orderlist_to_nav_orderdetail, bundle)

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
        holder.bind(currentArticle)
    }
}

class OrderCallBack : DiffUtil.ItemCallback<OrderList.OrderResult>() {
    override fun areItemsTheSame(oldItem: OrderList.OrderResult, newItem: OrderList.OrderResult): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: OrderList.OrderResult, newItem: OrderList.OrderResult): Boolean =
        oldItem.orderPriceAmount == newItem.orderPriceAmount

}
