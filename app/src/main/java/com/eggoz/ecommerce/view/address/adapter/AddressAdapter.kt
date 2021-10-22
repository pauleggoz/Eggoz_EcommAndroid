package com.eggoz.ecommerce.view.address.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.ItemAddressBinding
import com.eggoz.ecommerce.network.model.Address

class AddressAdapter(
    private var addresses: List<Address.AAddress?>
) : RecyclerView.Adapter<AddressAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var listItem: View
    private var lastCheckedPosition: Int = 0


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemBinding: ItemAddressBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_address, parent, false
        )
        listItem = itemBinding.root
        context = itemBinding.root.context
        return ViewHolder(itemBinding)
    }


    override fun getItemCount(): Int {
        return if (addresses != null) {
            addresses.size
        } else {
            0
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        listItem.setOnClickListener {
            val copyOfLastCheckedPosition = lastCheckedPosition
            lastCheckedPosition = position
            notifyItemChanged(copyOfLastCheckedPosition)
            notifyItemChanged(lastCheckedPosition)
        }

        holder.binding.apply {
            addSelect.isChecked = lastCheckedPosition == position

            addSelect.text = addresses[position]?.addressName ?: ""

            txtAddress.text =
                "${addresses[position]?.name ?: ""} ${addresses[position]?.buildingAddress ?: ""} ${addresses[position]?.streetAddress ?: ""} ${addresses[position]?.landmark ?: ""} ${addresses[position]?.city?.cityName ?: ""} " +
                        "${addresses[position]?.ecommerceSector ?: ""} ${addresses[position]?.pinCode ?: ""}"

            if (lastCheckedPosition == position)
                txtDefault.visibility = View.VISIBLE
            else
                txtDefault.visibility = View.GONE

        }
    }


    class ViewHolder(itemBinding: ItemAddressBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: ItemAddressBinding = itemBinding

    }

}