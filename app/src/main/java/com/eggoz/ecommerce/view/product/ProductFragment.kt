package com.eggoz.ecommerce.view.product

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentProductBinding
import com.eggoz.ecommerce.room.RoomCart
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.utils.NetworkConnect
import com.eggoz.ecommerce.view.cart.adapter.CartAdapter
import com.eggoz.ecommerce.view.product.adapter.ProductAdapter
import com.eggoz.ecommerce.view.product.callback.ProductCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


class ProductFragment : Fragment(), ProductCallback {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var bootomNave: BottomNavigationView

    private lateinit var viewModel: ProductViewModel
    var token = ""
    private lateinit var job: Job
    private lateinit var job2: Job
    private var userPreferences: UserPreferences? = null
    private lateinit var network: NetworkConnect

    private lateinit var prodadapter: ProductAdapter
    private var _tokenFlow: Flow<String>? = null
    private var city_id = -1

    private var price = 0.0

    private lateinit var dialog: Loadinddialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        network = NetworkConnect(requireContext())
        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        dialog = Loadinddialog()

        getCart()


        userPreferences = UserPreferences(requireContext())
        prodList()

        binding.apply {
            txtCart.setOnClickListener {
                Navigation.findNavController(root)
                    .navigate(R.id.action_nav_product_to_nav_cart_list)
            }
        }
        lifecycleScope.launch {
            token = userPreferences?.authtoken?.cancellable()?.first() ?: ""

        }
        bootomNave = requireActivity().findViewById(R.id.btm_nav_)
        binding.nested.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY - oldScrollY < 0) {
                bootomNave.alpha = 1f
                bootomNave.visibility = View.VISIBLE
            } else if (scrollY > 100) {
                bootomNave.clearAnimation()
                bootomNave.animate().translationY(0f).alpha(0.2f).setDuration(400)
                    .setListener(object :
                        AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            bootomNave.visibility = View.GONE

                        }
                    })
            }
        }



        network.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.apply {
                    nested.visibility = View.VISIBLE
                    consCart.visibility = View.VISIBLE

                    layoutNoNetwork.visibility = View.GONE
                }
                prodList()
                getCart()

            } else {
                binding.apply {
                    nested.visibility = View.GONE
                    consCart.visibility = View.GONE

                    layoutNoNetwork.visibility = View.VISIBLE
                }
            }
        })


    }

    @SuppressLint("SetTextI18n")
    private fun cartshow() {
        job = lifecycleScope.launch {

            viewModel.getCart2().collect {
                binding.apply {
                    if (it.isNotEmpty())
                        consCart.visibility = View.VISIBLE
                    txtItem.text = "${it.size} Item |"
                }
            }

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

    override fun incCart(id: Int) {
        lifecycleScope.launch {
            viewModel.incCart(id = id)
        }
    }
    override fun updateqNT(id: Int,qnt:Int) {
        lifecycleScope.launch {
            viewModel.updateQnt(id = id,qnt=qnt)
        }
    }

    override fun decCart(id: Int) {
        lifecycleScope.launch {
            viewModel.qntCart(id = id)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun getCart() {
        job2 = lifecycleScope.launch {

            viewModel.getCart2().collect {
                if (it.isNotEmpty()) {
                    binding.consCart.visibility=View.GONE
                    price = 0.0
                    for (i in it.indices) {
                        val qnt: Double = it[i].quantaty?.toDouble() ?: 0.0
                        val pricec = it[i].price?.toDouble() ?: 0.0
                        price += qnt * pricec
                    }

                    binding.apply {
                        txtPrice.text = "₹ $price"
                    }
                }else{
                    binding.consCart.visibility=View.GONE
                }

            }

        }
    }

    private fun prodList() {
        if (!dialog.isShowing())
            dialog.create(requireContext())
        lifecycleScope.launch {
            city_id = userPreferences?.city?.buffer()?.first() ?: -1
            Log.d("data", "city$city_id")
            viewModel.productList(
                city = city_id, is_available = 1
            )
            viewModel.responProduct.observe(viewLifecycleOwner, Observer {

                cartshow()

                if (dialog.isShowing())
                    dialog.dismiss()

                if (it?.errorType != null) {
                    Toast.makeText(requireContext(), it.errorType, Toast.LENGTH_SHORT)
                        .show()

                } else {
                    if (it.results != null) {
                        prodadapter = ProductAdapter(
                            callback = this@ProductFragment,
                            result = it.results!!
                        )
                        binding.apply {
                            recProducts.adapter = prodadapter
                            (recProducts.adapter as ProductAdapter).notifyDataSetChanged()
                        }
                    }
                }
            })
        }

        binding.apply {
            recProducts.apply {
                setHasFixedSize(true)
                layoutManager =
                    GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
                itemAnimator = DefaultItemAnimator()
                isNestedScrollingEnabled = false
            }
        }


    }

    override fun onStop() {
        super.onStop()
        bootomNave.alpha = 1f
        bootomNave.clearAnimation()
    }

    override fun itemclick(
        id: Int,
        name: String,
        Image: String,
        Priceval: String,
        qnt: Int,
        des: String,
        status: Boolean,
        sKUCount:Int
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _tokenFlow = null
        userPreferences = null

    }

    override fun onDetach() {
        super.onDetach()
        if (job != null)
            job.cancel()
    }

}