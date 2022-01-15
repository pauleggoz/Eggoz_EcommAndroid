package com.eggoz.ecommerce.view.cart.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.FragmentCartListBinding
import com.eggoz.ecommerce.room.MyDatabase
import com.eggoz.ecommerce.room.RoomCart
import com.eggoz.ecommerce.view.cart.adapter.RoomCartAdapter
import com.eggoz.ecommerce.view.cart.viewmodel.CartProductViewModel
import com.eggoz.ecommerce.view.cart.viewmodel.CartViewModelFactory
import com.eggoz.ecommerce.view.cart.viewmodel.ProductRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CartListFragment : Fragment() {

    private var _binding: FragmentCartListBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartAdapter: RoomCartAdapter
    private lateinit var viewModel: CartProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartListBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        val cartDoa = MyDatabase.getInstance(context = requireContext()).deatailcart
        val repository = ProductRepository(cartDoa)
        val viewmodelFat = CartViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewmodelFat)[CartProductViewModel::class.java]

        getCart()

        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(root)
                    .popBackStack()
            }

            btnBuy.setOnClickListener {
                val bundle = Bundle()
                bundle.putDouble("amount", viewModel.price)
                bundle.putString("ordertype", "cart")
                Navigation.findNavController(root)
                    .navigate(R.id.action_nav_cart_list_to_nav_address_list, bundle)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getCart() {
        cartAdapter = RoomCartAdapter { itemCart: RoomCart, qnt: Int ->
            if (qnt == 0)
                viewModel.deleteCart(item = itemCart)
            else
                viewModel.updateCart(item = itemCart, qnt = qnt)
        }
        binding.viewadapter = cartAdapter

        lifecycleScope.launch {

            viewModel.cartList?.collect {
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}