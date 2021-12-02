package com.example.gallery

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {

    abstract fun fetchData() : Job

}