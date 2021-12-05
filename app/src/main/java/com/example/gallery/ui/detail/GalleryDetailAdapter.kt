package com.example.gallery.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gallery.data.GalleryItem
import com.example.gallery.databinding.ItemGalleryDetailItemBinding
import java.util.ArrayList

class GalleryDetailAdapter(
    private val listener : () -> Unit
) : ListAdapter<GalleryItem, GalleryDetailAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemGalleryDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(galleryItem: GalleryItem) {
            Glide
                .with(binding.imageView.context)
                .load(galleryItem.contentUri)
                .into(binding.imageView)

            binding.imageView.setOnClickListener {
                listener()
            }
        }
    }

    fun getSelectItemList() = currentList.filter { it.isChecked }

    fun setSelectItemList(list: ArrayList<GalleryItem>?) {
        list?.forEach { item ->
            val index = currentList.indexOfFirst { it.id == item.id }
            currentList[index].isChecked = item.isChecked
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemGalleryDetailItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<GalleryItem>() {
            override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean =
                oldItem.contentUri == newItem.contentUri
        }
    }
}