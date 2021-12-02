package com.example.gallery.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gallery.data.GalleryItem
import com.example.gallery.databinding.ItemGalleryItemBinding

class GalleryAdapter : ListAdapter<GalleryItem, GalleryAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding : ItemGalleryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(galleryItem: GalleryItem) {
            Glide
                .with(binding.imageView.context)
                .load(galleryItem.contentUri)
                .into(binding.imageView)

            binding.checkBox.apply {
                isChecked = galleryItem.isChecked
                setOnCheckedChangeListener { _, isChecked ->
                    binding.viewBackground.isVisible = isChecked
                    currentList[bindingAdapterPosition].isChecked = isChecked
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemGalleryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<GalleryItem>(){
            override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean = oldItem.contentUri == newItem.contentUri
        }
    }

}