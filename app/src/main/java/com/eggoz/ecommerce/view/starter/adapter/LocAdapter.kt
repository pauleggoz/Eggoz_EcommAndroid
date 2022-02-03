package com.eggoz.ecommerce.view.starter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.databinding.ItemLocBinding
import com.eggoz.ecommerce.network.model.CityData
import com.eggoz.ecommerce.view.address.adapter.Opetion

class LocAdapter(private val mycallback: (CityData.Result.City.EcommerceSector?) -> Unit) :
    ListAdapter<CityData.Result.City.EcommerceSector?, LocAdapter.LocRecyclerViewHolder>(
        LocCallBack()
    ), Opetion {

    private var lastCheckedPosition: Int = 0

    class LocRecyclerViewHolder(
        private val binding: ItemLocBinding,
        private var adaptercallback: LocAdapter,
        private var lastCheckedPosition: Int
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: CityData.Result.City.EcommerceSector?,
            callback: (CityData.Result.City.EcommerceSector?) -> Unit,
            currentPos: Int
        ) {
            binding.apply {

                itemdata = item
                seletedPos = lastCheckedPosition
                viewcurrentPos = currentPos

                locSelect.setOnCheckedChangeListener { compoundButton, b ->
                    if (b){
                        adaptercallback.reset(position = currentPos)
                        callback(item)
                    }
                }


            /*    locSelect.setOnClickListener {
                    adaptercallback.reset(position = currentPos)
                    callback(item)
                }*/


            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocRecyclerViewHolder {
        val binding = ItemLocBinding.inflate(LayoutInflater.from(parent.context))
        return LocRecyclerViewHolder(binding, this, lastCheckedPosition)
    }

    override fun onBindViewHolder(holder: LocRecyclerViewHolder, position: Int) {
        val currentArticle = getItem(position)
        holder.bind(currentArticle, mycallback, position)
    }

    override fun reset(position: Int) {
        val copyOfLastCheckedPosition = lastCheckedPosition
        lastCheckedPosition = position
        notifyItemChanged(copyOfLastCheckedPosition)
        notifyItemChanged(lastCheckedPosition)
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
        oldItem.sectorName == newItem.sectorName

}


/*



(
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

}*/
