package com.eggoz.ecommerce.view.cart

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.FragmentCartListBinding
import com.eggoz.ecommerce.view.cart.adapter.CartAdapter
import com.eggoz.ecommerce.view.cart.callback.CartListCallback
import com.eggoz.ecommerce.view.product.ProductViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CartListFragment : Fragment(), CartListCallback {

    private var _binding: FragmentCartListBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartAdapter: CartAdapter
    private lateinit var job: Job
    private lateinit var viewModel: ProductViewModel
    private var price:Double= 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartListBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }
    private fun init(){
        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        getCart()

        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(root)
                    .popBackStack()
            }
            recCart.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                itemAnimator = DefaultItemAnimator()
                isNestedScrollingEnabled = false
            }

            btnBuy.setOnClickListener {
                val bundle = Bundle()
                bundle.putDouble("amount",price)
                bundle.putString("ordertype","cart")
                Navigation.findNavController(root)
                    .navigate(R.id.action_nav_cart_list_to_nav_address_list,bundle)
            }
        }
    }

    override fun updateCart(id: Int, qnt: Int,price:String) {
        lifecycleScope.launch {
            viewModel.updateCart(id = id,qnt =  qnt,price =  price)
        }
    }

    override fun CartSize() :Int{
        return runBlocking {
            viewModel.CartSize()
        }
    }

    override fun deleteCart(id: Int) {
        lifecycleScope.launch {
            viewModel.deleteCart(id = id)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getCart(){
        job = lifecycleScope.launch {
            viewModel.getCart2().collect {
                if (it.isNotEmpty()){
                    cartAdapter = CartAdapter(result = it,callbak = this@CartListFragment)

                    price=0.0
                    for (i in it.indices){
                        val qnt:Double=it[i].quantaty?.toDouble() ?:0.0
                        val pricec=it[i].price?.toDouble() ?:0.0
                        price+=qnt*pricec
                    }

                    binding.apply {
                        recCart.adapter = cartAdapter
                        (recCart.adapter as CartAdapter).notifyDataSetChanged()
                        txtPrice.text = "₹ $price"
                        txtDis.text="Saved ₹ ${price/4}"
                    }
                }

            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
        job.cancel()
    }

}