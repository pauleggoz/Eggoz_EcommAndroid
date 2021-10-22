package com.eggoz.ecommerce.view.profile.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.ItemManageAddressBinding
import com.eggoz.ecommerce.network.model.Address
import com.eggoz.ecommerce.view.profile.ManageAddressFragment

class ManageAdderssesAdapter(val result:List<Address.AAddress>,val callbacks: ManageAddressFragment) :
    RecyclerView.Adapter<ManageAdderssesAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var listItem: View
    private var select = 0


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemBinding: ItemManageAddressBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_manage_address, parent, false
        )
        listItem = itemBinding.root
        context = itemBinding.root.context
        return ViewHolder(itemBinding)
    }


    override fun getItemCount(): Int {
        return if (result!=null)
            result.size
        else
            0
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.apply {
            txtTitle.text=result[position].addressName
            if (select==position)
                txtStatus.visibility=View.VISIBLE
            else txtStatus.visibility=View.GONE
            txtTitle.text=result[position].addressName
            txtMobile.text="Mobile: ${result[position].phoneNo}"
            txtAddress.text="${result[position].buildingAddress} ${result[position].streetAddress} ${result[position].landmark} ${result[position].city?.cityName ?:""} ${result[position].city?.state ?:""} - ${result[position].pinCode}"
            txtEditAddress.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("id", result[position].id ?: -1)
                bundle.putString("addressName", result[position].addressName ?: "")
                bundle.putString("Mobile", result[position].phoneNo ?: "")
                bundle.putString("buildingAddress", result[position].buildingAddress ?: "")
                bundle.putString("streetAddress", result[position].streetAddress ?: "")
                bundle.putString("landmark", result[position].landmark ?: "")
                bundle.putString("cityName", result[position].city?.cityName ?: "")

                bundle.putInt("cityid",result[position].ecommerceSector?.cluster ?: -1)
                bundle.putInt("stateid",result[position].ecommerceSector?.city ?: -1)

                Log.d("data","adapter stateid ${result[position].ecommerceSector?.cluster ?: -1} city${result[position].ecommerceSector?.city ?: -1 }")

                bundle.putInt("pinCode", result[position].pinCode ?: 0)
                Navigation.findNavController(listItem)
                    .navigate(R.id.action_nav_manageAddress_to_nav_inputAddress, bundle)
            }
            txtDeleteAddress.setOnClickListener {
                callbacks.deleteAddress( result[position].id ?: -1)
            }


        }
        holder.itemView.setOnClickListener {
            select=position
            notifyDataSetChanged()
        }
    }


    class ViewHolder(itemBinding: ItemManageAddressBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val binding: ItemManageAddressBinding = itemBinding

    }
    interface callback{
        fun deleteAddress(id:Int)
    }
}