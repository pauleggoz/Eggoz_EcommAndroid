package com.eggoz.ecommerce.view.cart.adapter

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.ItemCartlistBinding
import com.eggoz.ecommerce.localdata.room.RoomCart

class RoomCartAdapter(
    private val updateqNT: (RoomCart, Int) -> Unit,
    private val deleteCart: (RoomCart) -> Unit
) :
    ListAdapter<RoomCart, RoomCartAdapter.RoomCartRecyclerViewHolder>(RoomCartCallBack()) {

    class RoomCartRecyclerViewHolder(
        private val binding: ItemCartlistBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private var qnt = 0

        fun bind(
            item: RoomCart, updateqNT: (RoomCart, Int) -> Unit,
            deleteCart: (RoomCart) -> Unit
        ) {
            qnt = item.quantaty ?: 1
            val vibe = binding.root.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

            binding.apply {
                itemData = item
                qntval = qnt

                btnInc.setOnClickListener {
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibe?.vibrate(VibrationEffect.createOneShot(20, 10))
                    } else {
                        vibe?.vibrate(20)
                    }
                    ++qnt;
                    updateqNT(item, qnt)
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
                        updateqNT(item, qnt)
                        binding.qntval = qnt
                    } else {
                        btnAdd.isEnabled = true
                        deleteCart(item)
                        qnt = 0
                        binding.qntval = qnt
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
        holder.bind(data, updateqNT, deleteCart)
    }

}

class RoomCartCallBack : DiffUtil.ItemCallback<RoomCart>() {
    override fun areItemsTheSame(oldItem: RoomCart, newItem: RoomCart): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: RoomCart, newItem: RoomCart): Boolean =
        oldItem.quantaty == newItem.quantaty

}
