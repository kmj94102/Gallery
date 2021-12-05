package com.example.gallery.ui

import com.example.gallery.data.GalleryItem

sealed class GalleryState {

    object Uninitialized : GalleryState()

    data class ShowGalleryList(
        val list : List<GalleryItem>
    ) : GalleryState()

}