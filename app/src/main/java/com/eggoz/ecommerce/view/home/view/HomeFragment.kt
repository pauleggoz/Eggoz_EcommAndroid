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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.ViewModelFactory
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentHomeBinding
import com.eggoz.ecommerce.network.model.HomeSlider
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.utils.NetworkConnect
import com.eggoz.ecommerce.view.home.adapter.BlogsAdapter
import com.eggoz.ecommerce.view.home.adapter.ProductPopularAdapter
import com.eggoz.ecommerce.view.home.adapter.SliderAdapter
import com.eggoz.ecommerce.view.home.adapter.SubscriptionAdapter
import com.eggoz.ecommerce.view.home.viewmodel.HomeRepository
import com.eggoz.ecommerce.view.home.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var swipeTimer: Timer
    private var location = 0
    private lateinit var subadapter: SubscriptionAdapter
    private lateinit var blogadapter: BlogsAdapter
    private lateinit var prodadapter: ProductPopularAdapter
    private lateinit var viewModel: HomeViewModel
    private lateinit var dialog: Loadinddialog
    private lateinit var network: NetworkConnect
    private lateinit var c: Calendar
    private lateinit var sdf: SimpleDateFormat
    private lateinit var sdfl: SimpleDateFormat
    private lateinit var start: String
    private lateinit var end: String

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
        val viewmodelFat = ViewModelFactory(repository)

        c = Calendar.getInstance()
        sdf = SimpleDateFormat("dd/MM/yyyy")
        sdfl = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        start = sdf.format(c.time)
        c.add(Calendar.DATE, 6)
        end = sdf.format(c.time)

        viewModel = ViewModelProvider(this, viewmodelFat)[HomeViewModel::class.java]

        network = NetworkConnect(requireContext())
        dialog = Loadinddialog()
        prodadapter = ProductPopularAdapter()
        subadapter = SubscriptionAdapter()
        blogadapter = BlogsAdapter()
        viewModel.blogsresults.clear()





        binding.apply {
            viewprodadapter = prodadapter
            viewsubadapter = subadapter
            viewblogadapter = blogadapter

            txtPopularProductsseeall.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("type", "Popular")
                Navigation.findNavController(txtPopularProductsseeall)
                    .navigate(R.id.action_nav_home_to_nav_product_popular, bundle)
            }
            groupOrder.visibility = View.VISIBLE
            txtOrderDateMonth.setOnClickListener {
                Navigation.findNavController(txtPopularProductsseeall)
                    .navigate(R.id.action_nav_home_to_nav_my_calendar)
            }
            imgCalander.setOnClickListener {
                Navigation.findNavController(txtPopularProductsseeall)
                    .navigate(R.id.action_nav_home_to_nav_my_calendar)
            }

            root.isFocusableInTouchMode = true
            root.requestFocus()
            root.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                    closeApp()
                    true
                } else false
            }

            layDay1.setOnClickListener {
                loadDayDetail(0)
            }
            layDay2.setOnClickListener {
                loadDayDetail(1)
            }
            layDay3.setOnClickListener {
                loadDayDetail(2)
            }
            layDay4.setOnClickListener {
                loadDayDetail(3)
            }
            layDay5.setOnClickListener {
                loadDayDetail(4)
            }
            layDay6.setOnClickListener {
                loadDayDetail(5)
            }
            layDay7.setOnClickListener {
                loadDayDetail(6)
            }
            txtReload.setOnClickListener {
                apiCallfrist()
            }
            scrollLayoutData.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (!v.canScrollVertically(1)) {
                    if (viewModel.blogsPageMax >= viewModel.blogsPage) {
                        prgBlog.visibility = View.VISIBLE
                        blogs()
                    }
                }
            }

            recBlog.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (!recyclerView.canScrollVertically(1)) {
                        if (viewModel.blogsPageMax <= viewModel.blogsPage) {
                            prgBlog.visibility = View.VISIBLE
                            blogs()
                        }
                    }
                }
            })
        }

        apiCallfrist()



        lifecycleScope.launch {
            val starteDate = sdf.parse(start)
            viewModel.orderlist(
                start, end
            ).observe(viewLifecycleOwner, {

                if (dialog.isShowing())
                    dialog.dismiss()
                it.results?.let { orderlist ->
                    orderlist.let { inorderlist ->
//                        if (inorderlist?.isEmpty()!!)
//                            binding.groupOrder.visibility = View.GONE
//                        else {
                            for (order in inorderlist) {
                                order.deliveryDate?.let { ddate ->
                                    val currDate = sdfl.parse(ddate)
                                    val diff1 = currDate.time - starteDate.time

                                    val date1 = ((diff1 / (1000 * 60 * 60 * 24))
                                            % 365)
                                    if (order.secondaryStatus.equals("created")) {
                                        if (date1 == 0L)
                                            binding.txtdate1.setBackgroundColor(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color.app_color
                                                )
                                            )
                                        if (date1 == 1L)
                                            binding.txtdate2.setBackgroundColor(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color.app_color
                                                )
                                            )
                                        if (date1 == 2L)
                                            binding.txtdate3.setBackgroundColor(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color.app_color
                                                )
                                            )
                                        if (date1 == 3L)
                                            binding.txtdate4.setBackgroundColor(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color.app_color
                                                )
                                            )
                                        if (date1 == 4L)
                                            binding.txtdate5.setBackgroundColor(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color.app_color
                                                )
                                            )
                                        if (date1 == 5L)
                                            binding.txtdate6.setBackgroundColor(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color.app_color
                                                )
                                            )
                                        if (date1 == 6L)
                                            binding.txtdate7.setBackgroundColor(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color.app_color
                                                )
                                            )
                                    }

                                    Log.d("TAG", "datediff: $date1")
                                }
                            }

                        }

//                    }

                }


            })
        }
        orderCalander()

    }

    private fun loadDayDetail(loc:Int){
        val cal = Calendar.getInstance()
        sdf = SimpleDateFormat("dd/MM/yyyy")
        cal.add(Calendar.DATE, loc)
        val bundle = Bundle()
        bundle.putString("date", sdf.format(cal.time))
        Navigation.findNavController(binding.txtPopularProductsseeall)
            .navigate(R.id.action_nav_home_to_nav_date_detail, bundle)

    }

    private fun orderCalander() {
        val c = Calendar.getInstance()
        val month = c.get(Calendar.MONTH)
        binding.apply {
            txtOrderDateMonth.text = DateFormatSymbols().shortMonths[month]
            groupOrder.visibility = View.VISIBLE
        }
        for (i in 0..6) {
            binding.apply {
                if (i == 0) {
                    txtday1.text =
                        DateFormatSymbols().shortWeekdays[c.get(Calendar.DAY_OF_WEEK)].substring(
                            0,
                            3
                        )
                    txtdate1.text = "${c.get(Calendar.DAY_OF_MONTH)}"
                } else if (i == 1) {
                    c.add(Calendar.DATE, 1)
                    txtday2.text =
                        DateFormatSymbols().shortWeekdays[c.get(Calendar.DAY_OF_WEEK)].substring(
                            0,
                            3
                        )
                    txtdate2.text = "${c.get(Calendar.DAY_OF_MONTH)}"
                } else if (i == 2) {
                    c.add(Calendar.DATE, 1)
                    txtday3.text =
                        DateFormatSymbols().shortWeekdays[c.get(Calendar.DAY_OF_WEEK)].substring(
                            0,
                            3
                        )
                    txtdate3.text = "${c.get(Calendar.DAY_OF_MONTH)}"
                } else if (i == 3) {
                    c.add(Calendar.DATE, 1)
                    txtday4.text =
                        DateFormatSymbols().shortWeekdays[c.get(Calendar.DAY_OF_WEEK)].substring(
                            0,
                            3
                        )
                    txtdate4.text = "${c.get(Calendar.DAY_OF_MONTH)}"
                } else if (i == 4) {
                    c.add(Calendar.DATE, 1)
                    txtday5.text =
                        DateFormatSymbols().shortWeekdays[c.get(Calendar.DAY_OF_WEEK)].substring(
                            0,
                            3
                        )
                    txtdate5.text = "${c.get(Calendar.DAY_OF_MONTH)}"
                } else if (i == 5) {
                    c.add(Calendar.DATE, 1)
                    txtday6.text =
                        DateFormatSymbols().shortWeekdays[c.get(Calendar.DAY_OF_WEEK)].substring(
                            0,
                            3
                        )
                    txtdate6.text = "${c.get(Calendar.DAY_OF_MONTH)}"
                } else if (i == 6) {
                    c.add(Calendar.DATE, 1)
                    txtday7.text =
                        DateFormatSymbols().shortWeekdays[c.get(Calendar.DAY_OF_WEEK)].substring(
                            0,
                            3
                        )
                    txtdate7.text = "${c.get(Calendar.DAY_OF_MONTH)}"
                    if (month != c.get(Calendar.MONTH))
                        txtOrderDateMonth.append(",${DateFormatSymbols().shortMonths[c.get(Calendar.MONTH)]}")
                }
            }


        }
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
                binding.scrollLayoutData.visibility = View.VISIBLE

                Subscription()

                productpopular()

                blogs()

                slider()

            } else {
                binding.layoutNoNetwork.visibility = View.VISIBLE
                binding.scrollLayoutData.visibility = View.GONE
            }
        })
    }

    private fun blogs() {

        lifecycleScope.launch {
            viewModel.blogs().observe(viewLifecycleOwner, {
                Log.d("TAG", "blogs: ${blogadapter.itemCount}")
                if (dialog.isShowing())
                    dialog.dismiss()
                if (binding.prgBlog.isVisible)
                    binding.prgBlog.visibility = View.GONE
                it.count?.let { max ->
                    viewModel.blogsPageMax = max
                }
                it.results?.let { blogList ->
                    if (blogList.isNotEmpty()) {
                        binding.txtSubscription.visibility = View.VISIBLE
                        viewModel.blogsresults.addAll(blogList)
                        blogadapter.submitList(null)
                        blogadapter.submitList(viewModel.blogsresults)
                        viewModel.blogsPage++
                        Log.d("TAG", "blogs: ${blogadapter.itemCount}")
                    }
                }
            })
        }

    }

    private fun Subscription() {

        if (!dialog.isShowing())
            dialog.create(requireContext())

        lifecycleScope.launch {
            viewModel.getSubList(
                userid = 3
            ).observe(viewLifecycleOwner, {
                if (dialog.isShowing())
                    dialog.dismiss()
                it.results?.let { sub->
                    if (sub.isNotEmpty()) {
                        binding.txtSubscription.visibility = View.VISIBLE
                        subadapter.submitList(sub)
                    }else binding.txtSubscription.visibility = View.GONE
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
                it.results?.let {
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
            viewModel.homeSlider().observe(viewLifecycleOwner, {

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