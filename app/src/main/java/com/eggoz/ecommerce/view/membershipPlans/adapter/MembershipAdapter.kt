package com.eggoz.ecommerce.view.membershipPlans.adapter

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
import com.eggoz.ecommerce.view.membershipPlans.MembershipPlansFragment
import com.eggoz.ecommerce.view.membershipPlans.model.Membership

class MembershipAdapter(
    val results: List<Membership.Result>,
    val callback: MembershipPlansFragment,
    var months: Int
) :
    RecyclerView.Adapter<MembershipAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var listItem: View


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemBinding: ItemMembershipBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_membership, parent, false
        )
        listItem = itemBinding.root
        context = itemBinding.root.context
        return ViewHolder(itemBinding)
    }


    fun setmMonths(mmonths: Int) {
        months = mmonths
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return results.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var id: Int = -1
        var price: Double = 0.0


        holder.binding.apply {

            recDet.apply {
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(context)
                itemAnimator = DefaultItemAnimator()
                isNestedScrollingEnabled = false
                adapter = MembershipDetailAdapter(result = results[position].benefitMembership!!)
            }
        }

        holder.binding.apply {

            if (results[position].dataMembership != null)
                for (i in results[position].dataMembership?.indices!!) {
                    if (months == results[position].dataMembership!![i].months) {
                        id=results[position].dataMembership!![i].id ?:-1
                        price=results[position].dataMembership!![i].rate?.toDouble() ?:0.0
                        txtPrice.text = "₹ ${
                            results[position].dataMembership!![i].rate?.toDouble()?.toInt() ?: 0
                        }"
                    }
                }
            txtType.text = results[position].name

            btnSubmit.setOnClickListener { callback.selectmambership(id, price) }
        }

    }


    class ViewHolder(itemBinding: ItemMembershipBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: ItemMembershipBinding = itemBinding

    }
}