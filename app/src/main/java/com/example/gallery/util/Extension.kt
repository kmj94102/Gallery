package com.example.gallery.util

inline fun <reified T>listToArrayList(list : List<T>?) : ArrayList<T>{
    val arrayList : ArrayList<T> = arrayListOf()
    list?.toTypedArray()?.let {
        arrayList.addAll(it)
    }
    return arrayList
}