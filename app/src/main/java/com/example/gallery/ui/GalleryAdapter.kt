package com.example.gallery.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gallery.data.GalleryItem
import com.example.gallery.databinding.ItemGalleryItemBinding
import java.util.ArrayList

class GalleryAdapter(
    private val itemClickListener : (Int) -> Unit,
    private val itemCountListener : (Int) -> Unit
) : ListAdapter<GalleryItem, GalleryAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding : ItemGalleryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(galleryItem: GalleryItem) {

            binding.root.setOnClickListener {
                itemClickListener(bindingAdapterPosition)
            }

            binding.viewBackground.isVisible = galleryItem.isChecked

            binding.checkBox.apply {
                isChecked = galleryItem.isChecked
                setOnCheckedChangeListener { _, isChecked ->
                    binding.viewBackground.isVisible = isChecked
                    currentList[bindingAdapterPosition].isChecked = isChecked

                    itemCountListener(getSelectItemList().size)
                }
            }

            Glide
                .with(binding.imageView.context)
                .load(galleryItem.contentUri)
                .into(binding.imageView)

        }
    }

    fun getSelectItemList() = currentList.filter { it.isChecked }

    fun setSelectItemList(list: ArrayList<GalleryItem>?) {
        list?.forEach { item ->
            val index = currentList.indexOfFirst { it.id == item.id }
            if (currentList[index].isChecked != item.isChecked){
                currentList[index].isChecked = item.isChecked
                notifyItemChanged(index)
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