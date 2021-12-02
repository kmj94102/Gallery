package com.example.gallery.util

import android.annotation.SuppressLint
import android.os.Bundle
import com.example.gallery.BaseActivity
import com.example.gallery.data.GalleryItem
import com.example.gallery.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class GalleryActivity : BaseActivity<GalleryViewModel>() {

    private val binding : ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override val viewModel: GalleryViewModel by viewModel()

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

    override fun observeData() {
        viewModel.stateLiveData.observe(this){
            when(it){
                is GalleryState.Uninitialized -> {
                    initViews()
                }
                is GalleryState.ShowGalleryList -> {
                    setGalleryItems(it.list)
                }
            }

        }
    }

    private fun initViews() = with(binding) {
        recyclerView.adapter = GalleryAdapter()
    }

    private fun setGalleryItems(list: List<GalleryItem>) = with(binding.recyclerView) {
        (adapter as GalleryAdapter).submitList(list)
    }

}