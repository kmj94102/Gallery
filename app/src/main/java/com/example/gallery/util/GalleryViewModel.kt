package com.example.gallery.util

import android.app.Application
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gallery.BaseViewModel
import com.example.gallery.data.GalleryItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class GalleryViewModel(
    private val application: Application
) : BaseViewModel() {

    private val _stateLiveData = MutableLiveData<GalleryState>(GalleryState.Uninitialized)
    val stateLiveData : LiveData<GalleryState>
        get() = _stateLiveData

    private val imageList = mutableListOf<GalleryItem>()

    override fun fetchData(): Job = viewModelScope.launch {
        init()
    }

    private fun init(){
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
        )

        val selection = "${MediaStore.Images.Media.DATE_TAKEN} >= ?"
        val selectionArgs = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        val cursor = application.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        cursor?.use {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val dateTaken = Date(cursor.getLong(dateTakenColumn))
                val displayName = cursor.getString(displayNameColumn)
                val contentUri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id.toString()
                )
                imageList.add(
                    GalleryItem(
                        id = id,
                        dateTaken = dateTaken,
                        displayName = displayName,
                        contentUri = contentUri
                    )
                )
                Log.d("+++++", "id: $id, display_name: $displayName, date_taken: $dateTaken, content_uri: $contentUri")
            }
        }

        _stateLiveData.postValue(GalleryState.ShowGalleryList(imageList))
    }

}