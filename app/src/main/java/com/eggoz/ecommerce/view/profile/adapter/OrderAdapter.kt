package com.eggoz.ecommerce.view.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.databinding.ItemOrderBinding
import com.eggoz.ecommerce.network.model.OrderDetail
import java.text.SimpleDateFormat
import java.util.*

class OrderAdapter(val callback: (OrderDetail) -> Unit) :
    ListAdapter<OrderDetail, OrderAdapter.OrderRecyclerViewHolder>(OrderCallBack()) {

    class OrderRecyclerViewHolder(
        private val binding: ItemOrderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OrderDetail, ccallback: (OrderDetail) -> Unit) {
            val adapter = OrderItemAdapter()
            binding.apply {
                itemData = item
                viewadapter = adapter

                adapter.submitList(item.orderLines?.orderItems)

                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sszzz", Locale.US)

                try {
                    val d = sdf.parse(item.deliveryDate)
                    val date2 = (SimpleDateFormat("dd MMM, hh:mm aa")).format(d)
                    txtEstimatedDelivery.text = "$date2"
                } catch (e: Exception) {
                    e.printStackTrace();
                }
                root.setOnClickListener {
                    ccallback(item)
                }


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderRecyclerViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context))
        return OrderRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderRecyclerViewHolder, position: Int) {
        val currentArticle = getItem(position)
        holder.bind(currentArticle, callback)
    }
}

class OrderCallBack : DiffUtil.ItemCallback<OrderDetail>() {
    override fun areItemsTheSame(oldItem: OrderDetail, newItem: OrderDetail): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: OrderDetail, newItem: OrderDetail): Boolean =
        oldItem.orderId == newItem.orderId

}
