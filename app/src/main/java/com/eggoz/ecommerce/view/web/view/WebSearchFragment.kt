package com.eggoz.ecommerce.view.web.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.eggoz.ecommerce.databinding.FragmentWebSearchBinding
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.web.viewmodel.WebSearchViewModel


class WebSearchFragment : Fragment() {
    private lateinit var binding: FragmentWebSearchBinding
    private lateinit var viewModel: WebSearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebSearchBinding.inflate(inflater, container, false)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        init()
        return binding.root
    }
    private fun init(){

        val loadingdialog = Loadinddialog()
        loadingdialog.create(requireContext())
        viewModel = ViewModelProvider(this)[WebSearchViewModel::class.java]
        this.arguments.let {
            viewModel.url=it?.getString("url", "") ?: ""
        }



        binding.apply {
            btnBack.setOnClickListener {
                Navigation.findNavController(binding.root)
                    .popBackStack()
            }
            webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {

                if (progress == 100) loadingdialog.dismiss()
            }
        }
//        webview.settings.javaScriptEnabled = true
        webview.loadUrl(viewModel.url)

        }

    }
}