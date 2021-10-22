package com.eggoz.ecommerce.view.product.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.ItemProductBinding
import com.eggoz.ecommerce.network.model.Products
import com.eggoz.ecommerce.view.product.ProductFragment
import java.text.DecimalFormat


class ProductAdapter(val callback: ProductFragment, var result: List<Products.Result?>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var listItem: View
    private var qnt = 0


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemBinding: ItemProductBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_product, parent, false
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
        val vibe = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?


        listItem.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", result[position]?.id ?: -1)
            Navigation.findNavController(listItem)
                .navigate(R.id.action_nav_product_to_nav_product_detail, bundle)
        }

        holder.binding.apply {
            btnSubsc.setOnClickListener {
                Navigation.findNavController(root)
                    .navigate(R.id.nav_subscribe)
            }
            if (!(result[position]?.isStockAvailableOnline == true)){
                imgOderStatus.setImageResource(R.drawable.outofstock)
            }else if (result[position]?.isPrimeOnline == true){
                imgOderStatus.setImageResource(R.drawable.prime)
            }else if (result[position]?.isNewOnline == true){
                imgOderStatus.setImageResource(R.drawable.newpro)
            }else imgOderStatus.visibility=View.GONE


            val df = DecimalFormat("#.##")
            val  price=df.format(result[position]?.currentPrice?.toDouble() ?: 0.00)
            holder.binding.txtPrice.text =
                "₹ ${price}"
            txtQnt.text = "${result[position]?.sKUCount ?: 0} pieces"
            txtUnit.text = "Net Wt:"
            txtUnitValue.text =
                " ${result[position]?.skuWeight?.toDouble()?.toInt() ?: 0} ${result[position]?.weightType ?: ""}"
            txtTitle.text = result[position]?.description ?: ""

            Glide.with(context)
                .asBitmap()
                .load(result[position]?.productImage ?: "")
                .centerInside()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.logo1)
                .into(img)


            var cqnt:Int =0

            cqnt = callback.qntCart(result[position]?.id ?: -1)

            if (cqnt>0){

                txtbuy.visibility = View.GONE
                btnDec.visibility = View.VISIBLE
                btnInc.visibility = View.VISIBLE
                txtQntVal.visibility = View.VISIBLE
                txtsub.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.app_color
                    )
                )
                btnAdd.background =
                    ContextCompat.getDrawable(context, R.drawable.curvedbackgrayborder)
                txtQntVal.text = cqnt.toString()
            }



            btnAdd.setOnClickListener {

                if (Build.VERSION.SDK_INT >= 26) {
                    vibe?.vibrate(VibrationEffect.createOneShot(20,10))
                } else {
                    vibe?.vibrate(20)
                }


                txtbuy.visibility = View.GONE
                btnDec.visibility = View.VISIBLE
                btnInc.visibility = View.VISIBLE
                txtQntVal.visibility = View.VISIBLE


                Log.d("data", "qnt $cqnt")

                callback.itemclick(
                    id = result[position]?.id ?: -1,
                    name = result[position]?.description + " ",
                    Image = result[position]?.productImage ?: "",
                    Priceval = result[position]?.ecommercePrice ?: "-1",
                    qnt = ++cqnt,
                    des = "",
                    status = result[position]?.isAvailable ?: false,
                    sKUCount = result[position]?.sKUCount ?:0
                )

                txtQntVal.text = "1"
                txtsub.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.app_color
                    )
                )
                btnAdd.background =
                    ContextCompat.getDrawable(context, R.drawable.curvedbackgrayborder)
                txtQntVal.text = cqnt.toString()

            }
            txtDis.text = "10% OFF"


            btnInc.setOnClickListener {

                if (Build.VERSION.SDK_INT >= 26) {
                    vibe?.vibrate(VibrationEffect.createOneShot(20,10))
                } else {
                    vibe?.vibrate(20)
                }



                qnt = txtQntVal.text.toString().toInt()
                Log.d("TAG", "onBindViewHolder: $qnt")
                qnt++
                /*callback.incCart(
                    id = result[position]?.id ?: -1
                )*/
                callback.updateqNT(id = result[position]?.id ?: -1,qnt)

                Log.d("TAG", "onBindViewHolder: $qnt")
                cqnt = callback.qntCart(result[position]?.id ?: -1)

                txtQntVal.text = cqnt.toString()
            }

            btnDec.setOnClickListener {

                if (Build.VERSION.SDK_INT >= 26) {
                    vibe?.vibrate(VibrationEffect.createOneShot(20,10))
                } else {
                    vibe?.vibrate(20)
                }



                qnt = txtQntVal.text.toString().toInt()
                qnt--
               /* if (qnt != 0) {
                    qnt--
                    txtQntVal.text = qnt.toString()
                } else {
                    txtQntVal.text = "00"
                }*/
                if (qnt > 0) {
                    callback.updateCart(
                        id = result[position]?.id ?: -1,
                        qnt = qnt,
                        price = result[position]?.ecommercePrice ?: "0"
                    )


                    cqnt = callback.qntCart(result[position]?.id ?: -1)

                    txtQntVal.text = cqnt.toString()
                } else {
                    callback.deleteCart(id = result[position]?.id ?: -1)

                    txtQntVal.text = "00"

                    txtbuy.visibility = View.VISIBLE
                    btnDec.visibility = View.GONE
                    btnInc.visibility = View.GONE
                    txtQntVal.visibility = View.GONE

                }
                notifyDataSetChanged()
            }

        }
    }


    class ViewHolder(itemBinding: ItemProductBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: ItemProductBinding = itemBinding

    }
}