package com.eggoz.ecommerce.view.cart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.databinding.ItemCartlistBinding
import com.eggoz.ecommerce.room.RoomCart

class RoomCartAdapter(private val callback: (RoomCart, Int) -> Unit) :
    ListAdapter<RoomCart, RoomCartAdapter.RoomCartRecyclerViewHolder>(RoomCartCallBack()) {

    class RoomCartRecyclerViewHolder(
        private val binding: ItemCartlistBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private var qnt = 0

        fun bind(item: RoomCart,callback: (RoomCart, Int) -> Unit) {
            qnt = item.quantaty ?: 1

            binding.apply {
                itemData = item
                qntval = qnt

                btnInc.setOnClickListener {
                    qnt = txtQntVal.text.toString().toInt()
                    qnt++
                    callback(item,qnt)
                  /*  callback.updateCart(
                        id = item.id,
                        qnt = qnt,
                        price = item.price
                    )*/
                }
                btnDec.setOnClickListener {

                    if (qnt != 0) {
                        qnt--
                    }
                    if (qnt >= 0) {
                        callback(item,qnt)
                       /* callback.updateCart(
                            id = item.id,
                            qnt = qnt,
                            price = item.price
                        )*/
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomCartRecyclerViewHolder {
        val binding = ItemCartlistBinding.inflate(LayoutInflater.from(parent.context))
        return RoomCartRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoomCartRecyclerViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data,callback)
    }

}

class RoomCartCallBack : DiffUtil.ItemCallback<RoomCart>() {
    override fun areItemsTheSame(oldItem: RoomCart, newItem: RoomCart): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: RoomCart, newItem: RoomCart): Boolean =
        oldItem.quantaty == newItem.quantaty

}


/*
(
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
}*/
