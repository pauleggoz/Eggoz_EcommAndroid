package com.eggoz.ecommerce.view.address.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.databinding.ItemAddressBinding
import com.eggoz.ecommerce.network.model.Address
import com.eggoz.ecommerce.network.model.CityData

class AddressAdapter(private val mycallback: (Address.AAddress?, Int) -> Unit) :
    ListAdapter<Address.AAddress, AddressAdapter.AddressRecyclerViewHolder>(AddressCallBack()),
    Opetion {
    var lastSeletedLoc: Int = 0

    class AddressRecyclerViewHolder(
        private val binding: ItemAddressBinding,
        private val fcallback: (Address.AAddress?, Int) -> Unit,
        private var callback: AddressAdapter
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Address.AAddress, currentPos: Int) {
            binding.apply {

                itemData = item

                addSelect.isChecked = item.isSelected

                /*if (item.isSelected)
                    txtDefault.visibility = View.VISIBLE*/

                addSelect.setOnCheckedChangeListener { compoundButton, b ->
                    if (b) {
                        callback.reset(currentPos)

                        fcallback(item, currentPos)
                        addSelect.isSelected = item.isSelected == true
                    }
                }


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressRecyclerViewHolder {
        val binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.context))
        return AddressRecyclerViewHolder(binding, fcallback = mycallback, this)
    }

    override fun onBindViewHolder(holder: AddressRecyclerViewHolder, position: Int) {
        val currentArticle = getItem(position)
        holder.bind(currentArticle, position)
    }

    override fun reset(position: Int) {
        currentList[lastSeletedLoc]?.isSelected = false
        notifyItemChanged(lastSeletedLoc)
        lastSeletedLoc = position
        currentList[position]?.isSelected = true
    }
}

interface Opetion {
    fun reset(position: Int)
}

class AddressCallBack : DiffUtil.ItemCallback<Address.AAddress>() {
    override fun areItemsTheSame(oldItem: Address.AAddress, newItem: Address.AAddress): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Address.AAddress, newItem: Address.AAddress): Boolean =
        oldItem.isSelected == newItem.isSelected

}
