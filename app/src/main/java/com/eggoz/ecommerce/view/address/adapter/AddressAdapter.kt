package com.eggoz.ecommerce.view.address.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.databinding.ItemAddressBinding
import com.eggoz.ecommerce.network.model.Address

class AddressAdapter : ListAdapter<Address.AAddress, AddressAdapter.AddressRecyclerViewHolder>(AddressCallBack()),Opetion {
    var lastCheckedPosition: Int = 0

    class AddressRecyclerViewHolder(
        private val binding: ItemAddressBinding,
        private var callback:AddressAdapter,
        private val lastCheckedPosition: Int
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Address.AAddress, currentPos: Int) {
            binding.apply {
                root.setOnClickListener {
                    callback.reset(currentPos)
                }
                itemData = item
                viewlastCheck=lastCheckedPosition
                viewcurrentPos=currentPos


//                addSelect.isChecked = lastCheckedPosition == position

//                addSelect.text = itemData?.addressName ?: ""

                /*txtAddress.text =
                    "${itemData?.name ?: ""} ${itemData?.buildingAddress ?: ""} ${itemData?.streetAddress ?: ""} ${item?.landmark ?: ""} ${item?.city?.cityName ?: ""} " +
                            "${itemData?.ecommerceSector ?: ""} ${itemData?.pinCode ?: ""}"
*/

//                if (lastCheckedPosition == currentPos)
//                    txtDefault.visibility = View.VISIBLE
//                else
//                    txtDefault.visibility = View.GONE


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressRecyclerViewHolder {
        val binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.context))
        return AddressRecyclerViewHolder(binding,this,lastCheckedPosition)
    }

    override fun onBindViewHolder(holder: AddressRecyclerViewHolder, position: Int) {
        val currentArticle = getItem(position)
        holder.bind(currentArticle,position)
    }

    override fun reset(position: Int) {
        val copyOfLastCheckedPosition = lastCheckedPosition
        lastCheckedPosition = position
        notifyItemChanged(copyOfLastCheckedPosition)
        notifyItemChanged(lastCheckedPosition)
    }
}

interface Opetion{
    fun reset(position: Int)
}

class AddressCallBack : DiffUtil.ItemCallback<Address.AAddress>() {
    override fun areItemsTheSame(oldItem: Address.AAddress, newItem: Address.AAddress): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Address.AAddress, newItem: Address.AAddress): Boolean =
        oldItem.isSelected == newItem.isSelected

}


/*(
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

}*/
