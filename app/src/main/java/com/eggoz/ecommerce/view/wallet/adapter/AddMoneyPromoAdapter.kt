package com.eggoz.ecommerce.view.wallet.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.ItemPromoBinding
import com.eggoz.ecommerce.network.model.WalletPromo

class AddMoneyPromoAdapter(var result: List<WalletPromo.Result>
) : RecyclerView.Adapter<AddMoneyPromoAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var listItem: View


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemBinding: ItemPromoBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_promo, parent, false
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
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            txtDis.text="Get ₹ ${result[position].promo?.toDouble()?.toInt() ?:0} extra on adding ₹ ${result[position].amount?.toDouble()?.toInt()?:0} & above"
        }
    }


    class ViewHolder(itemBinding: ItemPromoBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: ItemPromoBinding = itemBinding

    }

}