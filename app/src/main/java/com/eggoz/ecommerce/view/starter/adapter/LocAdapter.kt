package com.eggoz.ecommerce.view.starter.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.ItemLocBinding
import com.eggoz.ecommerce.network.model.CityData
import com.eggoz.ecommerce.view.starter.LocalityFragment

class LocAdapter(
    var result: List<CityData.Result.City.EcommerceSector?>,
    var contextloc: LocalityFragment
) : RecyclerView.Adapter<LocAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var listItem: View
    private var lastCheckedPosition: Int = 0


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemBinding: ItemLocBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_loc, parent, false
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.apply {

            locSelect.setOnClickListener {
                contextloc.locId(id = result[position]?.id ?: -1)
                val copyOfLastCheckedPosition = lastCheckedPosition
                lastCheckedPosition = position
                notifyItemChanged(copyOfLastCheckedPosition)
                notifyItemChanged(lastCheckedPosition)
            }

            locSelect.isChecked = lastCheckedPosition == position

            locSelect.text = result[position]?.sectorName ?: ""
        }

    }
    class ViewHolder(itemBinding: ItemLocBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: ItemLocBinding = itemBinding

    }

}