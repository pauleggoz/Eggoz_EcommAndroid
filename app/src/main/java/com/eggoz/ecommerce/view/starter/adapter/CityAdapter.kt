package com.eggoz.ecommerce.view.starter.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.databinding.ItemCityBinding
import com.eggoz.ecommerce.network.model.CityData
import kotlinx.coroutines.launch

class CityAdapter(var result:List<CityData.Result?>, var lifecycleScope: LifecycleCoroutineScope, var userPreferences: UserPreferences
) : RecyclerView.Adapter<CityAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var listItem: View


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemBinding: ItemCityBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_city, parent, false
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
            txtDes.text = result[position]?.zoneName ?: ""

            listItem.setOnClickListener {
                lifecycleScope.launch {
                    userPreferences.saveciy(city = result[position]?.id ?: -1)
                }
                val bundle = Bundle()
                bundle.putInt("id", result[position]?.id ?: -1)
                Navigation.findNavController(img)
                    .navigate(R.id.nav_loc, bundle)
            }
        }
    }


    class ViewHolder(itemBinding: ItemCityBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: ItemCityBinding = itemBinding

    }

}