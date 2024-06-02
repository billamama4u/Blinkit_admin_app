package com.tarun.admin_blinkit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tarun.admin_blinkit.Adapter.cartadapter
import com.tarun.admin_blinkit.ViewModel.Dataupload
import com.tarun.admin_blinkit.databinding.FragmentOrderStatusBinding


class orderStatusFragment : Fragment() {

    lateinit var binding: FragmentOrderStatusBinding
    private val viewmodel: Dataupload by viewModels()
    private lateinit var cartadapter: cartadapter
    private var id:String?=""
    private var status:Int=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentOrderStatusBinding.inflate(layoutInflater)
        getValues()
        settingStatus(status)
        getProductCategory()
        onchangestatusclick()
        onbackbuttonpresed()
        return binding.root
    }

    private fun onchangestatusclick() {
        binding.changestatus.setOnClickListener {
            val popmenu=PopupMenu(requireContext(),it)
            popmenu.menuInflater.inflate(R.menu.statys,popmenu.menu)
            popmenu.show()
            popmenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.received->{
                        settingStatus(1)
                        viewmodel.setstatus(id!!,1,requireContext())
                        true
                    }R.id.dispatched->{
                        settingStatus(2)
                    viewmodel.setstatus(id!!,2,requireContext())
                        true
                    }R.id.delivered->{
                        settingStatus(3)
                    viewmodel.setstatus(id!!,3,requireContext())
                        true
                    }
                    else->{
                        false
                    }
                }

            }
        }
    }

    private fun onbackbuttonpresed() {
        binding.orderappbrr.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_orderStatusFragment_to_orderFragment)
        }
    }

    private fun getValues() {
        val bundle = arguments
        id = bundle?.getString("orderId")
        status = bundle?.getInt("Status") ?: 0
        binding.tvuseraddress.text=bundle?.getString("userAddress").toString()
    }

    private fun settingStatus(status:Int){
        when(status){
            0 ->{
                binding.iv1.backgroundTintList= ContextCompat.getColorStateList(requireContext(), R.color.greyblue)
            }1 ->{
            binding.iv1.backgroundTintList= ContextCompat.getColorStateList(requireContext(), R.color.greyblue)
            binding.iv2.backgroundTintList= ContextCompat.getColorStateList(requireContext(), R.color.greyblue)
            binding.vw1.backgroundTintList= ContextCompat.getColorStateList(requireContext(), R.color.greyblue)
        }2 ->{
            binding.iv1.backgroundTintList= ContextCompat.getColorStateList(requireContext(), R.color.greyblue)
            binding.iv2.backgroundTintList= ContextCompat.getColorStateList(requireContext(), R.color.greyblue)
            binding.iv3.backgroundTintList= ContextCompat.getColorStateList(requireContext(), R.color.greyblue)
            binding.vw1.backgroundTintList= ContextCompat.getColorStateList(requireContext(), R.color.greyblue)
            binding.vw2.backgroundTintList= ContextCompat.getColorStateList(requireContext(), R.color.greyblue)
        }3 ->{
            binding.iv1.backgroundTintList= ContextCompat.getColorStateList(requireContext(), R.color.greyblue)
            binding.iv2.backgroundTintList= ContextCompat.getColorStateList(requireContext(), R.color.greyblue)
            binding.iv3.backgroundTintList= ContextCompat.getColorStateList(requireContext(), R.color.greyblue)
            binding.iv5.backgroundTintList= ContextCompat.getColorStateList(requireContext(), R.color.greyblue)
            binding.vw1.backgroundTintList= ContextCompat.getColorStateList(requireContext(), R.color.greyblue)
            binding.vw2.backgroundTintList= ContextCompat.getColorStateList(requireContext(), R.color.greyblue)
            binding.vw3.backgroundTintList= ContextCompat.getColorStateList(requireContext(), R.color.greyblue)
        }
        }
    }

    private fun getProductCategory() {


        viewmodel.getOrderProduct(id!!).observe(viewLifecycleOwner) { products ->

            cartadapter= cartadapter()
            binding.rvordersall.adapter = cartadapter
            cartadapter.differasync.submitList(products)


        }
    }


}