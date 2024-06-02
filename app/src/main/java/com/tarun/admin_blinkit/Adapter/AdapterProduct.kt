package com.tarun.admin_blinkit.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.models.SlideModel
import com.tarun.admin_blinkit.FilteringProducts
import com.tarun.admin_blinkit.Models.Product
import com.tarun.admin_blinkit.databinding.ItemViewProductBinding

class AdapterProduct(val onEditButtonClicked: (Product) -> Unit) : RecyclerView.Adapter<AdapterProduct.ProductViewHolder>(), Filterable {

    inner class ProductViewHolder(var binding: ItemViewProductBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)
    var originallist = ArrayList<Product>()

    private var filteringProducts: FilteringProducts? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ItemViewProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]

        holder.binding.apply {
            val itemlist = ArrayList<SlideModel>()

            val list = product.productimages
            if (list != null) {
                for (i in 0 until list.size) {
                    itemlist.add(SlideModel(list[i].toString()))
                }
            }
            ivslider.setImageList(itemlist)

            tvproducttitle.text = product.productTitle.toString()
            tvprice.text = "₹${product.productPrice}"
            tvproductquantity.text = "${product.productQuantity}${product.productUnit}"

            editbtn.setOnClickListener {
                onEditButtonClicked(product)
            }
        }
    }

    override fun getFilter(): Filter {
        if (filteringProducts == null) {
            filteringProducts = FilteringProducts(this, originallist)
        }
        return filteringProducts as FilteringProducts
    }
}
