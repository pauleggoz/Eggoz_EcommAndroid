package com.eggoz.ecommerce.view.order.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.databinding.ItemCustomOrderBinding
import com.eggoz.ecommerce.network.model.OrderDetail
import com.eggoz.ecommerce.network.model.OrderList
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class OrderListAdapter(val callback: (OrderDetail) -> (Unit)) :
    ListAdapter<OrderDetail, OrderListAdapter.OrderRecyclerViewHolder>(OrderListCallBack()) {


    class OrderRecyclerViewHolder(
        private val binding: ItemCustomOrderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var adapter:OrderItemAdapter


        @SuppressLint("SetTextI18n")
        fun bind(item: OrderDetail, callback: (OrderDetail) -> Unit) {
            binding.apply {
                itemData = item

                adapter=OrderItemAdapter()

                itemAdapter =adapter

                adapter.submitList(item.orderLines?.orderItems)



                txtPrice.text = "₹ ${item.orderPriceAmount?.toDouble()?.toInt() ?: 0}"
//                txtOrderqnt.text = "${item.orderLines?.totalQuantity?.toInt() ?: 0} Pieces"
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sszzz", Locale.US)

                try {
                    val d = sdf.parse(item.deliveryDate)
                    val date2 = (SimpleDateFormat("dd MMM, hh:mm aa")).format(d)
                    txtDate.text = "$date2"
                } catch (e: Exception) {
                    e.printStackTrace();
                }





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
        holder.bind(currentArticle, callback)
    }
}

class OrderListCallBack : DiffUtil.ItemCallback<OrderDetail>() {
    override fun areItemsTheSame(oldItem: OrderDetail, newItem: OrderDetail): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: OrderDetail, newItem: OrderDetail): Boolean =
        oldItem.orderPriceAmount == newItem.orderPriceAmount

}
