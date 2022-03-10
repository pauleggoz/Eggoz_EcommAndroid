package com.eggoz.ecommerce.view.cart.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.ViewModelFactory
import com.eggoz.ecommerce.databinding.FragmentCartListBinding
import com.eggoz.ecommerce.localdata.room.MyDatabase
import com.eggoz.ecommerce.localdata.room.RoomCart
import com.eggoz.ecommerce.view.cart.adapter.RoomCartAdapter
import com.eggoz.ecommerce.view.cart.viewmodel.CartProductViewModel
import com.eggoz.ecommerce.view.cart.viewmodel.ProductRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CartListFragment : Fragment() {

    private lateinit var binding: FragmentCartListBinding

    private lateinit var cartAdapter: RoomCartAdapter
    private lateinit var viewModel: CartProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartListBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        val cartDoa = MyDatabase.getInstance(context = activity?.application!!).deatailcart
        val repository = ProductRepository(cartDoa)
        val viewmodelFat = ViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewmodelFat)[CartProductViewModel::class.java]

        cartAdapter = RoomCartAdapter({ itemCart: RoomCart, qnt: Int ->
            lifecycleScope.launch {
                viewModel.updateQnt(id = itemCart.id, qnt = qnt)
            }
        }, { itemcart: RoomCart ->
            lifecycleScope.launch {
                viewModel.deleteCart(id = itemcart.id)
            }
        })
        getLocalCartList()

        binding.apply {

            viewadapter = cartAdapter

            btnBack.setOnClickListener {
                Navigation.findNavController(root)
                    .popBackStack()
            }
            btnNoOrder.setOnClickListener {
                Navigation.findNavController(binding.recCart)
                    .navigate(R.id.nav_product)
            }

            btnBuy.setOnClickListener {
                if (viewModel.price != 0.0) {
                    val bundle = Bundle()
                    bundle.putDouble("amount", viewModel.price)
                    bundle.putString("ordertype", "cart")
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_nav_cart_list_to_nav_address_list, bundle)
                } else Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                    .show()

            }
        }
    }

    private fun getLocalCartList() {
        lifecycleScope.launch {

            viewModel.getCart().collect {

                viewModel.localcartlist = it
                if (it.isNotEmpty()) {
                    binding.apply {
                        conData.visibility=View.VISIBLE
                        NoOrder.visibility=View.GONE
                    }
                    it.forEach { iteam ->
                        viewModel.localCartListIds += "${iteam.id},"
                    }
                    if (cartAdapter.itemCount==0)
                    getCartList(
                        viewModel.localCartListIds.subSequence(
                            0,
                            viewModel.localCartListIds.length - 1
                        ).toString()
                    )

                }else{
                    binding.apply {
                        conData.visibility=View.GONE
                        NoOrder.visibility=View.VISIBLE
                    }
                }
            }
        }
    }

    private fun getCartList(ids: String) {
        lifecycleScope.launch {
            viewModel.getCartList(ids)
            if (view != null)
                viewModel.apicart.observe(viewLifecycleOwner) { cartRes ->

                    cartRes.results?.let {

                        for (i in 0 until cartRes.results.size) {
                            lifecycleScope.launch {
                                viewModel.updateCartPrice(
                                    cartRes.results[i].id ?: -1,
                                    cartRes.results[i].ecommercePrice ?: "0"
                                )
                            }
                        }
                        getCart()
                    }


                }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun getCart() {


        lifecycleScope.launch {

            viewModel.getCart().collect {
                if (it.isNotEmpty()) {
                    viewModel.price = 0.0
                    it.forEach { iteam ->
                        launch {
                            val qnt: Double = iteam.quantaty?.toDouble() ?: 0.0
                            val pricec = iteam.price?.toDouble() ?: 0.0
                            viewModel.price += qnt * pricec
                        }
                    }

                    cartAdapter.submitList(it)

                    binding.apply {
                        txtPrice.text = "₹ ${viewModel.price}"
                        txtDis.text = "Saved ₹ ${viewModel.price / 4}"
                    }
                } else {
                    Navigation.findNavController(binding.root).navigate(R.id.nav_product)
                }

            }


        }
    }


}