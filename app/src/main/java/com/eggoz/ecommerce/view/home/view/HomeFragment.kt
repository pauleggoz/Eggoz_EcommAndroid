package com.eggoz.ecommerce.view.home.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentHomeBinding
import com.eggoz.ecommerce.network.model.HomeSlider
import com.eggoz.ecommerce.room.MyDatabase
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.utils.NetworkConnect
import com.eggoz.ecommerce.view.cart.viewmodel.CartProductViewModel
import com.eggoz.ecommerce.view.cart.viewmodel.CartViewModelFactory
import com.eggoz.ecommerce.view.cart.viewmodel.ProductRepository
import com.eggoz.ecommerce.view.home.viewmodel.HomeViewModel
import com.eggoz.ecommerce.view.home.adapter.ProductPopularAdapter
import com.eggoz.ecommerce.view.home.adapter.SliderAdapter
import com.eggoz.ecommerce.view.home.adapter.SubscriptionAdapter
import com.eggoz.ecommerce.view.home.viewmodel.HomeRepository
import com.eggoz.ecommerce.view.home.viewmodel.HomeViewModelFactory
import kotlinx.coroutines.launch
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var swipeTimer: Timer
    private var location = 0
    private lateinit var subadapter: SubscriptionAdapter
    private lateinit var prodadapter: ProductPopularAdapter
    private lateinit var viewModel: HomeViewModel
    private lateinit var dialog: Loadinddialog
    private lateinit var network: NetworkConnect

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        init()
        return binding.root
    }

    private fun init() {
        val userPreferences = UserPreferences(requireContext())
        val repository = HomeRepository(userPreferences)
        val viewmodelFat = HomeViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewmodelFat)[HomeViewModel::class.java]

        network = NetworkConnect(requireContext())
        dialog = Loadinddialog()
        prodadapter = ProductPopularAdapter()
        subadapter = SubscriptionAdapter()

        binding.popadapter = prodadapter
        binding.subdapter = subadapter


        binding.apply {
            root.isFocusableInTouchMode = true
            root.requestFocus()
            root.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                    return@setOnKeyListener true
                } else false
            }
        }

        binding.txtPopularProductsseeall.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("type", "Popular")
            Navigation.findNavController(binding.txtPopularProductsseeall)
                .navigate(R.id.action_nav_home_to_nav_product_popular, bundle)
        }



        binding.apply {

            root.isFocusableInTouchMode = true
            root.requestFocus()
            root.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                    closeApp()
                    true
                } else false
            }
        }

        binding.txtReload.setOnClickListener {
            apiCallfrist()
        }
        apiCallfrist()

        slider()

    }

    private fun closeApp() {
        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to exit?")
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { _, _ -> requireActivity().finish() }
            .setNegativeButton("No", null)
            .show()
    }

    private fun apiCallfrist() {

        network.observe(viewLifecycleOwner, {
            if (it) {
                binding.layoutNoNetwork.visibility = View.GONE
                binding.layoutData.visibility = View.VISIBLE

                Subscription()

                productpopular()

            } else {
                binding.layoutNoNetwork.visibility = View.VISIBLE
                binding.layoutData.visibility = View.GONE
            }
        })
    }

    private fun Subscription() {

        if (!dialog.isShowing())
            dialog.create(requireContext())

        lifecycleScope.launch {
            viewModel.getSubList(
                userid = 3, context = requireContext()
            ).observe(viewLifecycleOwner, {

                if (dialog.isShowing())
                    dialog.dismiss()
                if (it.results != null) {
                    if (it.results.isNotEmpty()) {
                        binding.txtSubscription.visibility = View.VISIBLE
                        subadapter.submitList(it.results)
                    }
                } else {
                    binding.txtSubscription.visibility = View.GONE
                }
            })
        }

    }

    private fun productpopular() {
        if (!dialog.isShowing())
            dialog.create(requireContext())

        lifecycleScope.launch {
            viewModel.productList().observe(viewLifecycleOwner, {
                if (dialog.isShowing())
                    dialog.dismiss()

                it.results.let {
                    binding.apply {
                        prodadapter.submitList(it)
                    }
                }
            })
        }


    }

    private fun slider() {
        if (!dialog.isShowing())
            dialog.create(requireContext())

        lifecycleScope.launch {
            viewModel.homeSlider(
                context = requireContext()
            ).observe(viewLifecycleOwner, {

                if (dialog.isShowing())
                    dialog.dismiss()

                it.results?.let { it1 -> pageSlider(it1) }
            })
        }
    }

    private fun pageSlider(list: List<HomeSlider.Result>) {


        val dotscount: Int
        val dots: Array<ImageView?>?

        val viewPagerAdapter = SliderAdapter(list)

        binding.paggerSlider.clipToPadding = false
        binding.paggerSlider.offscreenPageLimit = 2
        binding.paggerSlider.setPadding(20, 0, 20, 0)
        binding.paggerSlider.pageMargin = 20

        binding.paggerSlider.adapter = viewPagerAdapter

        swipeTimer = Timer()

        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.paggerSlider.setCurrentItem(location + 1, true)
                    location++
                    if (viewPagerAdapter.count == location)
                        location = 0
                }, 0)
            }
        }, 3000, 3000)

        dotscount = viewPagerAdapter.count
        dots = arrayOfNulls(dotscount)
        for (i in 0 until dotscount) {
            dots[i] = ImageView(requireContext())
            dots[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.non_active_dot
                )
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            binding.SliderDots.addView(dots[i], params)
        }

        dots[0]!!.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.active_dot
            )
        )

        binding.paggerSlider.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                location = position
            }

            override fun onPageSelected(position: Int) {
                Log.d("data", "onPageSelected $position")
                for (i in 0 until dotscount) {
                    dots[i]?.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.non_active_dot
                        )
                    )
                }
                dots[position]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.active_dot
                    )
                )
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })


    }


}