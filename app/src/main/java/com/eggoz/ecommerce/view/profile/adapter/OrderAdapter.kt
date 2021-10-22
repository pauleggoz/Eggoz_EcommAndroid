package com.eggoz.ecommerce.view.profile.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.ItemOrderBinding
import com.eggoz.ecommerce.network.model.Orderhistory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderAdapter(
    val date: ArrayList<String>,
    val orderitem: ArrayList<Orderhistory.Result.OrderLines.OrderItem>,
    val orderid: ArrayList<String>,
    val orderstatus: ArrayList<String>
) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var listItem: View
    private var qnt = 0


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemBinding: ItemOrderBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_order, parent, false
        )
        listItem = itemBinding.root
        context = itemBinding.root.context
        return ViewHolder(itemBinding)
    }


    override fun getItemCount(): Int {
        return if (orderid != null)
            orderid.size
        else
            0
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var dddate = "${date[position]}"
        dddate = dddate.substring(0, dddate.length - 10)
        val format = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US
        )
        format.timeZone = TimeZone.getTimeZone("UTC")
        try {
            if (date[position] != null) {
                val date: Date = format.parse("${date[position]}")!!
                val sdf = SimpleDateFormat("dd MMM, hh:mm aa")
                dddate = sdf.format(date)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("data", "Exception ${e.message}")
        }
        holder.binding.apply {
            txtTitle.text =
                "${orderitem[position].name}\n${orderitem[position].sku} Pcs X ${orderitem[position].quantity}"
            val qnt: Double = orderitem[position].quantity?.toDouble() ?: 0.0
            val price = orderitem[position].price ?: 0.0
            txtPrice.text = "₹ ${
                price * qnt
            }"
            txtDate.text = dddate
            txtStatus.text = orderstatus[position]
        }
    }


    class ViewHolder(itemBinding: ItemOrderBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: ItemOrderBinding = itemBinding

    }
}