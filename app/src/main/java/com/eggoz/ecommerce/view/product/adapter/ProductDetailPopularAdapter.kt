package com.eggoz.ecommerce.view.product.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.ItemPoddetailprodBinding
import com.eggoz.ecommerce.network.model.Products
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ProductDetailPopularAdapter(
    private val result: List<Products.Result?>
) : RecyclerView.Adapter<ProductDetailPopularAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var listItem: View


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemBinding: ItemPoddetailprodBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_poddetailprod, parent, false
        )
        listItem = itemBinding.root
        context = itemBinding.root.context
        return ViewHolder(itemBinding)
    }


    override fun getItemCount(): Int {
        return result.size ?: 0
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (result[position]?.isPrimeOnline != true)
            listItem.visibility = View.GONE

        listItem.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", result[position]?.id ?: -1)
            Navigation.findNavController(holder.itemView)
                .navigate(R.id.action_nav_product_detail_to_nav_product_detail, bundle)
        }


        holder.binding.apply {
            txtPrice.text =
                "₹ ${result[position]?.currentPrice ?: "0.0".toDouble().toInt()}"
            txtHeading.text =
                "${result[position]?.name ?: ""} Eggs \n${result[position]?.description ?: ""}"

            Glide.with(context)
                .asBitmap()
                .load(result[position]?.productImage ?: "")
                .centerInside()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.logo1)
                .into(imgItem)
        }
    }

    class ViewHolder(itemBinding: ItemPoddetailprodBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: ItemPoddetailprodBinding = itemBinding

    }

}