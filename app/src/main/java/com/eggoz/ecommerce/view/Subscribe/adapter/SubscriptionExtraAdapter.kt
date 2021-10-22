package com.eggoz.ecommerce.view.Subscribe.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.ItemMembershipDetailBinding
import com.eggoz.ecommerce.view.Subscribe.model.Subscribe

class SubscriptionExtraAdapter(var result: List<Subscribe.Result.ExtraSubscription>) :
    RecyclerView.Adapter<SubscriptionExtraAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var listItem: View
    private var qnt = 0


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemBinding: ItemMembershipDetailBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_membership_detail, parent, false
        )
        listItem = itemBinding.root
        context = itemBinding.root.context
        return ViewHolder(itemBinding)
    }


    override fun getItemCount(): Int {
        return result.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.apply {
            if (result[position].isVisible==false)
                container.visibility= View.GONE
            txtDet.text=result[position].extra
        }



    }


    class ViewHolder(itemBinding: ItemMembershipDetailBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: ItemMembershipDetailBinding = itemBinding

    }
}