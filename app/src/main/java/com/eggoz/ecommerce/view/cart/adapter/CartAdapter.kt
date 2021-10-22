package com.eggoz.ecommerce.view.cart.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.ItemCartlistBinding
import com.eggoz.ecommerce.room.RoomCart
import com.eggoz.ecommerce.view.cart.CartListFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class CartAdapter(
    var result: List<RoomCart>, private var callbak: CartListFragment
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var listItem: View
    private var qnt = 0


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemBinding: ItemCartlistBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_cartlist, parent, false
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
        qnt = result[position].quantaty!!
        val dis= (result[position].price?.toDouble())?.div(4.0) ?:0.0
        val disPrice= result[position].price?.toDouble()?.plus(dis) ?:0.0
        holder.binding.apply {
            txtDisprice.paintFlags =
                txtDisprice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            txtDisprice.text = "₹ ${disPrice}"
            txtPrice.text = "₹ ${result[position].price?.toDouble() ?:0.0}"
            txtTitle.text = result[position].name

            txtQnt.text="${result[position].sKUCount} pieces"


            txtQntVal.text = result[position].quantaty.toString()

            Glide.with(context)
                .asBitmap()
                .load(result[position].img)
                .centerInside()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.logo1)
                .into(img)

            btnInc.setOnClickListener {
                qnt = txtQntVal.text.toString().toInt()
                qnt++
                txtQntVal.text = qnt.toString()
                callbak.updateCart(
                    id = result[position].id,
                    qnt = qnt,
                    price = result[position].price ?: "0"
                )
            }

            btnDec.setOnClickListener {
                if (callbak.CartSize() == 0)
                    Navigation.findNavController(btnDec).navigate(R.id.nav_product)

                qnt = txtQntVal.text.toString().toInt()
                if (qnt != 0) {
                    qnt--
                    txtQntVal.text = qnt.toString()
                } else {
                    txtQntVal.text = "00"
                }
                if (qnt > 0) {
                    qnt = txtQntVal.text.toString().toInt()
                    callbak.updateCart(
                        id = result[position].id,
                        qnt = qnt,
                        price = result[position].price ?: "0"
                    )
                } else {
                    callbak.deleteCart(id = result[position].id)
                    notifyDataSetChanged()

                }
            }

        }
    }

    class ViewHolder(itemBinding: ItemCartlistBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: ItemCartlistBinding = itemBinding

    }
}