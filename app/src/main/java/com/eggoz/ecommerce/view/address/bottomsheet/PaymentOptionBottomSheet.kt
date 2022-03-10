package com.eggoz.ecommerce.view.address.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eggoz.ecommerce.databinding.BottomSheetPaymentOptionBinding
import com.eggoz.ecommerce.databinding.FragmentAddressBinding
import com.eggoz.ecommerce.network.model.OrderDetail
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PaymentOptionBottomSheet(val callback: (Boolean) -> (Unit)) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetPaymentOptionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetPaymentOptionBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init(){

        binding.apply {
            layoutWallet.setOnClickListener {
                callback(true)
                dismiss()
            }
            layoutCardUpi.setOnClickListener {
                callback(false)
                dismiss()
            }
        }

    }
}