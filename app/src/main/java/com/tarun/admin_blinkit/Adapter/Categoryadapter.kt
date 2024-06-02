package com.tarun.admin_blinkit.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tarun.admin_blinkit.Models.Category
import com.tarun.admin_blinkit.databinding.ItemlistBinding


class CategoryAdapter(val categorylist: ArrayList<Category>, val getcategory: (Category) -> Unit):
    RecyclerView.Adapter<CategoryAdapter.categoryviewholder>() {

    inner class categoryviewholder(var binding: ItemlistBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): categoryviewholder {
        val binding= ItemlistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return categoryviewholder(binding)
    }

    override fun getItemCount(): Int {
        return categorylist.size
    }

    override fun onBindViewHolder(holder: categoryviewholder, position: Int) {
        val category=categorylist[position]
        holder.binding.apply {
            ivcategory.setImageResource(category.image!!)
            categorytitle.text=category.title
        }

        holder.itemView.setOnClickListener {
            getcategory(category)
        }
    }
}