package com.tarun.admin_blinkit.Adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tarun.admin_blinkit.databinding.ItemViewBinding

class ImageAdapter(private val imageUri: ArrayList<Uri>) : RecyclerView.Adapter<ImageAdapter.ImageHolder>() {

    inner class ImageHolder(val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        return ImageHolder(ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return imageUri.size
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val image = imageUri[position]
        holder.binding.productimg.setImageURI(image)

        holder.binding.closebtn.setOnClickListener {
            imageUri.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, imageUri.size)
        }
    }
}
