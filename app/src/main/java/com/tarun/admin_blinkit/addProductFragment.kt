package com.tarun.admin_blinkit

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tarun.admin_blinkit.Adapter.ImageAdapter
import com.tarun.admin_blinkit.Models.Product
import com.tarun.admin_blinkit.ViewModel.Dataupload
import com.tarun.admin_blinkit.databinding.FragmentAddProductBinding
import kotlinx.coroutines.launch


class addProductFragment : Fragment() {

    private val viewmodel: Dataupload by viewModels()
    private lateinit var binding:FragmentAddProductBinding
    private var imageuri=ArrayList<Uri>()
    private val selectimage=registerForActivityResult(ActivityResultContracts.GetMultipleContents()){
        val imagelist= it.take(5)
        imageuri.clear()
        imageuri.addAll(imagelist)
        binding.rvproductimg.adapter=ImageAdapter(imageuri)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentAddProductBinding.inflate(layoutInflater)
        statusbarcolour()
        setAutotext()
        gettingimages()
        addingtodatabase()
        return binding.root
    }

    private fun statusbarcolour(){
        activity?.window?.apply {
            val statusbarcolour = ContextCompat.getColor(requireContext(),R.color.yellow)
            statusBarColor= statusbarcolour
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    private fun setAutotext() {
        val units=ArrayAdapter(requireContext(),R.layout.show_list,Constant.units)
        val category=ArrayAdapter(requireContext(),R.layout.show_list,Constant.allproductcategory)
        val type=ArrayAdapter(requireContext(),R.layout.show_list,Constant.allProductType)

        binding.apply {
            productcategory.setAdapter(category)
            productunit.setAdapter(units)
            productype.setAdapter(type)
        }
    }

    private fun gettingimages() {
        binding.productimagebtn.setOnClickListener {
            selectimage.launch("image/*")
        }
    }

    private fun addingtodatabase() {
        binding.productaddbtn.setOnClickListener {
            Utils.showDialogue(requireContext(),"Uploading Product & it\'s data.....")
            val productTitle=binding.producttitle.text.toString()
            val productQuantity=binding.productquantity.text.toString()
            val productUnit=binding.productunit.text.toString()
            val productCategory=binding.productcategory.text.toString()
            val productType=binding.productype.text.toString()
            val productPrice=binding.productprice.text.toString()
            val productStock=binding.productstock.text.toString()

            if(productPrice.isEmpty()||productCategory.isEmpty()||productQuantity.isEmpty()||productStock.isEmpty()||productTitle.isEmpty()||productUnit.isEmpty()||productType.isEmpty()){
                Utils.hideDialogue()
                Utils.showToast(requireContext(),"None of the entries can be empty")
            }
            else if(imageuri.isEmpty()){
                Utils.hideDialogue()
                Utils.showToast(requireContext(),"Please select images")
            }
            else{
                var product= Product(id = Utils.getRandomId(),
                    productTitle= productTitle,
                    productCategory=productCategory,productType= productType,
                    productQuantity=productQuantity.toInt(),productUnit=productUnit,
                    productPrice=productPrice.toInt(),productStock= productStock.toInt(),
                    itemCount = 0 ,adminUid = Utils.getauthinstance().currentUser?.uid.toString())

                saveImage(product)
            }
        }
    }

    private fun saveImage(product: Product) {
        viewmodel.saveimage(imageuri)
        lifecycleScope.launch {
            viewmodel.isImageUploaded.collect{
                if (it){
                    Utils.hideDialogue()
                    getUrls(product)
                }
            }
        }

    }

    private fun getUrls(product: Product) {
        Utils.showDialogue(requireContext(),"Publishing Product....")
        lifecycleScope.launch {
            viewmodel.downloadedurl.collect{
                var urls=it
                product.productimages=urls
                saveproduct(product,)
            }
        }

    }

    private fun saveproduct(product: Product){
        viewmodel.savedata(product)
        lifecycleScope.launch{
            viewmodel.isProductUploaded.collect{
                if (it) { // Launch only if product is uploaded and intent hasn't been launched
                    Utils.hideDialogue()
                    startActivity(Intent(requireActivity(),AdminPageMainActivity::class.java))
                }
            }
        }
    }
}