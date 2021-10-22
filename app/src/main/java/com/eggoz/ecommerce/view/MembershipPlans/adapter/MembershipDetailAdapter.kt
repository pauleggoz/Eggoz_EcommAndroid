package com.eggoz.ecommerce.view.MembershipPlans.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.ItemMembershipBinding
import com.eggoz.ecommerce.databinding.ItemMembershipDetailBinding
import com.eggoz.ecommerce.network.model.Products
import com.eggoz.ecommerce.view.MembershipPlans.model.Membership
import com.eggoz.ecommerce.view.product.ProductFragment

class MembershipDetailAdapter(var result: List<Membership.Result.BenefitMembership>) :
    RecyclerView.Adapter<MembershipDetailAdapter.ViewHolder>() {
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
            if (result[position].isVisible!=null)
            if (result[position].isVisible==false)
            container.visibility=View.GONE
            txtDet.text=result[position].benefit
        }



    }


    class ViewHolder(itemBinding: ItemMembershipDetailBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: ItemMembershipDetailBinding = itemBinding

    }
}