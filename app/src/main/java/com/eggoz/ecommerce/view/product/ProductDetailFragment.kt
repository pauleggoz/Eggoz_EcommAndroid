package com.eggoz.ecommerce.view.product

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.eggoz.ecommerce.databinding.FragmentProductDetailBinding
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.home.adapter.SliderAdapter
import com.eggoz.ecommerce.view.product.adapter.ImageSliderProd
import com.eggoz.ecommerce.view.product.adapter.ProductDetailPopularAdapter
import com.eggoz.ecommerce.view.product.adapter.TabLayoutAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.DecimalFormat


class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!


    private var location = 0
    private var item_id = -1
    private lateinit var viewModel: ProductViewModel
    private lateinit var subadapter: ProductDetailPopularAdapter
    private lateinit var tabadapter: TabLayoutAdapter

    private lateinit var benefit:ArrayList<String>
    private lateinit var specification:ArrayList<String>
    private lateinit var review:ArrayList<String>
    private lateinit var imageurl:ArrayList<String>
    private lateinit var NutritionInformation:ArrayList<String>

    private lateinit var dialog: Loadinddialog
    private lateinit var  viewPagerAdapter:ImageSliderProd
    private var city_id = -1
    private var userPreferences: UserPreferences? = null
    private var itemprice: Double? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        init()

        return binding.root
    }

    private fun init() {

        benefit= ArrayList()
        specification= ArrayList()
        review= ArrayList()
        imageurl= ArrayList()
        NutritionInformation= ArrayList()


        dialog = Loadinddialog()
        userPreferences = UserPreferences(requireContext())
        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        item_id = this.arguments?.getInt("id", -1) ?: -1


        binding.btnBuy.setOnClickListener {
            binding.btnBuy.startAnimation(AlphaAnimation(1F, 0.8F))
            if (itemprice!=null && itemprice?:0.0!=0.0) {
                val bundle = Bundle()
                bundle.putDouble("amount", itemprice ?: 0.0)
                bundle.putInt("product", item_id)
                bundle.putString("ordertype", "single")
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_nav_product_detail_to_nav_address_list, bundle)
            }else Toast.makeText(requireContext(),"Somthing went wrong",Toast.LENGTH_SHORT).show()
        }


        binding.btnBack.setOnClickListener {
            Navigation.findNavController(binding.root)
                .popBackStack()
        }
        if (item_id != -1)
            prodDetail()
        recycleTabLayout()
        recyclePeopealsobought()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d("data", "${tab?.position}")
                when (tab?.position) {
                    0 -> {
                        tabadapter.updatedata(benefit)

                    }
                    1 -> {
                        tabadapter.updatedata(specification)

                    }
                    2 -> {
//                        tabadapter.updatedata(NutritionInformation)

                    }
                    3 -> {
                        tabadapter.updatedata(NutritionInformation)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

    }

    @SuppressLint("SetTextI18n")
    private fun prodDetail() {
        if (!dialog.isShowing())
            dialog.create(requireContext())

        Log.d("data", "id$item_id")

        viewModel.productItembyid(
            id = item_id
        )
        viewModel.responProductbyid.observe(viewLifecycleOwner, {

            if (dialog.isShowing())
                dialog.dismiss()

            if (it?.errorType != null) {
                Toast.makeText(requireContext(), it.errorType, Toast.LENGTH_SHORT)
                    .show()

            } else {
                binding.txtItemTitle.text = it.name!! + " " + it.description!!

                val df = DecimalFormat("#.##")
                val  price=df.format(it.ecommercePrice?.toDouble() ?:0.00)
                binding.txtPrice.text = "₹ ${price}"
                binding.txtPrice.text = "₹ ${price}"
                itemprice=it.ecommercePrice?.toDouble() ?:0.00

                binding.txtItemDes.text = ""
                if (it.productDescriptions != null)
                    for (i in it.productDescriptions.indices) {
                        binding.txtItemDes.append(it.productDescriptions[i].description)
                    }
                binding.txtItemExp.text = it.shelfLife ?: ""

                if (it.productLongDescriptions != null)
                    for (i in it.productLongDescriptions.indices) {
                        benefit.add(it.productLongDescriptions[i].description ?: "")
                    }
                tabadapter.updatedata(benefit)
                if (it.productInformations != null)
                    for (i in it.productInformations.indices) {
                        if (it.productInformations[i].productInformationLine != null)
                            for (j in it.productInformations[i].productInformationLine?.indices!!) {
                                NutritionInformation.add("${it.productInformations[i].productInformationLine!![j].name}: ${String.format("%.2f",it.productInformations[i].productInformationLine!![j].infoValue.toString().toFloat())} ${it.productInformations[i].productInformationLine!![j].type}")

                            }

                    }
                if (it.productSpecifications!=null)
                    for (i in it.productSpecifications.indices) {
                        if (it.productSpecifications[i].specification != null)
                            specification.add(
                                it.productSpecifications[i].specification ?: ""
                            )

                    }

                imageurl.add(it.productImage ?:"")

                viewPagerAdapter = ImageSliderProd(imageurl)
                binding.paggerSlider.adapter = viewPagerAdapter
                (binding.paggerSlider.adapter as ImageSliderProd).notifyDataSetChanged()

//                pageSlider()


            }

        })
    }

    private fun pageSlider() {


        var dotscount = 0
        var dots: Array<ImageView?>? = null

        binding.paggerSlider.adapter = viewPagerAdapter
//        binding.paggerSlider.offscreenPageLimit = 2


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
        if (viewPagerAdapter.count>0)
        dots[0]!!.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.active_dot_rec
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
                        R.drawable.active_dot_rec
                    )
                )
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
//        view dots linked with viewpager ends

    }

    private fun recycleTabLayout() {
        tabadapter = TabLayoutAdapter()
        binding.recyTab.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            isNestedScrollingEnabled = false
            adapter = tabadapter
        }

    }

    private fun recyclePeopealsobought() {
        binding.recAlsobought.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            itemAnimator = DefaultItemAnimator()
            isNestedScrollingEnabled = false
        }


        if (!dialog.isShowing())
            dialog.create(requireContext())

        lifecycleScope.launch {
            city_id = userPreferences?.city?.buffer()?.first() ?: -1
            Log.d("data", "city$city_id")
            viewModel.productList(
                city = city_id, is_available = 1
            )
            viewModel.responProduct.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

                if (dialog.isShowing())
                    dialog.dismiss()

                if (it?.errorType != null) {
                    Toast.makeText(requireContext(), it.errorType, Toast.LENGTH_SHORT)
                        .show()

                } else {
                    if (it.results != null) {
                        subadapter = ProductDetailPopularAdapter(result =  it.results!!)
                        binding.apply {
                            recAlsobought.adapter = subadapter
                            (recAlsobought.adapter as ProductDetailPopularAdapter).notifyDataSetChanged()
                        }
                    }
                }
            })
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