package com.eggoz.ecommerce.view.home

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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentHomeBinding
import com.eggoz.ecommerce.network.model.HomeSlider
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.utils.NetworkConnect
import com.eggoz.ecommerce.view.home.adapter.ProductPopularAdapter
import com.eggoz.ecommerce.view.home.adapter.SliderAdapter
import com.eggoz.ecommerce.view.home.adapter.SubscriptionAdapter
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var swipeTimer: Timer
    private var location = 0
    private lateinit var subadapter: SubscriptionAdapter
    private lateinit var prodadapter: ProductPopularAdapter
    private var city_id = -1
    private var user_id = -1
    private var userPreferences: UserPreferences? = null
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
        network= NetworkConnect(requireContext())
        userPreferences = UserPreferences(requireContext())
        dialog = Loadinddialog()

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        lifecycleScope.launch {

            city_id = userPreferences?.city?.buffer()?.first() ?: -1
            user_id = userPreferences?.userid?.buffer()?.first() ?: -1
        }

        binding.apply {
            root.isFocusableInTouchMode = true
            root.requestFocus()
            root.setOnKeyListener { v, keyCode, event ->
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

            recycleSubs.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(
                    activity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                itemAnimator = DefaultItemAnimator()
                isNestedScrollingEnabled = false
            }
            recyclePop.apply {
                layoutManager = LinearLayoutManager(
                    activity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                itemAnimator = DefaultItemAnimator()
                isNestedScrollingEnabled = false
            }
            root.isFocusableInTouchMode = true
            root.requestFocus()
            root.setOnKeyListener { v, keyCode, event ->
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
            ) { dialog, id -> requireActivity().finish() }
            .setNegativeButton("No", null)
            .show()
    }

    private fun apiCallfrist(){

        network.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
        if(it) {
            binding.layoutNoNetwork.visibility=View.GONE
            binding.layoutData.visibility=View.VISIBLE
            Subscription()

            productpopular()

        }else {
            binding.layoutNoNetwork.visibility = View.VISIBLE
            binding.layoutData.visibility=View.GONE
        }
        })
    }

    private fun Subscription() {

        if (!dialog.isShowing())
            dialog.create(requireContext())

        lifecycleScope.launch {
            viewModel.getSubList(
               userid =  3,context=requireContext()
            )
            viewModel.responSublist.observe(viewLifecycleOwner, {

                if (dialog.isShowing())
                    dialog.dismiss()

                if (it?.errorType != null) {
                    Toast.makeText(requireContext(), it.errorType, Toast.LENGTH_SHORT)
                        .show()

                } else {
                    if (it.results != null) {
                        subadapter = SubscriptionAdapter(it.results)
                        binding.apply {
                            recycleSubs.adapter = subadapter
                            (recycleSubs.adapter as SubscriptionAdapter).notifyDataSetChanged()
                        }
                    }else{
                        binding.txtSubscription.visibility=View.GONE
                    }
                }
            })
        }

    }

    private fun productpopular() {
        if (!dialog.isShowing())
            dialog.create(requireContext())

        lifecycleScope.launch {
            viewModel.productList(
                city = city_id, is_available = 1
            )
            viewModel.responProduct.observe(viewLifecycleOwner, {

                if (dialog.isShowing())
                    dialog.dismiss()

                if (it?.errorType != null) {
                    Toast.makeText(requireContext(), it.errorType, Toast.LENGTH_SHORT)
                        .show()

                } else {
                    if (it.results != null) {
                        prodadapter = ProductPopularAdapter( result = it.results!!)
                        binding.apply {
                            recyclePop.adapter = prodadapter
                            (recyclePop.adapter as ProductPopularAdapter).notifyDataSetChanged()
                        }
                    }
                }
            })
        }


    }

    private fun slider(){
        if (!dialog.isShowing())
            dialog.create(requireContext())

        lifecycleScope.launch {
            viewModel.homeSlider(
                context = requireContext()
            )
            viewModel.responHomeSlider.observe(viewLifecycleOwner, {

                if (dialog.isShowing())
                    dialog.dismiss()

                if (it?.errorType != null) {
                    Toast.makeText(requireContext(), it.errorType, Toast.LENGTH_SHORT)
                        .show()

                } else {
                    if (it.results != null) {
                        if (it.results.size>0)
                            pageSlider(it.results)
                    }
                }
            })
        }
    }

    private fun pageSlider(list:List<HomeSlider.Result>) {


        var dotscount = 0
        var dots: Array<ImageView?>? = null

        val viewPagerAdapter = SliderAdapter(list)

        binding.paggerSlider.clipToPadding = false
        binding.paggerSlider.offscreenPageLimit = 2
        binding.paggerSlider.setPadding(20, 0, 20, 0)
        binding.paggerSlider.pageMargin=20

        binding.paggerSlider.adapter = viewPagerAdapter

        swipeTimer = Timer()

        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                Handler(Looper.getMainLooper()).postDelayed( {

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
//        view dots linked with viewpager ends


    }

}