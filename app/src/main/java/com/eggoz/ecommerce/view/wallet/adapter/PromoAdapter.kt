package com.eggoz.ecommerce.view.wallet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.databinding.ItemPromoBinding
import com.eggoz.ecommerce.network.model.WalletPromo

class PromoAdapter :
    ListAdapter<WalletPromo.Result?, PromoAdapter.PromoRecyclerViewHolder>(
        PromoCallBack()
    ) {

    class PromoRecyclerViewHolder(
        private val binding: ItemPromoBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: WalletPromo.Result?
        ) {
            binding.apply {
                promo=item?.promo?.toDouble()?.toInt() ?:0
                amount=item?.amount?.toDouble()?.toInt()?:0
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PromoRecyclerViewHolder {
        val binding = ItemPromoBinding.inflate(LayoutInflater.from(parent.context))
        return PromoRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PromoRecyclerViewHolder, position: Int) {
        val currentArticle = getItem(position)
        holder.bind(currentArticle)
    }


}

class PromoCallBack : DiffUtil.ItemCallback<WalletPromo.Result?>() {

    override fun areItemsTheSame(
        oldItem: WalletPromo.Result,
        newItem: WalletPromo.Result
    ): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(
        oldItem: WalletPromo.Result,
        newItem: WalletPromo.Result
    ): Boolean =
        oldItem.promo == newItem.promo

}

/*























    (var result: List<WalletPromo.Result>
) : RecyclerView.Adapter<PromoAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var listItem: View


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemBinding: ItemPromoBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_promo, parent, false
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

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            txtDis.text="Get ₹ ${result[position].promo?.toDouble()?.toInt() ?:0} extra on adding ₹ ${result[position].amount?.toDouble()?.toInt()?:0} & above"
        }
    }


    class ViewHolder(itemBinding: ItemPromoBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: ItemPromoBinding = itemBinding

    }

}*/
