package com.eggoz.ecommerce

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.BuildConfig
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.cashfree.pg.CFPaymentService
import com.eggoz.ecommerce.data.UserPreferences
import com.eggoz.ecommerce.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var loc = 0
    private lateinit var navController: NavController
    private var userPreferences: UserPreferences? = null

    private lateinit var viewModel: MainViewModel

    private var userid: Int = -1
    private lateinit var headerLayout: View
    private lateinit var txt_person_name: TextView
    private lateinit var txt_person_mobile: TextView

    var totalamount = 0.0
    var paymentType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_color)
//        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerlayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerlayout.addDrawerListener(toggle)
        toggle.syncState()

        if (binding.drawerlayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerlayout.closeDrawer(GravityCompat.START)
        }

        binding.toolbar.visibility = View.GONE
        binding.navView.visibility = View.GONE


        initView()
        sideNaveClick()


    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (binding.drawerlayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerlayout.closeDrawer(GravityCompat.START)
        } else {
            val des = navController.currentDestination?.id ?: -1
            when (des) {

                R.id.nav_home -> {
                    loc = 0
                    Log.d("data", "des $des loc $loc nav_home")
                    binding.btmNav.menu.getItem(loc).isChecked = true
                    binding.btmNav.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.VISIBLE
                    binding.navView.visibility = View.VISIBLE
                }
                R.id.nav_product -> {
                    loc = 1
                    Log.d("data", "des $des loc $loc nav_product")
                    binding.btmNav.menu.getItem(loc).isChecked = true
                    binding.btmNav.visibility = View.VISIBLE
                }
                R.id.nav_wallet -> {
                    loc = 2
                    Log.d("data", "des $des loc $loc nav_wallet")
                    binding.btmNav.menu.getItem(loc).isChecked = true
                    binding.btmNav.visibility = View.VISIBLE
                }
                R.id.nav_profile -> {
                    loc = 3
                    Log.d("data", "des $des loc $loc nav_profile")
                    binding.btmNav.menu.getItem(loc).isChecked = true
                    binding.btmNav.visibility = View.VISIBLE
                }
                else -> {
                    Log.d("data", "des $des loc $loc else")
                    binding.btmNav.visibility = View.GONE
                }
            }
        }
    }


    private fun initView() {
        userPreferences = UserPreferences(this)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.btmNav.menu.findItem(R.id.nav_botomhome).isChecked = true
        try {
            val pInfo = packageManager.getPackageInfo(packageName, 0);
            binding.footerItem1.text = pInfo.versionName;
        } catch (e: Exception) {
            e.printStackTrace();
        }
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.nav_home -> {
                    loc = 0
                    binding.btmNav.menu.findItem(R.id.nav_botomhome).isChecked = true
                    binding.btmNav.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.VISIBLE
                    binding.navView.visibility = View.VISIBLE
                    if (binding.drawerlayout.isDrawerOpen(GravityCompat.START)) {
                        binding.drawerlayout.closeDrawer(GravityCompat.START)
                    }
                }
                R.id.nav_product -> {
                    loc = 1
                    binding.btmNav.menu.findItem(R.id.nav_botomprod).isChecked = true
                    binding.btmNav.visibility = View.VISIBLE
                }
                R.id.nav_wallet -> {
                    loc = 2
                    binding.btmNav.menu.findItem(R.id.nav_botomwallet).isChecked = true
                    binding.btmNav.visibility = View.VISIBLE
                }
                R.id.nav_profile -> {
                    loc = 3
                    binding.btmNav.menu.findItem(R.id.nav_botomprofile).isChecked = true
                    binding.btmNav.visibility = View.VISIBLE
                }
                else -> {
                    binding.btmNav.visibility = View.GONE
                }

            }

        }


        headerLayout = binding.navView.inflateHeaderView(R.layout.nav_header)

        txt_person_name = headerLayout.findViewById(R.id.txt_name)
        txt_person_mobile = headerLayout.findViewById(R.id.txt_person_mobile)

        headerLayout.setOnClickListener {
            binding.drawerlayout.closeDrawer(GravityCompat.START)
            navController.navigate(R.id.nav_profile)
            loc = 3
            binding.btmNav.menu.findItem(R.id.nav_botomprofile).isChecked = true
            binding.btmNav.visibility = View.VISIBLE
        }

        bottomNav()
        lifecycleScope.launch {
            userid = userPreferences!!.Customer_id.first() ?: -1
            if (userid != -1) {
                getData()
            }
        }

    }

    private fun getData() {
        lifecycleScope.launch {
            viewModel.user(userid, this@MainActivity)

        }



        viewModel.responUser.observe(this, {
            if (it?.errorType != null) {
                if (it.errors != null)
                    if (it.errors!![0].message == "Invalid signature.") {
                        viewModel.refreshToken.observe(this) {
                            lifecycleScope.launchWhenStarted {
                                userPreferences?.authtoken?.collect { response ->
                                    viewModel.setRefreshToken(response!!)
                                }
                                userPreferences?.saveAuthtoke(it.token!!)

                            }
                        }
                        navController.navigate(R.id.nav_sigin1)
                    }

            } else {
                txt_person_name.text = it.name ?: ""
                txt_person_mobile.text = it.phoneNo ?: ""

            }
        })
    }

    private fun bottomNav() {
        binding.btmNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_botomhome -> {
                    if (loc != 0)
                        navController.navigate(R.id.nav_home)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_botomprod -> {
                    if (loc != 1)
                        navController.navigate(R.id.nav_product)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_botomwallet -> {
                    if (loc != 2)
                        navController.navigate(R.id.nav_wallet)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_botomprofile -> {
                    if (loc != 3)
                        navController.navigate(R.id.nav_profile)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Same request code for all payment APIs.
        //Same request code for all payment APIs.
        Log.d("TAG", "ReqCode : " + CFPaymentService.REQ_CODE)
        Log.d("TAG", "API Response : ")
        Log.d("TAG", "ordertype :$paymentType ")
        //Prints all extras. Replace with app logic.
        //Prints all extras. Replace with app logic.

        data?.let {
            try {
                val bundle: Bundle = data.extras!!
                for (key in bundle.keySet()) {
                    if (bundle.getString(key) != null) {
                        Log.d("TAG", key + ":" + bundle.getString(key))
                    }
                }


                val ordertype = paymentType
                val txStatus = bundle.getString("txStatus")
                Log.d("TAG", "onActivityResult: order $ordertype stat= $txStatus")
                if ((ordertype == "single" || ordertype == "cart") && txStatus == "SUCCESS")
                    conformPaymentCart(bundle)
                else if (ordertype == "wallet" && txStatus == "SUCCESS")
                    conformPaymentWallet(bundle)
                else
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun conformPaymentCart(bundle: Bundle) {
        lifecycleScope.launch {
            lifecycleScope.launch {

                viewModel.conformPaymentCart(this@MainActivity, bundle)
                viewModel.responCheckout.observe(this@MainActivity, Observer {
                    if (it.errorType == null) {
                        if (paymentType == "cart")
                            if ((it.sign == it.signData?.signature) && it.sign != null && it.sign != "") {
                                viewModel.cartclear()
                                Log.d(
                                    "TAG",
                                    "conformPaymentCart: ${it.sign} \n ${it.signData?.signature}"
                                )
                                loc = 0
                                binding.btmNav.menu.findItem(R.id.nav_botomhome).isChecked = true
                                binding.btmNav.visibility = View.VISIBLE
                                binding.toolbar.visibility = View.VISIBLE
                                binding.navView.visibility = View.VISIBLE
                                navController.navigate(R.id.nav_home)
                                paymentType = ""

                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Some error has occurred",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                })
            }

        }
    }

    fun conformPaymentWallet(bundle: Bundle) {
        lifecycleScope.launch {
            lifecycleScope.launch {

                viewModel.conformPaymentWallet(this@MainActivity, bundle)
                viewModel.responCheckoutWallet.observe(this@MainActivity, Observer {
                    if (it.errorType == null) {
                        Log.d("TAG", "conformPaymentWallet: ${it.sign}  ${it.signData?.signature}")
                        if (paymentType == "wallet")
                            if ((it.sign == it.signData?.signature) && it.sign != null && it.sign != "") {
                                loc = 0
                                binding.btmNav.menu.findItem(R.id.nav_botomhome).isChecked = true
                                binding.btmNav.visibility = View.VISIBLE
                                binding.toolbar.visibility = View.VISIBLE
                                binding.navView.visibility = View.VISIBLE
                                navController.navigate(R.id.nav_home)
                                paymentType = ""

                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Some error has occurred",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                })
            }

        }
    }

    fun sideNaveClick() {
        binding.navView.setNavigationItemSelectedListener { item ->

            when {
                item.itemId == R.id.nav_homeside -> {
                    loc = 0
                    binding.btmNav.menu.findItem(R.id.nav_botomhome).isChecked = true
                    binding.btmNav.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.VISIBLE
                    binding.navView.visibility = View.VISIBLE
                    navController.navigate(R.id.nav_home)
                }
                item.itemId == R.id.nav_prod -> {
                    loc = 1
                    binding.btmNav.menu.findItem(R.id.nav_botomprod).isChecked = true
                    binding.btmNav.visibility = View.VISIBLE
                    navController.navigate(R.id.nav_product)
                }
                item.itemId == R.id.nav_order -> {

                }
                item.itemId == R.id.nav_subscription -> {

                }
                item.itemId == R.id.nav_referearn -> {

                }
                item.itemId == R.id.nav_myaccount -> {

                }
                item.itemId == R.id.nav_contactus -> {

                }
                item.itemId == R.id.nav_side_FAQs -> {

                }
                item.itemId == R.id.nav_side_help_support -> {

                }
                item.itemId == R.id.nav_side_Logout -> {
                    lifecycleScope.launch {
                        userPreferences!!.clear()
                        navController.navigate(R.id.nav_city)
                    }
                }
            }
            binding.drawerlayout.closeDrawer(GravityCompat.START)

            return@setNavigationItemSelectedListener true
        }
    }
}