package com.eggoz.ecommerce.view.product.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.ItemTablistBinding

class TabLayoutAdapter(
) : RecyclerView.Adapter<TabLayoutAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var listItem: View
    private var size: Int=3
    private var result:ArrayList<String> = ArrayList()


    fun updatedata(result:ArrayList<String>){
        this.size=result.size
        this.result=result
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemBinding: ItemTablistBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_tablist, parent, false
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
            tag.text = result[position] ?: ""
        }
    }


    class ViewHolder(itemBinding: ItemTablistBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: ItemTablistBinding = itemBinding

    }

}