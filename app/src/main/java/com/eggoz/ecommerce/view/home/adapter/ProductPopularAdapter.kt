package com.eggoz.ecommerce.view.home.adapter

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
import com.eggoz.ecommerce.databinding.ItemPopprodBinding
import com.eggoz.ecommerce.network.model.Products
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.text.DecimalFormat

class ProductPopularAdapter(val result: List<Products.Result?>
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
        }/*else{
            listItem.visibility = View.VISIBLE;
            listItem.layoutParams =
                RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }*/

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

}