package com.eggoz.ecommerce.view.home.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.ItemPopprodBinding
import com.eggoz.ecommerce.network.model.Products

class ProductPopularAdapter :
    ListAdapter<Products.Result, ProductPopularAdapter.ProductPopularRecyclerViewHolder>(
        ProductPopularCallBack()
    ) {

    class ProductPopularRecyclerViewHolder(
        private val binding: ItemPopprodBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Products.Result) {
            binding.apply {

                itemdata = item

                if (item.isPrimeOnline != true) {
                    root.visibility = View.GONE
                    catMain.visibility = View.GONE
                    root.layoutParams = RecyclerView.LayoutParams(0, 0)
                }

                root.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putInt("id", item.id ?: -1)
                    Navigation.findNavController(root)
                        .navigate(R.id.action_nav_home_to_nav_product_detail, bundle)
                }

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductPopularRecyclerViewHolder {
        val binding = ItemPopprodBinding.inflate(LayoutInflater.from(parent.context))
        return ProductPopularRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductPopularRecyclerViewHolder, position: Int) {
        val currentArticle = getItem(position)
        holder.bind(currentArticle)
    }


}

class ProductPopularCallBack : DiffUtil.ItemCallback<Products.Result>() {
    override fun areItemsTheSame(oldItem: Products.Result, newItem: Products.Result): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Products.Result, newItem: Products.Result): Boolean =
        oldItem.name == newItem.name

}
/*(val result: List<Products.Result?>
) : RecyclerView.Adapter<ProductPopularAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var listItem: View

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemBinding: ItemPopprodBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_popprod, parent, false
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


        if (result[position]?.isPrimeOnline ?: false != true) {
            listItem.visibility = View.GONE
            holder.binding.catMain.visibility=View.GONE
            listItem.layoutParams = RecyclerView.LayoutParams(0, 0)
        }*//*else{
            listItem.visibility = View.VISIBLE;
            listItem.layoutParams =
                RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }*//*

        holder.binding.apply {

            val df = DecimalFormat("#.##")
            val  price=df.format(result[position]?.currentPrice?.toDouble() ?:0.00)
            txtPrice.text =
                "₹ ${price}"
            txtHeading.text =
                "${result[position]?.name ?: ""} ${result[position]?.sKUCount ?: 0} pcs"

            Glide.with(context)
                .asBitmap()
                .load(result[position]?.productImage ?: "")
                .centerInside()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.logo1)
                .into(imgItem)

            listItem.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("id", result[position]?.id ?: -1)
                Navigation.findNavController(listItem)
                    .navigate(R.id.action_nav_home_to_nav_product_detail, bundle)
            }
        }
    }

    class ViewHolder(itemBinding: ItemPopprodBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: ItemPopprodBinding = itemBinding

    }

}*/