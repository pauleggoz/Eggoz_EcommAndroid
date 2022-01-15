package com.eggoz.ecommerce.view.product

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentMoreProductBinding
import com.eggoz.ecommerce.databinding.FragmentProductBinding
import com.eggoz.ecommerce.network.model.Products
import com.eggoz.ecommerce.room.RoomCart
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.product.adapter.ProductAdapter
import com.eggoz.ecommerce.view.product.adapter.ProductMoreAdapter
import com.eggoz.ecommerce.view.product.callback.ProductCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MoreProductFragment : Fragment(), ProductCallback {

    private var _binding: FragmentMoreProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialog: Loadinddialog
    private var userPreferences: UserPreferences? = null
    private lateinit var job2: Job
    private lateinit var viewModel: ProductMoreViewModel
    private var price = 0.0
    private var city_id = -1
    private lateinit var prodadapter: ProductMoreAdapter

    private lateinit var type: String
    private lateinit var result: ArrayList<Products.Result>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoreProductBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        type = this.arguments?.getString("type", "") ?: ""
        viewModel = ViewModelProvider(this).get(ProductMoreViewModel::class.java)
        dialog = Loadinddialog()
        result = ArrayList()


        getCart()

        userPreferences = UserPreferences(requireContext())
        prodList()

        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(binding.root)
                    .popBackStack()
            }
            txtCart.setOnClickListener {
                Navigation.findNavController(root)
                    .navigate(R.id.action_nav_product_popular_to_nav_cart_list)
            }
        }

    }

    private fun prodList() {

        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {
            city_id = userPreferences?.city?.buffer()?.first() ?: -1
            viewModel.productList(
                city = city_id, is_available = 1
            )
            viewModel.responProduct.observe(viewLifecycleOwner, Observer {

                if (dialog.isShowing())
                    dialog.dismiss()

                if (it?.errorType != null) {
                    Toast.makeText(requireContext(), it.errorType, Toast.LENGTH_SHORT)
                        .show()

                } else {
                    if (it.results != null) {
                        if (type == "Popular") {
                            result.clear()
                            for (i in it.results!!.indices) {
                                if (it.results!![i].isPrimeOnline == true) {
                                    result.add(it.results!![i])
                                }
                            }
                        }
                        prodadapter = ProductMoreAdapter(
                            callback = this@MoreProductFragment,
                            result = result
                        )
                        binding.apply {
                            recProd.adapter = prodadapter
                            (recProd.adapter as ProductMoreAdapter).notifyDataSetChanged()
                        }
                    }
                }
            })
        }

        binding.apply {
            recProd.apply {
                setHasFixedSize(true)
                layoutManager =
                    GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
                itemAnimator = DefaultItemAnimator()
                isNestedScrollingEnabled = false
            }
        }


    }

    @SuppressLint("SetTextI18n")
    private fun getCart() {
        job2 = lifecycleScope.launch {

            viewModel.getCart2().collect {
                if (it.isNotEmpty()) {
                    binding.consCart.visibility = View.VISIBLE

                    price = 0.0
                    for (i in it.indices) {
                        val qnt: Double = it[i].quantaty?.toDouble() ?: 0.0
                        val pricec = it[i].price?.toDouble() ?: 0.0
                        price += qnt * pricec
                    }

                    binding.apply {
                        txtPrice.text = "₹ $price"
                    }
                }

            }

        }
    }

    override fun itemclick(
        id: Int,
        name: String,
        Image: String,
        Priceval: String,
        qnt: Int,
        des: String,
        status: Boolean,
        sKUCount: Int
    ) {
        binding.consCart.visibility = View.VISIBLE

        val cart = RoomCart(
            id = id,
            name = name,
            img = Image,
            price = Priceval,
            quantaty = qnt,
            des = des,
            status = status,
            sKUCount = sKUCount
        )
        //save
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.insertdata(cart = cart)
        }
    }

    override fun updateCart(id: Int, qnt: Int, price: String) {
        lifecycleScope.launch {
            viewModel.updateCart(id = id, qnt = qnt, price = price)
        }
    }

    override fun deleteCart(id: Int) {
        lifecycleScope.launch {
            viewModel.deleteCart(id = id)
        }
    }

    override fun qntCart(id: Int): Int = runBlocking {
        viewModel.qntCart(id = id) ?: 0
    }

    override fun updateqNT(id: Int, qnt: Int) {
        lifecycleScope.launch {
            viewModel.updateQnt(id = id, qnt = qnt)
        }
    }

    override fun incCart(id: Int) {
        lifecycleScope.launch {
            viewModel.incCart(id = id)
        }
    }

    override fun decCart(id: Int) {
        lifecycleScope.launch {
            viewModel.qntCart(id = id)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}