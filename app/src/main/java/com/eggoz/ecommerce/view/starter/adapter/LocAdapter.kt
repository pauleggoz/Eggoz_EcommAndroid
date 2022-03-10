package com.eggoz.ecommerce.view.starter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.databinding.ItemLocBinding
import com.eggoz.ecommerce.network.model.CityData
import com.eggoz.ecommerce.view.address.adapter.Opetion

class LocAdapter(private val mycallback: (CityData.Result.City.EcommerceSector?, Int) -> Unit) :
    ListAdapter<CityData.Result.City.EcommerceSector?, LocAdapter.LocRecyclerViewHolder>(
        LocCallBack()
    ), Opetion {

    var lastSeletedLoc=0


    class LocRecyclerViewHolder(
        private val binding: ItemLocBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: CityData.Result.City.EcommerceSector?,
            callback: (CityData.Result.City.EcommerceSector?, Int) -> Unit,
            currentPos: Int,
            adaCall:LocAdapter
        ) {
            binding.apply {

                itemdata = item

                locSelect.isChecked = item?.isSelected ?: false


                locSelect.setOnCheckedChangeListener { compoundButton, b ->
                    if (b) {
                        adaCall.reset(currentPos)
                        callback(item, currentPos)
                        locSelect.isSelected = item?.isSelected == true
                    }
                }


            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocRecyclerViewHolder {
        val binding = ItemLocBinding.inflate(LayoutInflater.from(parent.context))
        return LocRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocRecyclerViewHolder, position: Int) {
        val currentArticle = getItem(position)
        holder.bind(currentArticle, mycallback, position,this)
    }

    override fun reset(position: Int) {
        currentList[lastSeletedLoc]?.isSelected=false
        notifyItemChanged(lastSeletedLoc)
        lastSeletedLoc =position
        currentList[position]?.isSelected=true
    }


}

class LocCallBack : DiffUtil.ItemCallback<CityData.Result.City.EcommerceSector?>() {

    override fun areItemsTheSame(
        oldItem: CityData.Result.City.EcommerceSector,
        newItem: CityData.Result.City.EcommerceSector
    ): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(
        oldItem: CityData.Result.City.EcommerceSector,
        newItem: CityData.Result.City.EcommerceSector
    ): Boolean =
        oldItem.isSelected == newItem.isSelected

}


