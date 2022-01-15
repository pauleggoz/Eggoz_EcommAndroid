package com.eggoz.ecommerce.view.product.adapter

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.ItemProductBinding
import com.eggoz.ecommerce.network.model.Products
import com.eggoz.ecommerce.view.product.ProductFragment


class ProductAdapter(val mycallback: ProductFragment) :
    ListAdapter<Products.Result, ProductAdapter.ProductRecyclerViewHolder>(ProductCallBack()) {

    class ProductRecyclerViewHolder(
        private val binding: ItemProductBinding,
        private val mycallback: ProductFragment
    ) : RecyclerView.ViewHolder(binding.root) {

        private var qnt = 0
        val context: Context = binding.root.context
        val vibe = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

        fun bind(item: Products.Result) {
            binding.apply {
                itemdata = item
                root.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putInt("id", item.id ?: -1)
                    Navigation.findNavController(root)
                        .navigate(R.id.action_nav_product_to_nav_product_detail, bundle)
                }
                btnSubsc.setOnClickListener {
                    Navigation.findNavController(root)
                        .navigate(R.id.nav_subscribe)
                }

                if (!(item.isStockAvailableOnline == true)) {
                    imgOderStatus.setImageResource(R.drawable.outofstock)
                } else if (item.isPrimeOnline == true) {
                    imgOderStatus.setImageResource(R.drawable.prime)
                } else if (item.isNewOnline == true) {
                    imgOderStatus.setImageResource(R.drawable.newpro)
                } else imgOderStatus.visibility = View.GONE

                qnt = mycallback.qntCart(item.id ?: -1)

                if (qnt > 0) {
                    txtsub.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.app_color
                        )
                    )
                    btnAdd.background =
                        ContextCompat.getDrawable(root.context, R.drawable.curvedbackgrayborder)
                    binding.qntval = qnt
                }



                btnAdd.setOnClickListener {
                    btnAdd.isEnabled=false

                    if (Build.VERSION.SDK_INT >= 26) {
                        vibe?.vibrate(VibrationEffect.createOneShot(20, 10))
                    } else {
                        vibe?.vibrate(20)
                    }

                    qnt=1
                    mycallback.itemclick(
                        id = item.id ?: -1,
                        name = item.description + " ",
                        Image = item.productImage ?: "",
                        Priceval = item.ecommercePrice ?: "-1",
                        qnt = qnt,
                        des = "",
                        status = item.isAvailable ?: false,
                        sKUCount = item.sKUCount ?: 0
                    )
                    txtsub.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.app_color
                        )
                    )
                    btnAdd.background =
                        ContextCompat.getDrawable(root.context, R.drawable.curvedbackgrayborder)
                    binding.qntval = qnt

                }

                btnInc.setOnClickListener {

                    if (Build.VERSION.SDK_INT >= 26) {
                        vibe?.vibrate(VibrationEffect.createOneShot(20, 10))
                    } else {
                        vibe?.vibrate(20)
                    }
                    ++qnt;
                    mycallback.updateqNT(id = item.id ?: -1, qnt)
                    binding.qntval = qnt
                }

                btnDec.setOnClickListener {

                    if (Build.VERSION.SDK_INT >= 26) {
                        vibe?.vibrate(VibrationEffect.createOneShot(20, 10))
                    } else {
                        vibe?.vibrate(20)
                    }
                    qnt--

                    if (qnt > 0) {
                        mycallback.updateCart(
                            id = item.id ?: -1,
                            qnt = qnt,
                            price = item.ecommercePrice ?: "0"
                        )
                        binding.qntval = qnt
                    } else {
                        btnAdd.isEnabled=true
                        mycallback.deleteCart(id = item.id ?: -1)
                        qnt=0
                        binding.qntval = qnt
                        btnAdd.background =
                            ContextCompat.getDrawable(root.context, R.drawable.curvedback)
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductRecyclerViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context))
        return ProductRecyclerViewHolder(binding, mycallback)
    }

    override fun onBindViewHolder(holder: ProductRecyclerViewHolder, position: Int) {
        val currentArticle = getItem(position)
        holder.bind(currentArticle)
    }


}

class ProductCallBack : DiffUtil.ItemCallback<Products.Result>() {
    override fun areItemsTheSame(oldItem: Products.Result, newItem: Products.Result): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Products.Result, newItem: Products.Result): Boolean =
        oldItem.name == newItem.name

}
