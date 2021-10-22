package com.eggoz.ecommerce.view.wallet.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.ItemWallhisBinding
import com.eggoz.ecommerce.network.model.Wallet
import java.util.*

class WalletHistoryAdapter(var result: List<Wallet.Result>
) : RecyclerView.Adapter<WalletHistoryAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var listItem: View


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemBinding: ItemWallhisBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_wallhis, parent, false
        )
        listItem = itemBinding.root
        context = itemBinding.root.context
        return ViewHolder(itemBinding)
    }


    override fun getItemCount(): Int {
        return if (result != null) {
            result.size
        } else {
            0
        }
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {/*

        holder.binding.apply {
            txtTitle.text = result[position]?.name.toString()

//        holder.txt_price.text="${result[position].orderPriceAmount}"
//        holder.txt_ordderid.text="Eggoz Order #${result[position].orderId}"
//        holder.txt_ordderdate.text="${result[position].date}"

            val quantity: Double = result[position]?.quantity?.toDouble() ?: 0.0
            val itemprice: Double = result[position]?.price ?: 0.0

            val price = itemprice * quantity

            if (price < 0) {
                txtPrice.text = "- ₹ ${price}"
                txtPrice.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.cardred
                    )
                )
            } else {
                txtPrice.setTextColor(ContextCompat.getColor(context, R.color.green))
                txtPrice.text = "+ ₹ $price"

            }

            txtOrdderid.text = "Eggoz Order #${orderid[position]}"

            var dddate = "${orderdate[position]}"
            dddate = dddate.substring(0, dddate.length - 10)
            val format = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US
            )
            format.timeZone = TimeZone.getTimeZone("UTC")
            try {
                if (orderdate[position]!=null) {
                    val date: Date = format.parse("${orderdate[position]}")!!
                    val sdf = SimpleDateFormat("dd MMM, hh:mm aa")
                    dddate = sdf.format(date)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("data", "Exception ${e.message}")
            }

            txtOrdderdate.text = dddate


        }*/
    }


    class ViewHolder(itemBinding: ItemWallhisBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: ItemWallhisBinding = itemBinding

    }

}