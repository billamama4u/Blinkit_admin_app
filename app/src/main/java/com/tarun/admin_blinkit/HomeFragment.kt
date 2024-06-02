package com.tarun.admin_blinkit

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tarun.admin_blinkit.Adapter.AdapterProduct
import com.tarun.admin_blinkit.Adapter.CategoryAdapter
import com.tarun.admin_blinkit.Models.Category
import com.tarun.admin_blinkit.Models.Product
import com.tarun.admin_blinkit.ViewModel.AuthViewModels
import com.tarun.admin_blinkit.ViewModel.Dataupload
import com.tarun.admin_blinkit.databinding.EditLayoutBinding
import com.tarun.admin_blinkit.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val viewmodel: Dataupload by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: AdapterProduct
    private val authViewModels: AuthViewModels by viewModels()
    private var filteringProducts: FilteringProducts? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        statusbarcolour()
        setcategory()
        onlogout()
        getAllProducts("All")
        return binding.root
    }

    private fun onLogOutClicked() {
            val dialog= androidx.appcompat.app.AlertDialog.Builder(requireContext())
            val alertDialog=dialog.create()
            dialog.setTitle("Log Out")
                .setMessage("Are you sure you want to log out").setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, which ->
                        startActivity(Intent(requireContext(),AdminMainActivity::class.java))
                        requireActivity().finish()
                        authViewModels.logOutUser()
                    }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                    alertDialog.dismiss()
                }).show().setCancelable(false)
    }

    private fun onlogout() {
        binding.tbhome.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.logout->{
                        onLogOutClicked()
                        true
                    }else->{
                        false
                    }
                }
        }
    }

    private fun searchproducts() {
        binding.Searchet.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                filteringProducts?.filter(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun statusbarcolour() {
        activity?.window?.apply {
            val statusbarcolour = ContextCompat.getColor(requireContext(), R.color.yellow)
            statusBarColor = statusbarcolour
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    private fun setcategory() {
        val categorylist = ArrayList<Category>()
        for (i in 0 until Constant.allproductcategory.size) {
            categorylist.add(Category(Constant.allproductcategory[i], Constant.producticon[i]))
        }
        binding.rvcategories.adapter = CategoryAdapter(categorylist, ::getcategory)
    }

    private fun getAllProducts(title: String?) {
        binding.shimmer.visibility = View.VISIBLE
        viewmodel.getAllProducts(title).observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.rvitems.visibility = View.INVISIBLE
                binding.shimmer.visibility = View.INVISIBLE
                binding.tvitem.visibility = View.VISIBLE
            } else {
                binding.rvitems.visibility = View.VISIBLE
                binding.tvitem.visibility = View.INVISIBLE
                binding.shimmer.visibility = View.INVISIBLE
                adapter = AdapterProduct(::onEditButtonClicked)
                adapter.originallist = it as ArrayList<Product>
                binding.rvitems.adapter = adapter
                adapter.differ.submitList(it)

                // Initialize FilteringProducts and set as the adapter's filter
                filteringProducts = FilteringProducts(adapter, adapter.originallist)
                searchproducts()
            }
        }
    }

    private fun getcategory(category: Category) {
        getAllProducts(category.title)
    }

    private fun onEditButtonClicked(product: Product) {
        val editProduct = EditLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        editProduct.apply {
            editproducttitle.setText(product.productTitle)
            editproductcategory.setText(product.productCategory)
            editproductquantity.setText(product.productQuantity.toString())
            editproductprice.setText(product.productPrice.toString())
            editproductstock.setText(product.productStock.toString())
            editproductunit.setText(product.productUnit)
            editproductype.setText(product.productType)
        }

        val alertDialog = AlertDialog.Builder(requireContext()).setView(editProduct.root).create()
        alertDialog.show()

        editProduct.editbtn.setOnClickListener {
            editProduct.apply {
                editproductunit.isEnabled = true
                editproductprice.isEnabled = true
                editproductstock.isEnabled = true
                editproductype.isEnabled = true
                editproductcategory.isEnabled = true
                editproducttitle.isEnabled = true
                editproductquantity.isEnabled = true
            }
        }
        setAutotext(editProduct)

        editProduct.savebtn.setOnClickListener {
            lifecycleScope.launch {
                product.apply {
                    productTitle = editProduct.editproducttitle.text.toString()
                    productQuantity = editProduct.editproductquantity.text.toString().toInt()
                    productType = editProduct.editproductype.text.toString()
                    productCategory = editProduct.editproductcategory.text.toString()
                    productStock = editProduct.editproductstock.text.toString().toInt()
                    productPrice = editProduct.editproductprice.text.toString().toInt()
                    productUnit = editProduct.editproductunit.text.toString()
                }
                viewmodel.saveupdatedproduct(product, requireContext())
            }
            alertDialog.dismiss()
        }
    }

    private fun setAutotext(editProduct: EditLayoutBinding) {
        val units = ArrayAdapter(requireContext(), R.layout.show_list, Constant.units)
        val category = ArrayAdapter(requireContext(), R.layout.show_list, Constant.allproductcategory)
        val type = ArrayAdapter(requireContext(), R.layout.show_list, Constant.allProductType)

        editProduct.apply {
            editproductcategory.setAdapter(category)
            editproductunit.setAdapter(units)
            editproductype.setAdapter(type)
        }
    }
}
