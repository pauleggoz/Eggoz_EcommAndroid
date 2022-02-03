package com.eggoz.ecommerce.view.subscribe.adapter

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
import com.eggoz.ecommerce.databinding.ItemSubscribeBinding
import com.eggoz.ecommerce.view.subscribe.SubscribeDetailFragment
import com.eggoz.ecommerce.view.subscribe.model.Subscribe

class SubscribeAdapter(
    val results: List<Subscribe.Result>,
    val callback: SubscribeDetailFragment
) :
    RecyclerView.Adapter<SubscribeAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var listItem: View


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemBinding: ItemSubscribeBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_subscribe, parent, false
        )
        listItem = itemBinding.root
        context = itemBinding.root.context
        return ViewHolder(itemBinding)
    }



    override fun getItemCount(): Int {
        return results.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var id: Int = -1
        var price: Double = 0.0


        holder.binding.apply {
            txtTitle.text=results[position].name

            recDetbenefit.apply {
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(context)
                itemAnimator = DefaultItemAnimator()
                isNestedScrollingEnabled = false
                if (results[position].benefitSubscription!=null)
                adapter = SubscriptionBenefitAdapter(result = results[position].benefitSubscription!!)
            }
            recDetextra.apply {
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(context)
                itemAnimator = DefaultItemAnimator()
                isNestedScrollingEnabled = false
                if (results[position].extraSubscription!=null)
                adapter = SubscriptionExtraAdapter(result = results[position].extraSubscription!!)
            }
        }

    }


    class ViewHolder(itemBinding: ItemSubscribeBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: ItemSubscribeBinding = itemBinding

    }
}