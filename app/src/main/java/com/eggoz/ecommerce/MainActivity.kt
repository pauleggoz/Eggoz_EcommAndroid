package com.eggoz.ecommerce

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
//import com.cashfree.pg.CFPaymentService
import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.databinding.ActivityMainBinding
import com.eggoz.ecommerce.mainactivityviewmodel.MainRepository
import com.eggoz.ecommerce.mainactivityviewmodel.MainViewModel
import com.eggoz.ecommerce.mainactivityviewmodel.MainViewModelFactory
import com.eggoz.ecommerce.localdata.room.MyDatabase
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
//    private val binding get() = _binding!!

    private var loc = 0
    private lateinit var navController: NavController

    private lateinit var viewModel: MainViewModel

    private lateinit var headerLayout: View
    private lateinit var txt_person_name: TextView
    private lateinit var txt_person_mobile: TextView

    var paymentType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_color)
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
            when (navController.currentDestination?.id ?: -1) {

                R.id.nav_home -> {
                    loc = 0
                    binding.btmNav.menu.getItem(loc).isChecked = true
                    binding.btmNav.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.VISIBLE
                    binding.navView.visibility = View.VISIBLE
                }
                R.id.nav_product -> {
                    loc = 1
                    binding.btmNav.menu.getItem(loc).isChecked = true
                    binding.btmNav.visibility = View.VISIBLE
                }
                R.id.nav_wallet -> {
                    loc = 2
                    binding.btmNav.menu.getItem(loc).isChecked = true
                    binding.btmNav.visibility = View.VISIBLE
                }
                R.id.nav_profile -> {
                    loc = 3
                    binding.btmNav.menu.getItem(loc).isChecked = true
                    binding.btmNav.visibility = View.VISIBLE
                }
                else -> {
                    binding.btmNav.visibility = View.GONE
                }
            }
        }
    }


    private fun initView() {
        val userPreferences = UserPreferences(this)

        val repository =
            MainRepository(userPreferences, MyDatabase.getInstance(context = application))
        val viewmodelFat = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewmodelFat)[MainViewModel::class.java]
        binding.btmNav.menu.findItem(R.id.nav_botomhome).isChecked = true
        var versionName: String? = null
        try {
            versionName = packageManager.getPackageInfo(packageName, 0).toString()
            binding.versionName = versionName
        } catch (e: Exception) {
            binding.versionName = versionName
            e.printStackTrace()
        }
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
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
        getData()
    }

    private fun getData() {

        viewModel.responUser.observe(this) {
            if (it?.errorType != null) {
                if (it.errors != null)
                    if (it.errors!![0].message == "Invalid signature.") {
                        viewModel.refreshToken.observe(this) {


                            lifecycleScope.launchWhenStarted {
                                it.token?.let { it1 -> viewModel.upDateToken(it1) }
                            }
                        }
                        navController.navigate(R.id.nav_sigin1)
                    }

            } else {
                txt_person_name.text = it.name ?: ""
                txt_person_mobile.text = it.phoneNo ?: ""

            }
        }
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
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun conformPaymentCart(bundle: Bundle) {
        lifecycleScope.launch {
            lifecycleScope.launch {

                viewModel.conformPaymentCart(bundle)
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

    private fun conformPaymentWallet(bundle: Bundle) {
        lifecycleScope.launch {
            lifecycleScope.launch {

                viewModel.conformPaymentWallet(bundle)
                viewModel.responCheckoutWallet.observe(this@MainActivity) {
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
                }
            }

        }
    }

    private fun sideNaveClick() {
        binding.navView.setNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.nav_homeside -> {
                    loc = 0
                    binding.btmNav.menu.findItem(R.id.nav_botomhome).isChecked = true
                    binding.btmNav.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.VISIBLE
                    binding.navView.visibility = View.VISIBLE
                    navController.navigate(R.id.nav_home)
                }
                R.id.nav_prod -> {
                    loc = 1
                    binding.btmNav.menu.findItem(R.id.nav_botomprod).isChecked = true
                    binding.btmNav.visibility = View.VISIBLE
                    navController.navigate(R.id.nav_product)
                }
                R.id.nav_order -> {
                    navController.navigate(R.id.nav_orderlist)

                }
                R.id.nav_cart -> {
                    navController.navigate(R.id.nav_cart_list)

                }
                R.id.nav_subscription -> {
                    navController.navigate(R.id.nav_subscribeinfro)
                }
                R.id.nav_referearn -> {
                    binding.btmNav.visibility = View.GONE
                    binding.toolbar.visibility = View.GONE
                    binding.navView.visibility = View.GONE
                    navController.navigate(R.id.nav_referearn)
                }
                R.id.nav_myaccount -> {
                    loc = 3
                    binding.btmNav.menu.findItem(R.id.nav_botomprofile).isChecked = true
                    binding.btmNav.visibility = View.VISIBLE
                    navController.navigate(R.id.nav_profile)
                }
                R.id.nav_contactus -> {
                    binding.btmNav.visibility = View.GONE
                    binding.toolbar.visibility = View.GONE
                    binding.navView.visibility = View.GONE
                    navController.navigate(R.id.nav_contact_us)
                }
                R.id.nav_side_FAQs -> {
                    navController.navigate(R.id.nav_faq)

                }
                R.id.nav_side_help_support -> {
                    navController.navigate(R.id.nav_help_support)
                }
                R.id.nav_side_Logout -> {
                    AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Log Out")
                        .setMessage("Are you sure you want to logout ?")
                        .setPositiveButton("Yes") { _, _ ->
                            lifecycleScope.launch {
                                viewModel.userDataClear()
                                navController.navigate(R.id.nav_city)
                            }
                        }.setNegativeButton("No", null).show()

                }
            }
            binding.drawerlayout.closeDrawer(GravityCompat.START)

            return@setNavigationItemSelectedListener true
        }
    }
}