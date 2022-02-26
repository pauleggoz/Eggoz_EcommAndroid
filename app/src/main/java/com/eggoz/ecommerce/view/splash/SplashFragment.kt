package com.eggoz.ecommerce.view.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentSplashBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private var userPreferences: UserPreferences?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
/*
        val bundle = Bundle()
        bundle.putString("screen_name", name.get(position))
        bundle.putString("city_name", city)*/
        @Suppress("DEPRECATION")
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {

            requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        init()

        return binding.root
    }

    private fun init(){

        userPreferences= UserPreferences(requireContext())

        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                val cityid= userPreferences?.city?.first()
                val locid = userPreferences?.loc?.first()
                val token = userPreferences?.authtoken?.first()
                if (cityid==null||cityid==-1)
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.nav_city)
                else if (locid==null||locid==-1)
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.nav_loc)
                else if (token==""||token==null)
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.nav_sigin1)
                else
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.nav_home)

            }
            @Suppress("DEPRECATION")
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {

                requireActivity().window.insetsController?.show(WindowInsets.Type.statusBars())
            } else {
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        }, 3000)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}