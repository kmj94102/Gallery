package com.example.gallery.ui

import android.app.Application
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gallery.BaseViewModel
import com.example.gallery.data.GalleryItem
import com.example.gallery.util.GalleryUtil
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

    override fun fetchData(): Job = viewModelScope.launch {}

    fun searchGalleryList(){
        imageList.addAll(GalleryUtil(application).getImageList())
        _stateLiveData.postValue(GalleryState.ShowGalleryList(imageList))
    }

}