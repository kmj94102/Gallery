package com.example.gallery.data

import android.net.Uri
import java.util.*

data class GalleryItem (
    val id : Long,
    val displayName : String,
    val dateTaken : Date,
    val contentUri : Uri,
    var isChecked : Boolean = false
)