package com.eggoz.ecommerce.view.starter.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.ItemCityBinding
import com.eggoz.ecommerce.network.model.CityData

class CityAdapter(private val mycallback: (CityData.Result) -> Unit):ListAdapter<CityData.Result, CityAdapter.CityRecyclerViewHolder>(
        CityCallBack()
    ) {

    class CityRecyclerViewHolder(
        private val binding: ItemCityBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CityData.Result,callback: (CityData.Result) -> Unit) {
            binding.apply {
                itemdata=item

                root.setOnClickListener {
                    callback(item)
                    val bundle = Bundle()
                    bundle.putInt("id", item.id ?: -1)
                    Navigation.findNavController(img)
                        .navigate(R.id.nav_loc, bundle)
                }

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CityRecyclerViewHolder {
        val binding = ItemCityBinding.inflate(LayoutInflater.from(parent.context))
        return CityRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityRecyclerViewHolder, position: Int) {
        val currentArticle = getItem(position)
        holder.bind(currentArticle,mycallback)
    }


}

class CityCallBack : DiffUtil.ItemCallback<CityData.Result>() {
    override fun areItemsTheSame(oldItem: CityData.Result, newItem: CityData.Result): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: CityData.Result, newItem: CityData.Result): Boolean =
        oldItem.zoneName == newItem.zoneName

}