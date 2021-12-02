package com.example.gallery.util

import com.example.gallery.data.GalleryItem

sealed class GalleryState {

    object Uninitialized : GalleryState()

    data class ShowGalleryList(
        val list : List<GalleryItem>
    ) : GalleryState()

}