package com.eggoz.ecommerce.view.order.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.ViewModelFactory
import com.eggoz.ecommerce.localdata.UserPreferences
import com.eggoz.ecommerce.databinding.FragmentOrderStatusBinding
import com.eggoz.ecommerce.utils.Loadinddialog
import com.eggoz.ecommerce.view.home.adapter.OrderInLineAdapter
import com.eggoz.ecommerce.view.order.adapter.OrderStatusHistoryAdapter
import com.eggoz.ecommerce.view.order.viewmodel.OrderStatusRepository
import com.eggoz.ecommerce.view.order.viewmodel.OrderStatusViewModel
import com.eggoz.ecommerce.view.profile.adapter.OrderItemAdapter

class OrderStatusFragment : Fragment() {
    private lateinit var binding: FragmentOrderStatusBinding
    private lateinit var adapterHist: OrderStatusHistoryAdapter
    private lateinit var adapter: OrderItemAdapter
    private lateinit var viewModel: OrderStatusViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderStatusBinding.inflate(inflater, container, false)

        init()
        return binding.root
    }
    private fun init(){
        val loadingdialog = Loadinddialog()
        loadingdialog.create(requireContext())

        adapter=OrderItemAdapter()
        adapterHist=OrderStatusHistoryAdapter()

        val repository = OrderStatusRepository(UserPreferences(requireContext()),this.arguments?.getInt("id", -1) ?: -1)
        val viewmodelFat = ViewModelFactory(repository)


        viewModel = ViewModelProvider(this,viewmodelFat)[OrderStatusViewModel::class.java]

        viewModel.orderevent.observe(viewLifecycleOwner) {

            adapterHist.submitList(it.results)

        }

        binding.apply {
            viewOrderadapter = adapter
            viewOrderHisadapter = adapterHist

            conHistory.setOnClickListener {
                if (recHistory.visibility==View.VISIBLE)
                recHistory.visibility= View.GONE
                else
                    recHistory.visibility=View.VISIBLE
                if (recHistory.visibility==View.VISIBLE)
                    imgSel.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_keyboard_arrow_down_24))
                else

                    imgSel.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_keyboard_arrow_up_24))
            }

            /*btnAddQuery.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("id", viewModel.order_id)
                Navigation.findNavController(binding.recOrderInline)
                    .navigate(R.id.action_nav_orderdetail_to_nav_addQuery, bundle)
            }*/
        }
        viewModel.apply {
            order.observe(viewLifecycleOwner){
                binding.apply {
                    txtOrderId.text= it.orderId ?:""
                    txtOrderStatus.text= it.status ?:""
                }
                if (loadingdialog.isShowing())
                loadingdialog.dismiss()
                adapter.submitList(it.orderLines?.orderItems)
            }
        }

    }

}